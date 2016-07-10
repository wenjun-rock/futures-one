package fwj.futures.resource.hedging.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.data.struct.Formula;
import fwj.futures.resource.hedging.entity.HedgingContract;
import fwj.futures.resource.hedging.entity.HedgingProdContract;
import fwj.futures.resource.hedging.repos.HedgingContractRepos;
import fwj.futures.resource.hedging.repos.HedgingProdContractRepos;
import fwj.futures.resource.hedging.vo.HedgingContractContainer;
import fwj.futures.resource.hedging.vo.HedgingContractDiff;
import fwj.futures.resource.price.buss.ContractDailyPriceBuss;
import fwj.futures.resource.price.buss.KLineBuss;
import fwj.futures.resource.price.entity.ContractKLine;
import fwj.futures.resource.price.repos.ContractKLineRepos;
import fwj.futures.resource.price.vo.Price;
import fwj.futures.resource.price.vo.Series;
import fwj.futures.resource.util.LineHelper;

@Component
public class HedgingProdContractBuss {
	
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private HedgingContractRepos hedgingRepo;

	@Autowired
	private ContractKLineRepos contractKLineRepo;

	@Autowired
	private HedgingProdContractRepos hedgingProdContractRepo;

	@Autowired
	private ContractDailyPriceBuss conPriceBuss;

	@Autowired
	private KLineBuss kLineBuss;

	@Cacheable(value = "HedgingContractBuss.queryHedgingContractByCode", key = "#code+','+#startDt.getTime()+','+#endDt.getTime()")
	public List<HedgingContractContainer> queryHedgingContractByCode(Date startDt, Date endDt, String code) {
		List<ContractKLine> contractKLineList = contractKLineRepo.findByCodeAndDtBetween(code, startDt, endDt);
		Map<Integer, List<ContractKLine>> contractKLineMap = contractKLineList.stream()
				.collect(Collectors.groupingBy(ContractKLine::getMonth, Collectors.toList()));
		return hedgingRepo.findByCode(code).stream().map(hedging -> {
			List<ContractKLine> contract1 = contractKLineMap.get(hedging.getContractMonth1());
			List<ContractKLine> contract2 = contractKLineMap.get(hedging.getContractMonth2());
			TreeMap<Date, Map<Integer, ContractKLine>> dateContractKLineMap = Stream
					.concat(contract1.stream(), contract2.stream()).collect(Collectors.groupingBy(ContractKLine::getDt,
							TreeMap::new, Collectors.toMap(ContractKLine::getMonth, Function.identity())));
			List<HedgingContractDiff> line = dateContractKLineMap.entrySet().stream()
					.filter(entry -> entry.getValue().size() == 2).map(entry -> {
				Map<Integer, ContractKLine> ele = entry.getValue();
				ContractKLine k1 = ele.get(hedging.getContractMonth1());
				ContractKLine k2 = ele.get(hedging.getContractMonth2());
				BigDecimal diff = k2.getEndPrice().subtract(k1.getEndPrice()).multiply(new BigDecimal(100))
						.divide(k1.getEndPrice(), 2, RoundingMode.FLOOR);
				Integer tradeVol1 = k1.getTradeVol();
				Integer tradeVol2 = k2.getTradeVol();
				return new HedgingContractDiff(diff, tradeVol1, tradeVol2, entry.getKey());
			}).collect(Collectors.toList());

			return new HedgingContractContainer(hedging.getName(), line, LineHelper.foldLineInYear(line));
		}).collect(Collectors.toList());
	}

	@Cacheable("HedgingContractBuss.queryHedgingContractBasic")
	public List<HedgingContract> queryHedgingContractBasic(String code) {
		return hedgingRepo.findByCode(code);
	}

	public void refreshHedging() {
		List<HedgingProdContract> hedgingList = hedgingProdContractRepo.findAll();
		hedgingList.stream().forEach(hedging -> {
			try {
				this.refreshHedging(hedging);				
			} catch(Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		});
	}

	private void refreshHedging(HedgingProdContract hedging) {
		Calendar cal = Calendar.getInstance();
		Date end = cal.getTime();
		cal.add(Calendar.MONTH, -12);
		Date start = cal.getTime();
		Series series = this.getHedgingSeries(hedging, start, end);		
		int size = series.getPrices().size();
		List<BigDecimal> pList = series.getPrices().stream().map(Price::getP).sorted().collect(Collectors.toList());
		BigDecimal curr = series.getPrices().get(size - 1).getP();
		BigDecimal min = pList.get(0);
		BigDecimal down = pList.get(size / 10);
		BigDecimal up = pList.get(size * 9 / 10);
		BigDecimal max = pList.get(size - 1);
		BigDecimal range = up.subtract(down);
		BigDecimal rate1 = curr.subtract(down).divide(range, 4, RoundingMode.FLOOR);
		BigDecimal rate2 = up.subtract(curr).divide(range, 4, RoundingMode.FLOOR);
		BigDecimal rate = rate1.compareTo(rate2) > 0 ? rate1 : rate2;

		hedging.setCurr(curr);
		hedging.setMax(max);
		hedging.setMin(min);
		hedging.setUp(up);
		hedging.setDown(down);
		hedging.setRate(rate);
		hedging.setRefreshDt(end);
		hedgingProdContractRepo.saveAndFlush(hedging);
	}

	@SuppressWarnings("unchecked")
	private Series getHedgingSeries(HedgingProdContract hedging, Date start, Date end) {
		List<ContractKLine> kLine1 = conPriceBuss.queryWithRange(hedging.getCode1(), hedging.getMonth1(), start, end);
		List<ContractKLine> kLine2 = conPriceBuss.queryWithRange(hedging.getCode2(), hedging.getMonth2(), start, end);
		String name1 = kLine1.get(0).getName();
		String name2 = kLine2.get(0).getName();
		Formula f = Formula.create().putConstant(BigDecimal.ZERO) //
				.putMultinomial(name1, "1").putMultinomial(name2, "-1");
		return kLineBuss.calculateSeries(name1 + "-" + name2, f, kLine1, kLine2);
	}
	
	public Series getHedgingSeries(Integer id, Date start, Date end) {
		HedgingProdContract hedging = hedgingProdContractRepo.findOne(id);
		return this.getHedgingSeries(hedging, start, end);
	}

	public List<HedgingProdContract> listAll() {
		return hedgingProdContractRepo.findAll();	
	}

}

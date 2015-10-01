package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.price.ContractKLine;
import fwj.futures.resource.repository.hedging.HedgingContractRepository;
import fwj.futures.resource.repository.price.ContractKLineRepository;
import fwj.futures.resource.vo.HedgingContractContainer;
import fwj.futures.resource.vo.HedgingContractContainer.HedgingContractLine;
import fwj.futures.resource.vo.HedgingContractDiff;

@Component
public class HedgingContractBuss {

	@Autowired
	private HedgingContractRepository hedgingRepo;

	@Autowired
	private ContractKLineRepository contractKLineRepo;

	@Cacheable("HedgingContractBuss.queryHedgingContractByCode")
	public List<HedgingContractContainer> queryHedgingContractByCode(Date startDt, Date endDt, String code) {
		DateFormat df = new SimpleDateFormat("yyyy");
		List<ContractKLine> contractKLineList = contractKLineRepo.findByCodeAndDtBetween(code, startDt, endDt);
		Map<Integer, List<ContractKLine>> contractKLineMap = contractKLineList.stream()
				.collect(Collectors.groupingBy(ContractKLine::getContractMonth, Collectors.toList()));
		return hedgingRepo.findByCode(code).stream().map(hedging -> {
			List<ContractKLine> contract1 = contractKLineMap.get(hedging.getContractMonth1());
			List<ContractKLine> contract2 = contractKLineMap.get(hedging.getContractMonth2());
			TreeMap<Date, Map<Integer, ContractKLine>> dateContractKLineMap = Stream
					.concat(contract1.stream(), contract2.stream()).collect(Collectors.groupingBy(ContractKLine::getDt,
							TreeMap::new, Collectors.toMap(ContractKLine::getContractMonth, Function.identity())));
			Map<String, List<HedgingContractDiff>> diffMap = dateContractKLineMap.entrySet().stream()
					.filter(entry -> entry.getValue().size() == 2).map(entry -> {
				Map<Integer, ContractKLine> ele = entry.getValue();
				ContractKLine k1 = ele.get(hedging.getContractMonth1());
				ContractKLine k2 = ele.get(hedging.getContractMonth2());
				BigDecimal diff = k2.getEndPrice().subtract(k1.getEndPrice());
				Integer tradeVol1 = k1.getTradeVol();
				Integer tradeVol2 = k2.getTradeVol();
				String year = df.format(entry.getKey());
				Calendar cal = Calendar.getInstance();
				cal.setTime(entry.getKey());
				cal.set(Calendar.YEAR, 2012);
				Date cmpDt = cal.getTime();
				return new HedgingContractDiff(diff, tradeVol1, tradeVol2, year, cmpDt);
			}).collect(Collectors.groupingBy(HedgingContractDiff::getY, Collectors.toList()));
			List<HedgingContractLine> lines = diffMap.entrySet().stream()
					.map(entry -> new HedgingContractLine(entry.getKey(), entry.getValue())).sorted()
					.collect(Collectors.toList());
			return new HedgingContractContainer(hedging.getName(), lines);
		}).collect(Collectors.toList());

	}

}

package fwj.futures.resource.buss;

import java.math.BigDecimal;
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

import fwj.futures.resource.entity.hedging.HedgingContract;
import fwj.futures.resource.entity.price.ContractKLine;
import fwj.futures.resource.repository.hedging.HedgingContractRepository;
import fwj.futures.resource.repository.price.ContractKLineRepository;
import fwj.futures.resource.util.LineHelper;
import fwj.futures.resource.vo.HedgingContractContainer;
import fwj.futures.resource.vo.HedgingContractDiff;

@Component
public class HedgingContractBuss {

	@Autowired
	private HedgingContractRepository hedgingRepo;

	@Autowired
	private ContractKLineRepository contractKLineRepo;

	@Cacheable(value = "HedgingContractBuss.queryHedgingContractByCode", key = "#code+','+#startDt.getTime()+','+#endDt.getTime()")
	public List<HedgingContractContainer> queryHedgingContractByCode(Date startDt, Date endDt, String code) {
		List<ContractKLine> contractKLineList = contractKLineRepo.findByCodeAndDtBetween(code, startDt, endDt);
		Map<Integer, List<ContractKLine>> contractKLineMap = contractKLineList.stream()
				.collect(Collectors.groupingBy(ContractKLine::getContractMonth, Collectors.toList()));
		return hedgingRepo.findByCode(code).stream().map(hedging -> {
			List<ContractKLine> contract1 = contractKLineMap.get(hedging.getContractMonth1());
			List<ContractKLine> contract2 = contractKLineMap.get(hedging.getContractMonth2());
			TreeMap<Date, Map<Integer, ContractKLine>> dateContractKLineMap = Stream
					.concat(contract1.stream(), contract2.stream()).collect(Collectors.groupingBy(ContractKLine::getDt,
							TreeMap::new, Collectors.toMap(ContractKLine::getContractMonth, Function.identity())));
			List<HedgingContractDiff> line = dateContractKLineMap.entrySet().stream()
					.filter(entry -> entry.getValue().size() == 2).map(entry -> {
				Map<Integer, ContractKLine> ele = entry.getValue();
				ContractKLine k1 = ele.get(hedging.getContractMonth1());
				ContractKLine k2 = ele.get(hedging.getContractMonth2());
				BigDecimal diff = k2.getEndPrice().subtract(k1.getEndPrice());
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

}

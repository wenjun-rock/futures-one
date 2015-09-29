package fwj.futures.resource.buss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.price.ContractKLine;
import fwj.futures.resource.repository.price.ContractKLineRepository;
import fwj.futures.resource.web.vo.Price;
import fwj.futures.resource.web.vo.ProdContracts;
import fwj.futures.resource.web.vo.ProdContracts.Contract;

@Component
public class ContractDailyPriceBuss {

	@Autowired
	private ContractKLineRepository kLineRepository;

	@CacheEvict(value = { "ContractDailyPriceBuss.getConstractsByCode" }, allEntries = true)
	public void reload() {
	}

	@Cacheable(value = "ContractDailyPriceBuss.getConstractsByCode")
	public ProdContracts getConstractsByCode(String code, Date startDt, Date endDt) {
		List<ContractKLine> contractKLineList = kLineRepository.findByCodeAndDtBetween(code, startDt, endDt);
		Date latestDt = contractKLineList.stream().map(ContractKLine::getDt).max(Comparator.naturalOrder())
				.orElseGet(null);
		Map<String, List<ContractKLine>> kLineMap = contractKLineList.stream()
				.collect(Collectors.groupingBy(ContractKLine::getContractName));
		List<Contract> contracts = kLineMap.entrySet().stream().map(entry -> {
			String contract = entry.getKey();
			List<ContractKLine> lines = entry.getValue();
			Collections.sort(lines);
			List<Price> ks = lines.stream().map(kLine -> new Price(kLine.getDt(), kLine.getEndPrice()))
					.collect(Collectors.toList());
			long totalVol = lines.stream().map(kLine -> kLine.getTradeVol().longValue()).reduce(0L, (l, r) -> {
				return l + r;
			});
			ContractKLine latest = lines.get(lines.size() - 1);
			long latestVol = latest.getDt().compareTo(latestDt) == 0 ? latest.getTradeVol() : 0;
			return new Contract(contract, totalVol, latestVol, ks);
		}).sorted().collect(Collectors.toList());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return new ProdContracts(code, df.format(startDt), df.format(endDt), df.format(latestDt), contracts);
	}

}

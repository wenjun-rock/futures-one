package fwj.futures.resource.price.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.price.ContractKLine;
import fwj.futures.resource.price.entity.ProdIndex;
import fwj.futures.resource.price.repos.ProdIndexRepos;
import fwj.futures.resource.repository.price.ContractKLineRepository;

@Component
public class ProdIndexBuss {

	@Autowired
	private ContractKLineRepository conKlineRepos;

	@Autowired
	private ProdIndexRepos prodIndexRepos;

	private Logger log = Logger.getLogger(this.getClass());

	public void updateProdIndex(final String code, Date startDt, Date endDt) {
		List<ContractKLine> conKLineList = conKlineRepos.findByCodeAndDtBetween(code, startDt, endDt);
		Map<Date, List<ContractKLine>> kLineMap = conKLineList.stream()
				.collect(Collectors.groupingBy(ContractKLine::getDt));
		List<ProdIndex> prodIndexList = kLineMap.entrySet().stream().map(entry -> {
			Date dt = entry.getKey();
			List<ContractKLine> eleList = entry.getValue();
			BigDecimal b1 = BigDecimal.ZERO;
			BigDecimal b2 = BigDecimal.ZERO;
			for (ContractKLine ele : eleList) {
				b1 = b1.add(ele.getEndPrice().multiply(new BigDecimal(ele.getTradeVol())));
				b2 = b2.add(new BigDecimal(ele.getTradeVol()));
			}
			BigDecimal price = b1.divide(b2, 2, RoundingMode.FLOOR);
			ProdIndex prodIndex = new ProdIndex();
			prodIndex.setCode(code);
			prodIndex.setDt(dt);
			prodIndex.setPrice(price);
			return prodIndex;
		}).sorted().collect(Collectors.toList());

		int del = prodIndexRepos.deleteByCodeAndDtBetween(code, startDt, endDt);
		log.info(String.format("delete %d records for %s between %s and %s", del, code, startDt, endDt));

		prodIndexRepos.save(prodIndexList);
		prodIndexRepos.flush();
	}
}

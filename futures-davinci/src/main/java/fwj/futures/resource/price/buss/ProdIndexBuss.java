package fwj.futures.resource.price.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.price.entity.ContractKLine;
import fwj.futures.resource.price.entity.ProdIndex;
import fwj.futures.resource.price.repos.ContractKLineRepos;
import fwj.futures.resource.price.repos.ProdIndexRepos;

@Component
public class ProdIndexBuss {

	@Autowired
	private ContractKLineRepos conKlineRepos;

	@Autowired
	private ProdIndexRepos prodIndexRepos;

	private Logger log = Logger.getLogger(this.getClass());

	@Transactional
	public void updateProdIndex(final String code, Date startDt, Date endDt) {
		List<ContractKLine> conKLineList = conKlineRepos.findByCodeAndDtBetween(code, startDt, endDt);
		Map<Date, List<ContractKLine>> kLineMap = conKLineList.stream()
				.collect(Collectors.groupingBy(ContractKLine::getDt));
		List<ProdIndex> prodIndexList = new ArrayList<>();
		kLineMap.entrySet().stream().map(entry -> {
			Date dt = entry.getKey();
			List<ContractKLine> eleList = entry.getValue();
			BigDecimal b1 = BigDecimal.ZERO;
			int vol = 0;
			for (ContractKLine ele : eleList) {
				if (ele.getEndPrice() != null && ele.getTradeVol() != null) {
					b1 = b1.add(ele.getEndPrice().multiply(new BigDecimal(ele.getTradeVol())));
					vol += ele.getTradeVol();
				}
			}
			BigDecimal price = vol == 0 ? BigDecimal.ZERO : b1.divide(new BigDecimal(vol), 2, RoundingMode.FLOOR);
			ProdIndex prodIndex = new ProdIndex();
			prodIndex.setCode(code);
			prodIndex.setDt(dt);
			prodIndex.setPrice(price);
			prodIndex.setVol(vol);
			return prodIndex;
		}).sorted().forEach(prodIndex -> {
			if (prodIndexList.isEmpty()
					|| prodIndex.getVol() > prodIndexList.get(prodIndexList.size() - 1).getVol() * 0.05) {
				prodIndexList.add(prodIndex);
			}
		});

		int del = prodIndexRepos.deleteByCodeAndDtBetween(code, startDt, endDt);
		prodIndexRepos.flush();
		log.info(String.format("delete %d records for %s between %s and %s", del, code, startDt, endDt));

		prodIndexRepos.save(prodIndexList);
		prodIndexRepos.flush();
	}

	@Cacheable(value = "ProdIndexBuss.queryAscByCode")
	public List<ProdIndex> queryAscByCode(String code) {
		return Collections.unmodifiableList(prodIndexRepos.findByCodeOrderByDtAsc(code));
	}
}

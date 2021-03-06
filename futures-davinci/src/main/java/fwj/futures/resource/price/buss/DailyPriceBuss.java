package fwj.futures.resource.price.buss;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.price.entity.KLine;
import fwj.futures.resource.price.entity.ProdIndex;
import fwj.futures.resource.price.repos.KLineRepos;
import fwj.futures.resource.price.repos.ProdIndexRepos;
import fwj.futures.resource.price.vo.KLineGroup;
import fwj.futures.resource.price.vo.Price;
import fwj.futures.resource.price.vo.ProdDailyPrice;
import fwj.futures.resource.price.vo.Series;
import fwj.futures.resource.prod.buss.ProdBuss;
import fwj.futures.resource.prod.entity.Futures;

@Component
public class DailyPriceBuss {

	@Autowired
	private KLineRepos kLineRepos;
	
	@Autowired
	private ProdIndexRepos prodIndexRepos;

	@Autowired
	private ProdBuss productBuss;

	@Cacheable(value = "KLineBuss.queryDescByCode")
	public List<KLine> queryDescByCode(String code) {
		return Collections.unmodifiableList(kLineRepos.findByCodeOrderByDtDesc(code));
	}

	@Cacheable(value = "KLineBuss.queryAscByCode")
	public List<KLine> queryAscByCode(String code) {
		return Collections.unmodifiableList(kLineRepos.findByCodeOrderByDtAsc(code));
	}

	@Cacheable(value = "KLineBuss.queryAllGroup")
	public List<KLineGroup> queryAllGroup() {
		Map<Date, Map<String, KLine>> kLineMap = kLineRepos.findAll().stream()
				.collect(Collectors.groupingBy(KLine::getDt, Collectors.toMap(KLine::getCode, kLine -> kLine)));
		List<KLineGroup> list = kLineMap.entrySet().stream()
				.map(entry -> new KLineGroup(entry.getKey(), entry.getValue())).sorted().collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	@Cacheable(value = "KLineBuss.queryLatest")
	public KLineGroup queryLatest() {
		List<KLineGroup> list = queryAllGroup();
		return list.get(list.size() - 1);
	}

	@Cacheable(value = "KLineBuss.querySeriesByCode")
	public Series querySeriesByCode(String code, int month) {
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return Series.EMPTY;
		} else {
			List<KLine> kLineList = null;
			if (month < 0) {
				kLineList = this.queryAscByCode(code);
			} else {
				Calendar cal = Calendar.getInstance();
				Date endDt = cal.getTime();
				cal.add(Calendar.MONTH, -month);
				Date startDt = cal.getTime();
				kLineList = kLineRepos.findByCodeAndDtBetweenOrderByDtAsc(code, startDt, endDt);
			}
			List<Price> prices = kLineList.stream().map(kLine -> {
				return new Price(kLine.getDt(), kLine.getEndPrice());
			}).collect(Collectors.toList());
			return new Series(prod.getCode(), prod.getName(), prices);
		}
	}

	@CacheEvict(value = { "KLineBuss.queryDescByCode", "KLineBuss.queryAscByCode", "KLineBuss.queryAllGroup",
			"KLineBuss.querySeriesByCode" }, allEntries = true)
	public void reload() {
	}

	public ProdDailyPrice queryProdDailyPrice(String code, int month) {
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return null;
		} else {
			List<KLine> kLineList = null;
			List<ProdIndex> indexList = null; 
			if (month < 0) {
				// 所有
				kLineList = kLineRepos.findByCodeOrderByDtAsc(code);
				indexList = prodIndexRepos.findByCodeOrderByDtAsc(code);
			} else {
				// 指定月数
				Calendar cal = Calendar.getInstance();
				Date endDt = cal.getTime();
				cal.add(Calendar.MONTH, -month);
				Date startDt = cal.getTime();
				kLineList = kLineRepos.findByCodeAndDtBetweenOrderByDtAsc(code, startDt, endDt);
				indexList = prodIndexRepos.findByCodeAndDtBetweenOrderByDtAsc(code, startDt, endDt);
			}
			List<Price> mainPriceList = kLineList.stream().map(kLine -> {
				return new Price(kLine.getDt(), kLine.getEndPrice());
			}).collect(Collectors.toList());
			List<Price> indexPriceList = indexList.stream().map(index -> {
				return new Price(index.getDt(), index.getPrice());
			}).collect(Collectors.toList());
			return new ProdDailyPrice(prod.getCode(), prod.getName(), mainPriceList, indexPriceList);
		}
	}

}

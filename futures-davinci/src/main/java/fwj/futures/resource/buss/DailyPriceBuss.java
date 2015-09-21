package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.Futures;
import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.vo.KLineGroup;
import fwj.futures.resource.web.vo.Series;

@Component
public class DailyPriceBuss {

	@Autowired
	private KLineRepository kLineRepository;

	@Autowired
	private ProductBuss productBuss;

	@Cacheable(value = "KLineBuss.queryDescByCode")
	public List<KLine> queryDescByCode(String code) {
		return Collections.unmodifiableList(kLineRepository.findByCodeOrderByDtDesc(code));
	}

	@Cacheable(value = "KLineBuss.queryAscByCode")
	public List<KLine> queryAscByCode(String code) {
		return Collections.unmodifiableList(kLineRepository.findByCodeOrderByDtAsc(code));
	}

	@Cacheable(value = "KLineBuss.queryAllGroup")
	public List<KLineGroup> queryAllGroup() {
		Map<String, Map<String, KLine>> kLineMap = kLineRepository.findAll().stream()
				.collect(Collectors.groupingBy(KLine::getDt, Collectors.toMap(KLine::getCode, kLine -> kLine)));
		List<KLineGroup> list = kLineMap.entrySet().stream()
				.map(entry -> new KLineGroup(entry.getKey(), entry.getValue())).sorted().collect(Collectors.toList());
		return Collections.unmodifiableList(list);
	}

	@Cacheable(value = "KLineBuss.querySeriesByCode")
	public Series querySeriesByCode(String code) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return Series.EMPTY;
		} else {
			List<KLine> kLineList = this.queryAscByCode(code);
			List<Object[]> data = kLineList.stream().map(kLine -> {
				long time = 0;
				try {
					time = df.parse(kLine.getDt()).getTime();
				} catch (Exception e) {
				}
				BigDecimal price = kLine.getEndPrice();
				return new Object[] { time, price };
			}).collect(Collectors.toList());
			return new Series(prod.getCode(), prod.getName(), data.toArray(new Object[0][2]));
		}
	}

	@CacheEvict(value = { "KLineBuss.queryDescByCode", "KLineBuss.queryAscByCode", "KLineBuss.queryAllGroup",
			"KLineBuss.querySeriesByCode" }, allEntries = true)
	public void reload() {
	}

}

package fwj.futures.resource.trend.buss;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.price.buss.ProdIndexBuss;
import fwj.futures.resource.price.entity.ProdIndex;
import fwj.futures.resource.trend.indicator.EMA;
import fwj.futures.resource.trend.indicator.MA;
import fwj.futures.resource.trend.vo.ProdMA;
import fwj.futures.resource.web.vo.Price;
import fwj.futures.resource.web.vo.Series;

@Component
public class MovingAvgBuss {

	@Autowired
	private ProdIndexBuss prodIndexBuss;

	public ProdMA calProdMovingAverage(String code, int month) {
		List<ProdIndex> prodIndexList = prodIndexBuss.queryAscByCode(code);

		Date d = null;
		if (month > 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, Math.negateExact(month));
			d = cal.getTime();
		}
		final Date start = d;

		List<Price> priceList = prodIndexList.stream().filter(prodIndex -> {
			if (month > 0 && prodIndex.getDt().compareTo(start) < 0) {
				return false;
			} else {
				return true;
			}
		}).map(prodIndex -> {
			return new Price(prodIndex.getDt(), prodIndex.getPrice());
		}).collect(Collectors.toList());
		Series prodIndexLine = new Series(code, code, priceList);
		List<Series> mvAvgLineList = Arrays.asList(new EMA(5), new EMA(10), new EMA(20), new EMA(60), new MA(5),
				new MA(10), new MA(20), new MA(60)).stream().map(indicator -> {
					prodIndexList.stream().forEach(prodIndex -> {
						indicator.push(prodIndex.getDt(), prodIndex.getPrice());
					});
					if (month < 0) {
						return new Series("", indicator.getName(), indicator.getLine());
					} else {
						return new Series("", indicator.getName(), indicator.getLine().stream()
								.filter(price -> price.getD().compareTo(start) >= 0).collect(Collectors.toList()));
					}
				}).collect(Collectors.toList());
		return new ProdMA(code, prodIndexLine, mvAvgLineList);
	}

}

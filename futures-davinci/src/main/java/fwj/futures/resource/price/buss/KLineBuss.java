package fwj.futures.resource.price.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import fwj.futures.data.struct.Formula;
import fwj.futures.data.struct.Formula.Multinomial;
import fwj.futures.resource.price.vo.K;
import fwj.futures.resource.price.vo.KGroup;
import fwj.futures.resource.price.vo.Price;
import fwj.futures.resource.price.vo.Series;

@Component
public class KLineBuss {

	public <T extends K> Series transform2Series(List<T> kline) {
		if (kline.isEmpty()) {
			return Series.EMPTY;
		} else {
			List<Price> prices = kline.stream().map(kLineUnit -> {
				return new Price(kLineUnit.getDt(), kLineUnit.getEndPrice());
			}).collect(Collectors.toList());
			String name = kline.get(0).getName();
			return new Series(name, name, prices);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends K> Series calculateSeries(String name, Formula formula, List<T>... kLines) {
		Map<Date, Map<String, K>> kLineMap = Stream.of(kLines).flatMap(kLine -> kLine.stream())
				.collect(Collectors.groupingBy(K::getDt, Collectors.toMap(K::getName, k -> k)));
		List<KGroup> kGroupList = kLineMap.entrySet().stream()
				.map(entry -> new KGroup(entry.getKey(), entry.getValue())).sorted().collect(Collectors.toList());
		return calculateSeries(name, formula, kGroupList);
	}
	
	public Series calculateSeries(String name, Formula formula, List<KGroup> groupList) {
		List<Price> priceList = groupList.stream().map(group -> new Price(group.getDt(), this.calculate(formula, group)))
				.filter(price -> price.getP() != null).collect(Collectors.toList());
		return new Series(name, name, priceList);
	}
	
	public BigDecimal calculate(Formula formula, KGroup group) {
		BigDecimal result = formula.getConstant();
		for (Multinomial multinomial : formula.getMultinomials()) {
			K kLine = group.getkLineMap().get(multinomial.getCode());
			if (kLine == null) {
				return null;
			} else {
				result = result.add(multinomial.getCoefficient().multiply(kLine.getEndPrice()));
			}
		}
		return result.setScale(2, RoundingMode.FLOOR);
	}


}

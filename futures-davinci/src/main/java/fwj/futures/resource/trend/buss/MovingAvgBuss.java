package fwj.futures.resource.trend.buss;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.price.buss.ContractDailyPriceBuss;
import fwj.futures.resource.price.buss.ProdIndexBuss;
import fwj.futures.resource.price.entity.ContractKLine;
import fwj.futures.resource.price.entity.ProdIndex;
import fwj.futures.resource.price.vo.Price;
import fwj.futures.resource.price.vo.Series;
import fwj.futures.resource.trend.indicator.EMA;
import fwj.futures.resource.trend.indicator.Indicator;
import fwj.futures.resource.trend.indicator.MA;
import fwj.futures.resource.trend.vo.ProdMA;

@Component
public class MovingAvgBuss {

	@Autowired
	private ProdIndexBuss prodIndexBuss;

	@Autowired
	private ContractDailyPriceBuss contractDailyPriceBuss;

	public ProdMA calProdMovingAverage(String code, int month) {
		return calProdMovingAverage(code, month, Arrays.asList(new EMA(5), new EMA(10), new EMA(20), new EMA(60),
				new MA(5), new MA(10), new MA(20), new MA(60)));
	}

	public ProdMA calProdMovingAverage(String code, int month, Indicator indocator) {
		return calProdMovingAverage(code, month, Arrays.asList(indocator));
	}

	public ProdMA calProdMovingAverage(String code, int month, List<Indicator> indicatorList) {
		List<ProdIndex> prodIndexList = prodIndexBuss.queryAscByCode(code);

		Date d = null;
		if (month > 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, Math.negateExact(month));
			d = cal.getTime();
		}
		final Date start = d;

		List<Price> priceList = new ArrayList<>();
		List<Price> volList = new ArrayList<>();
		prodIndexList.stream().filter(prodIndex -> {
			if (month > 0 && prodIndex.getDt().compareTo(start) < 0) {
				return false;
			} else {
				return true;
			}
		}).forEach(prodIndex -> {
			priceList.add(new Price(prodIndex.getDt(), prodIndex.getPrice()));
			volList.add(new Price(prodIndex.getDt(), new BigDecimal(prodIndex.getVol())));
		});
		Series prodIndexLine = new Series(code, code, priceList);
		Series volLine = new Series(code, code, volList);
		List<Series> mvAvgLineList = indicatorList.stream().map(indicator -> {
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
		return new ProdMA(code, prodIndexLine, volLine, mvAvgLineList);
	}

	public ProdMA calContractMovingAverage(String code, int contract, int month) {
		return calContractMovingAverage(code, contract, month, Arrays.asList(new EMA(5), new EMA(10), new EMA(20),
				new EMA(60), new MA(5), new MA(10), new MA(20), new MA(60)));
	}

	public ProdMA calContractMovingAverage(String code, int contract, int month, Indicator indocator) {
		return calContractMovingAverage(code, contract, month, Arrays.asList(indocator));
	}

	public ProdMA calContractMovingAverage(String code, int contract, int month, List<Indicator> indicatorList) {
		List<ContractKLine> contractList = contractDailyPriceBuss.queryByCode(code, contract);

		Date d = null;
		if (month > 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, Math.negateExact(month));
			d = cal.getTime();
		}
		final Date start = d;

		List<Price> priceList = new ArrayList<>();
		List<Price> volList = new ArrayList<>();
		contractList.stream().filter(conK -> {
			if (month > 0 && conK.getDt().compareTo(start) < 0) {
				return false;
			} else {
				return true;
			}
		}).forEach(conK -> {
			priceList.add(new Price(conK.getDt(), conK.getEndPrice()));
			volList.add(new Price(conK.getDt(), new BigDecimal(conK.getTradeVol())));
		});
		String name = String.format("%s%02d", code, contract);
		Series prodIndexLine = new Series(name, name, priceList);
		Series volLine = new Series(name, name, volList);
		List<Series> mvAvgLineList = indicatorList.stream().map(indicator -> {
			contractList.stream().forEach(conK -> {
				indicator.push(conK.getDt(), conK.getEndPrice());
			});
			if (month < 0) {
				return new Series("", indicator.getName(), indicator.getLine());
			} else {
				return new Series("", indicator.getName(), indicator.getLine().stream()
						.filter(price -> price.getD().compareTo(start) >= 0).collect(Collectors.toList()));
			}
		}).collect(Collectors.toList());
		return new ProdMA(name, prodIndexLine, volLine, mvAvgLineList);
	}

}

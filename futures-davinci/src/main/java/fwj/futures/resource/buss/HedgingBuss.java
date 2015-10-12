package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.struct.Formula;
import fwj.futures.data.struct.Formula.Multinomial;
import fwj.futures.resource.entity.hedging.Hedging;
import fwj.futures.resource.entity.price.KLine;
import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.repository.hedging.HedgingRepository;
import fwj.futures.resource.vo.HedgingMonitor;
import fwj.futures.resource.vo.HedgingView;
import fwj.futures.resource.vo.KLineGroup;
import fwj.futures.resource.vo.UnitData;
import fwj.futures.resource.vo.UnitDataGroup;
import fwj.futures.resource.web.vo.Price;
import fwj.futures.resource.web.vo.Series;

@Component
public class HedgingBuss {

	@Autowired
	private HedgingRepository hedgingRepo;

	@Autowired
	private DailyPriceBuss dailyPriceBuss;

	@Autowired
	private RealTimePriceBuss realTimePriceBuss;

	public Hedging getById(Integer id) {
		return hedgingRepo.findOne(id);
	}

	public BigDecimal calculate(Formula fomular, KLineGroup group) {
		BigDecimal result = fomular.getConstant();
		for (Multinomial multinomial : fomular.getMultinomials()) {
			KLine kLine = group.getkLineMap().get(multinomial.getCode());
			if (kLine == null) {
				return null;
			} else {
				result = result.add(multinomial.getCoefficient().multiply(kLine.getEndPrice()));
			}
		}
		return result.setScale(2, RoundingMode.FLOOR);
	}

	public BigDecimal calculate(Formula fomular, UnitDataGroup unitDataGroup) {
		BigDecimal result = fomular.getConstant();
		for (Multinomial multinomial : fomular.getMultinomials()) {
			UnitData unit = unitDataGroup.getUnitData(multinomial.getCode());
			if (unit == null || unit == UnitData.DUMMY) {
				return null;
			} else {
				result = result.add(multinomial.getCoefficient().multiply(unit.getPrice()));
			}
		}
		return result.setScale(2, RoundingMode.FLOOR);
	}

	public List<Price> calculateKLine(Formula fomular, List<KLineGroup> groupList) {
		return groupList.stream().map(group -> new Price(group.getDt(), this.calculate(fomular, group)))
				.filter(price -> price.getP() != null).collect(Collectors.toList());
	}

	public List<Price> calculateKLine(String expression, List<KLineGroup> groupList) {
		return calculateKLine(Formula.parse(expression), groupList);
	}

	public List<Price> calculateUnitData(Formula fomular, List<UnitDataGroup> groupList) {
		return groupList.stream().map(group -> new Price(group.getDatetime(), this.calculate(fomular, group)))
				.filter(price -> price.getP() != null).collect(Collectors.toList());
	}

	public List<Price> calculateUnitData(String expression, List<UnitDataGroup> groupList) {
		return calculateUnitData(Formula.parse(expression), groupList);
	}

	public List<Series> compareProd(Futures f1, Futures f2) {
		Series s1 = dailyPriceBuss.querySeriesByCode(f1.getCode(), -1);
		Series s2 = dailyPriceBuss.querySeriesByCode(f2.getCode(), -1);
		Formula formula = Formula.create().putConstant(BigDecimal.ZERO).putMultinomial(f1.getCode(), "1")
				.putMultinomial(f2.getCode(), "-1");
		List<Price> diffPriceList = this.calculateKLine(formula, dailyPriceBuss.queryAllGroup());
		String diffName = f1.getName() + "-" + f2.getName();
		Series diffSeries = new Series("", diffName, diffPriceList);
		return Arrays.asList(s1, s2, diffSeries);
	}

	public List<HedgingView> queryHedging() {
		UnitDataGroup realTime = realTimePriceBuss.queryLatest();
		KLineGroup kline = dailyPriceBuss.queryLatest();
		return hedgingRepo.findAll().stream().map(hedging -> {
			Formula f = Formula.parse(hedging.getExpression());
			BigDecimal diffKline = this.calculate(f, kline);
			BigDecimal diffRealtime = this.calculate(f, realTime);
			BigDecimal diff = diffRealtime == null ? diffKline : diffRealtime;
			int mid = hedging.getMid();
			int down = hedging.getDown();
			int up = hedging.getUp();
			BigDecimal complete = null;
			if (diff.compareTo(new BigDecimal(mid)) > 0) {
				complete = diff.subtract(new BigDecimal(mid)).divide(new BigDecimal(up - mid), 4, RoundingMode.FLOOR);
			} else {
				complete = new BigDecimal(mid).subtract(diff).divide(new BigDecimal(mid - down), 4, RoundingMode.FLOOR);
			}
			return new HedgingView(hedging.getId(), hedging.getName(), f.toString(), complete, diffKline, diffRealtime,
					hedging.getUp(), hedging.getDown(), hedging.getMid(), hedging.getQ1(), hedging.getQ3());
		}).collect(Collectors.toList());
	}

	public HedgingMonitor monitorHedging(Integer id) {
		Hedging hedging = hedgingRepo.findOne(id);
		Formula formula = Formula.parse(hedging.getExpression());
		List<Price> realtimePrices = this.calculateUnitData(formula, realTimePriceBuss.queryAllAsc());
		List<Price> klinePrices = this.calculateKLine(formula, dailyPriceBuss.queryAllGroup());
		return new HedgingMonitor(id, hedging.getName(), formula.toString(), hedging.getStartDt(), hedging.getEndDt(),
				hedging.getDown(), hedging.getUp(), realtimePrices, klinePrices);
	}
}

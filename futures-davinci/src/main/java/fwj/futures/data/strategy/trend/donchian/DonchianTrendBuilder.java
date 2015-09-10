package fwj.futures.data.strategy.trend.donchian;

import java.math.BigDecimal;

import fwj.futures.data.process.DateGenerator;
import fwj.futures.data.strategy.trend.donchian.struct.DonchianTrend;
import fwj.futures.data.strategy.trend.donchian.struct.DonchianWave;
import fwj.futures.data.strategy.trend.donchian.struct.DonchianWave.Direction;
import fwj.futures.data.strategy.trend.donchian.struct.EMA;
import fwj.futures.data.strategy.trend.donchian.struct.FixedSizeEndPriceQueue;
import fwj.futures.data.strategy.trend.donchian.struct.FixedSizeMinMaxPriceQueue;
import fwj.futures.data.struct.DailyK;
import fwj.futures.data.struct.DailyKLine;

public class DonchianTrendBuilder {

	private String startDt; // 统计开始日期
	private String endDt; // 统计结束日期

	private int enterBreakout;
	private int exitBreakout;

	public DonchianTrendBuilder(String startDt, String endDt, int enterBreakout, int exitBreakout) {
		this.startDt = startDt;
		this.endDt = endDt;
		this.enterBreakout = enterBreakout;
		this.exitBreakout = exitBreakout;
	}

	public DonchianTrend createEndPriceDonchianTrend(DailyKLine kLine) throws Exception {

		int days = 0;
		DonchianTrend trend = new DonchianTrend(kLine.getProd(), enterBreakout, exitBreakout);
		FixedSizeEndPriceQueue enterQueue = new FixedSizeEndPriceQueue(enterBreakout);
		FixedSizeEndPriceQueue exitQueue = new FixedSizeEndPriceQueue(exitBreakout);
		EMA EMA25 = new EMA(25);
		EMA EMA350 = new EMA(350);
		for (String dt : DateGenerator.range(startDt, endDt)) {
			DailyK k = kLine.getDailyK(dt);
			if (k == null) {
				continue;
			}
			trend.includeDt(dt);
			BigDecimal price = k.getEndPrice();
			if (++days > enterBreakout) {
				DonchianWave currentWave = trend.getCurrentWave();
				if (currentWave == null) {
					// 当前未在唐奇安波段中
					if (EMA25.get().compareTo(EMA350.get()) > 0 && price.compareTo(enterQueue.getMax()) > 0) {
						// 向上突破
						trend.enterWave(Direction.UP, dt, price);
					} else if (EMA25.get().compareTo(EMA350.get()) < 0 && price.compareTo(enterQueue.getMin()) < 0) {
						// 向下突破
						trend.enterWave(Direction.DOWN, dt, price);
					} else {
						trend.prepare(price, enterQueue.getMin(), enterQueue.getMax(), EMA25.get(), EMA350.get());
					}
				} else if (currentWave != null) {
					// 当前处在唐奇安波段中
					currentWave.put(dt, price);
					if (currentWave.getDirection() == Direction.UP) {
						// 向上通道
						if (price.compareTo(exitQueue.getMin()) < 0) {
							// 退出
							trend.exitWave();
						}
					} else if (currentWave.getDirection() == Direction.DOWN) {
						// 向下通道
						if (price.compareTo(exitQueue.getMax()) > 0) {
							// 退出
							trend.exitWave();
						}
					}
				}
			}

			EMA25.update(price);
			EMA350.update(price);
			enterQueue.enqueue(price);
			exitQueue.enqueue(price);
		}

		return trend;
	}

	public DonchianTrend createMinMaxPriceDonchianTrend(DailyKLine kLine) throws Exception {

		int days = 0;
		DonchianTrend trend = new DonchianTrend(kLine.getProd(), enterBreakout, exitBreakout);
		FixedSizeMinMaxPriceQueue enterQueue = new FixedSizeMinMaxPriceQueue(enterBreakout);
		FixedSizeMinMaxPriceQueue exitQueue = new FixedSizeMinMaxPriceQueue(exitBreakout);
		EMA EMA25 = new EMA(25);
		EMA EMA350 = new EMA(350);
		for (String dt : DateGenerator.range(startDt, endDt)) {
			DailyK k = kLine.getDailyK(dt);
			if (k == null) {
				continue;
			}
			trend.includeDt(dt);
			BigDecimal price = k.getEndPrice();
			BigDecimal min = k.getMinPrice();
			BigDecimal max = k.getMaxPrice();
			if (++days > enterBreakout) {
				DonchianWave currentWave = trend.getCurrentWave();
				if (currentWave == null) {
					// 当前未在唐奇安波段中
					if (EMA25.get().compareTo(EMA350.get()) > 0 && price.compareTo(enterQueue.getMax()) > 0) {
						// 向上突破
						trend.enterWave(Direction.UP, dt, price);
					} else if (EMA25.get().compareTo(EMA350.get()) < 0 && price.compareTo(enterQueue.getMin()) < 0) {
						// 向下突破
						trend.enterWave(Direction.DOWN, dt, price);
					} else {
						trend.prepare(price, enterQueue.getMin(), enterQueue.getMax(), EMA25.get(), EMA350.get());
					}
				} else if (currentWave != null) {
					// 当前处在唐奇安波段中
					currentWave.put(dt, price);
					if (currentWave.getDirection() == Direction.UP) {
						// 向上通道
						if (price.compareTo(exitQueue.getMin()) < 0) {
							// 退出
							trend.exitWave();
						}
					} else if (currentWave.getDirection() == Direction.DOWN) {
						// 向下通道
						if (price.compareTo(exitQueue.getMax()) > 0) {
							// 退出
							trend.exitWave();
						}
					}
				}
			}

			EMA25.update(price);
			EMA350.update(price);
			enterQueue.enqueue(min, max);
			exitQueue.enqueue(min, max);
		}

		return trend;
	}

}

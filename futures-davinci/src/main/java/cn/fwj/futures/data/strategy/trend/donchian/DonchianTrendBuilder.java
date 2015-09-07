package cn.fwj.futures.data.strategy.trend.donchian;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianTrend;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianWave;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianWave.Direction;
import cn.fwj.futures.data.struct.DailyK;
import cn.fwj.futures.data.struct.DailyKLine;

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

	public DonchianTrend createDonchianTrend(DailyKLine kLine) throws Exception {
		DonchianTrend trend = new DonchianTrend(kLine.getProd(), enterBreakout, exitBreakout);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse(startDt);
		Date end = df.parse(endDt);
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		while (!cal.getTime().after(end)) {
			String dt = df.format(cal.getTime());
			DailyK k = kLine.getDailyK(dt);
			if (k != null) {
				trend.setEndDt(dt);
				if (trend.getStartDt() == null) {
					trend.setStartDt(dt);
				}
				nextK(trend, dt, k);
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		return trend;
	}

	private void nextK(DonchianTrend trend, String dt, DailyK k) {
		BigDecimal price = k.getEndPrice();
		DonchianWave currentWave = trend.getCurrentWave();
		if (currentWave == null) {
			// 当前未在唐奇安波段中
			if (price.compareTo(trend.getEnterQueue().getMax()) > 0) {
				// 向上突破
				trend.enter(Direction.UP, dt, price);
			} else if (price.compareTo(trend.getEnterQueue().getMin()) < 0) {
				// 向下突破
				trend.enter(Direction.DOWN, dt, price);
			}
		} else if (currentWave != null) {
			// 当前处在唐奇安波段中
			if (currentWave.getDirection() == Direction.UP) {
				// 向上通道
				if (currentWave.getMfePrice().compareTo(k.getMaxPrice()) < 0) {
					currentWave.setMfeDt(dt);
					currentWave.setMfePrice(k.getMaxPrice());
				}
				if (currentWave.getMaePrice().compareTo(k.getMinPrice()) > 0) {
					currentWave.setMaeDt(dt);
					currentWave.setMaePrice(k.getMinPrice());
				}
				if (price.compareTo(trend.getExitQueue().getMin()) < 0) {
					// 退出
					trend.exit(dt, price);
				}
			} else if (currentWave.getDirection() == Direction.DOWN) {
				// 向下通道
				if (currentWave.getMfePrice().compareTo(k.getMinPrice()) > 0) {
					currentWave.setMfeDt(dt);
					currentWave.setMfePrice(k.getMinPrice());
				}
				if (currentWave.getMaePrice().compareTo(k.getMaxPrice()) < 0) {
					currentWave.setMaeDt(dt);
					currentWave.setMaePrice(k.getMaxPrice());
				}
				if (price.compareTo(trend.getExitQueue().getMax()) > 0) {
					// 退出
					trend.exit(dt, price);
				}
			}
		}
		trend.addK(k);

	}

}

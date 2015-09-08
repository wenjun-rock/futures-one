package cn.fwj.futures.data.strategy.trend.donchian.struct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianWave.Direction;

public class DonchianTrend {

	private Product product; // 产品
	private int enterBreakout;
	private int exitBreakout;

	private String startDt; // 统计开始日期
	private String endDt; // 统计结束日期
	private DonchianWave currentWave;
	private List<DonchianWave> histWave;
	private DonchianPrepare prepare;

	public DonchianTrend(Product product, int enterBreakout, int exitBreakout) {
		this.product = product;
		this.enterBreakout = enterBreakout;
		this.exitBreakout = exitBreakout;
		this.histWave = new ArrayList<>();
	}

	public Product getProduct() {
		return product;
	}

	public String getStartDt() {
		return startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public int getEnterBreakout() {
		return enterBreakout;
	}

	public int getExitBreakout() {
		return exitBreakout;
	}

	public DonchianWave getCurrentWave() {
		return currentWave;
	}

	public List<DonchianWave> getHistWave() {
		return histWave;
	}

	public DonchianPrepare getPrepare() {
		return prepare;
	}

	public void includeDt(String dt) {
		this.endDt = dt;
		if (this.startDt == null) {
			this.startDt = dt;
		}
	}

	public void enterWave(Direction direction, String dt, BigDecimal price) {
		prepare = null;
		currentWave = new DonchianWave(direction, dt, price);
		histWave.add(currentWave);
	}

	public void exitWave() {
		currentWave.setEnding(true);
		currentWave = null;
	}

	public void prepare(BigDecimal price, BigDecimal min, BigDecimal max, BigDecimal EMA25, BigDecimal EMA350) {
		prepare = new DonchianPrepare(price, min, max, EMA25, EMA350);
	}

	public class DonchianPrepare {

		private BigDecimal current;
		private BigDecimal min;
		private BigDecimal max;
		private BigDecimal EMA25;
		private BigDecimal EMA350;

		private DonchianPrepare(BigDecimal current, BigDecimal min, BigDecimal max, BigDecimal EMA25,
				BigDecimal EMA350) {
			this.current = current;
			this.min = min;
			this.max = max;
			this.EMA25 = EMA25;
			this.EMA350 = EMA350;
		}

		public String toString() {
			return String.format("%s|%s|%s|%s|%s|%s", current, min, max, EMA25, EMA350);
		}

	}

}

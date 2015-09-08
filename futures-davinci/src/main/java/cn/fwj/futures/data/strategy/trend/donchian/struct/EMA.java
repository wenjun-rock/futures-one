package cn.fwj.futures.data.strategy.trend.donchian.struct;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class EMA {

	final private int range;
	final private BigDecimal multi;
	final private BigDecimal divisor;

	private BigDecimal value;

	public EMA(int range) {
		this.range = range;
		this.multi = new BigDecimal(range - 1);
		this.divisor = new BigDecimal(range);
	}

	public void update(BigDecimal append) {
		value = value == null ? append : value.multiply(multi).add(append).divide(divisor, 2, RoundingMode.DOWN);
	}

	public int getRange() {
		return range;
	}

	public BigDecimal get() {
		return value;
	}

}

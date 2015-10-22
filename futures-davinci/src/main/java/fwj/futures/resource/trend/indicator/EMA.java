package fwj.futures.resource.trend.indicator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fwj.futures.resource.web.vo.Price;

public class EMA implements Indicator {

	private int range;
	private BigDecimal m;
	private BigDecimal d;
	private BigDecimal last = null;
	private List<Price> line = new ArrayList<>();

	public EMA(int range) {
		this.range = range;
		this.m = new BigDecimal(range - 1);
		this.d = new BigDecimal(range);
	}

	@Override
	public void push(Date dt, BigDecimal price) {
		if (last == null) {
			last = price;
		} else {
			last = last.multiply(m).add(price).divide(d, 2, RoundingMode.FLOOR);
		}
		line.add(new Price(dt, last));
	}

	public int getRange() {
		return range;
	}

	public BigDecimal getLast() {
		return last;
	}

	public List<Price> getLine() {
		return line;
	}

	@Override
	public String getName() {
		return "EMA" + range;
	}

}

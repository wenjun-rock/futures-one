package fwj.futures.resource.trend.indicator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import fwj.futures.resource.price.vo.Price;

public class MA implements Indicator {

	private int range;
	private BigDecimal d;
	private BigDecimal sum = BigDecimal.ZERO;
	private List<Price> line = new ArrayList<Price>();
	private LinkedList<BigDecimal> queue = new LinkedList<>();

	public MA(int range) {
		this.range = range;
		this.d = new BigDecimal(range);
	}

	@Override
	public void push(Date dt, BigDecimal price) {
		if(queue.size() == range) {
			sum = sum.subtract(queue.removeFirst());
		}
		queue.add(price);
		sum = sum.add(price);
		if(queue.size() == range) {
			line.add(new Price(dt, sum.divide(d, 2, RoundingMode.FLOOR)));	
		}
	}

	public int getRange() {
		return range;
	}

	public List<Price> getLine() {
		return line;
	}

	@Override
	public String getName() {
		return "MA" + range;
	}

}

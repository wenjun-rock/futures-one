package cn.fwj.futures.data.strategy.trend.donchian.struct;

import java.math.BigDecimal;

import cn.fwj.futures.data.struct.DailyK;

public class FixedSizeQueue {

	private int size;
	private DailyK[] queue;
	private int count = 0;

	public FixedSizeQueue(int size) {
		this.size = size;
		queue = new DailyK[size];
	}

	public DailyK enqueue(DailyK in) {
		int index = count % size;
		count++;
		DailyK previous = queue[index];
		queue[index] = in;
		return previous;
	}

	public BigDecimal getMax() {
		if (count < size) {
			return new BigDecimal(Integer.MAX_VALUE);
		}
		BigDecimal max = new BigDecimal(Integer.MIN_VALUE);
		for (DailyK element : queue) {
			if (element != null) {
				max = element.getMaxPrice().compareTo(max) > 0 ? element.getMaxPrice() : max;
			}
		}
		return max;
	}

	public BigDecimal getMin() {
		if (count < size) {
			return new BigDecimal(Integer.MIN_VALUE);
		}
		BigDecimal min = new BigDecimal(Integer.MAX_VALUE);
		for (DailyK element : queue) {
			if (element != null) {
				min = element.getMinPrice().compareTo(min) < 0 ? element.getMinPrice() : min;
			}
		}
		return min;
	}

}

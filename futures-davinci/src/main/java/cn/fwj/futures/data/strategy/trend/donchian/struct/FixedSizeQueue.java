package cn.fwj.futures.data.strategy.trend.donchian.struct;

import java.math.BigDecimal;

public class FixedSizeQueue {

	private int size;
	private BigDecimal[] queue;
	private int count = 0;

	public FixedSizeQueue(int size) {
		this.size = size;
		queue = new BigDecimal[size];
	}

	public BigDecimal enqueue(BigDecimal in) {
		int index = count % size;
		count++;
		BigDecimal previous = queue[index];
		queue[index] = in;
		return previous;
	}

	public BigDecimal getMax() {
		if (count < size) {
			return new BigDecimal(Integer.MAX_VALUE);
		}
		BigDecimal max = new BigDecimal(Integer.MIN_VALUE);
		for (BigDecimal element : queue) {
			if (element != null) {
				max = element.compareTo(max) > 0 ? element : max;
			}
		}
		return max;
	}

	public BigDecimal getMin() {
		if (count < size) {
			return new BigDecimal(Integer.MIN_VALUE);
		}
		BigDecimal min = new BigDecimal(Integer.MAX_VALUE);
		for (BigDecimal element : queue) {
			if (element != null) {
				min = element.compareTo(min) < 0 ? element : min;
			}
		}
		return min;
	}

}

package cn.fwj.futures.data.strategy.trend.donchian.struct;

import java.math.BigDecimal;

public class FixedSizeMinMaxPriceQueue {

	private int size;
	private BigDecimal[] minQueue;
	private BigDecimal[] maxQueue;
	private int count;

	public FixedSizeMinMaxPriceQueue(int size) {
		this.size = size;
		minQueue = new BigDecimal[size];
		maxQueue = new BigDecimal[size];
	}

	public BigDecimal enqueue(BigDecimal min, BigDecimal max) {
		int index = count++ % size;
		minQueue[index] = min;
		maxQueue[index] = max;
		return null;
	}

	public BigDecimal getMax() {
		BigDecimal max = new BigDecimal(Integer.MIN_VALUE);
		for (BigDecimal element : maxQueue) {
			if (element != null) {
				max = element.compareTo(max) > 0 ? element : max;
			}
		}
		return max;
	}

	public BigDecimal getMin() {
		BigDecimal min = new BigDecimal(Integer.MAX_VALUE);
		for (BigDecimal element : minQueue) {
			if (element != null) {
				min = element.compareTo(min) < 0 ? element : min;
			}
		}
		return min;
	}

}

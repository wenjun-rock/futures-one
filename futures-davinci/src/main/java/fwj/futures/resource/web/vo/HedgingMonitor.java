package fwj.futures.resource.web.vo;

import java.util.Arrays;
import java.util.List;

import fwj.futures.resource.entity.Hedging;

public class HedgingMonitor {
	private Hedging hedging;
	private Object[][] data;

	public HedgingMonitor(Hedging hedging, Object[][] data) {
		this.hedging = hedging;
		this.data = data;
	}

	public List<Series> toSeries() {
		Object[][] upData = new Object[data.length][2];
		Object[][] downData = new Object[data.length][2];
		for (int i = 0; i < data.length; i++) {
			upData[i][0] = data[i][0];
			upData[i][1] = hedging.getUpLimit();
			downData[i][0] = data[i][0];
			downData[i][1] = hedging.getDownLimit();
		}
		return Arrays.asList(new Series("", hedging.getName(), data), new Series("", "上限", upData), new Series("", "下限", downData));
	}
}
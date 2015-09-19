package fwj.futures.resource.web.vo;

import fwj.futures.resource.entity.Futures;

public class PriceLine {
	private Futures prod;
	private Object[][] data;

	public PriceLine(Futures prod, Object[][] data) {
		this.prod = prod;
		this.data = data;
	}

	public void setProd(Futures prod) {
		this.prod = prod;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

	public Series toSeries() {
		return new Series(prod.getCode() + "（" + prod.getName() + "）", data);
	}
}
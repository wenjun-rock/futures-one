package fwj.futures.resource.web.vo;

import fwj.futures.resource.entity.Product;

public class ProductPrice {
	private Product prod;
	private Object[][] data;

	public ProductPrice(Product prod, Object[][] data) {
		this.prod = prod;
		this.data = data;
	}

	public Product getProd() {
		return prod;
	}

	public Object[][] getData() {
		return data;
	}

	public void setProd(Product prod) {
		this.prod = prod;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

	public Series toSeries() {
		return new Series(prod.getCode() + "（" + prod.getName() + "）", data);
	}
}
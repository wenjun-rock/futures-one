package fwj.futures.resource.price.vo;

import java.util.List;

public class Series {

	final public static Series EMPTY = new Series("", "", null);

	private String code;
	private String name;
	private List<Price> prices;

	public Series(String code, String name, List<Price> prices) {
		this.code = code;
		this.name = name;
		this.prices = prices;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public List<Price> getPrices() {
		return prices;
	}

}
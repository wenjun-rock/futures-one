package fwj.futures.resource.price.vo;

import java.util.List;

public class ProdDailyPrice {

	final public static ProdDailyPrice EMPTY = new ProdDailyPrice("", "", null, null);

	private String code;
	private String name;
	private List<Price> mainPriceList;
	private List<Price> indexPriceList;

	public ProdDailyPrice(String code, String name, List<Price> mainPriceList, List<Price> indexPriceList) {
		this.code = code;
		this.name = name;
		this.mainPriceList = mainPriceList;
		this.indexPriceList = indexPriceList;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public List<Price> getMainPriceList() {
		return mainPriceList;
	}

	public List<Price> getIndexPriceList() {
		return indexPriceList;
	}

}
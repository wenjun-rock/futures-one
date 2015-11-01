package fwj.futures.resource.price.vo;

import java.util.List;

public class ProdSpot {

	private String code;
	private String name;
	private List<Price> spotPriceList;
	private List<Price> latestPriceList;
	private List<Price> mainPriceList;
	private List<Price> latestPercList;
	private List<Price> mainPercList;

	public ProdSpot(String code, String name, List<Price> spotPriceList, List<Price> latestPriceList,
			List<Price> mainPriceList, List<Price> latestPercList, List<Price> mainPercList) {
		this.code = code;
		this.name = name;
		this.spotPriceList = spotPriceList;
		this.latestPriceList = latestPriceList;
		this.mainPriceList = mainPriceList;
		this.latestPercList = latestPercList;
		this.mainPercList = mainPercList;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public List<Price> getSpotPriceList() {
		return spotPriceList;
	}

	public List<Price> getLatestPriceList() {
		return latestPriceList;
	}

	public List<Price> getMainPriceList() {
		return mainPriceList;
	}

	public List<Price> getLatestPercList() {
		return latestPercList;
	}

	public List<Price> getMainPercList() {
		return mainPercList;
	}

}
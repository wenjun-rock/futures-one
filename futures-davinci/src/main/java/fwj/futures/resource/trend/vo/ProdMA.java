package fwj.futures.resource.trend.vo;

import java.util.List;

import fwj.futures.resource.web.vo.Series;

public class ProdMA {
	
	private String code;
	private Series prodIndexLine;
	private List<Series> mvAvgLineList;
	
	public ProdMA(String code, Series prodIndexLine, List<Series> mvAvgLineList) {
		this.code = code;
		this.prodIndexLine = prodIndexLine;
		this.mvAvgLineList = mvAvgLineList;
	}

	public String getCode() {
		return code;
	}

	public Series getProdIndexLine() {
		return prodIndexLine;
	}

	public List<Series> getMvAvgLineList() {
		return mvAvgLineList;
	}

}

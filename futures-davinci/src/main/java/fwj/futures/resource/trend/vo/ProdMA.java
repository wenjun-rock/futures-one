package fwj.futures.resource.trend.vo;

import java.util.List;

import fwj.futures.resource.web.vo.Series;

public class ProdMA {
	
	private String code;
	private Series prodIndexLine;
	private Series volLine;
	private List<Series> mvAvgLineList;
	
	public ProdMA(String code, Series prodIndexLine, Series volLine, List<Series> mvAvgLineList) {
		this.code = code;
		this.prodIndexLine = prodIndexLine;
		this.volLine = volLine;
		this.mvAvgLineList = mvAvgLineList;
	}

	public String getCode() {
		return code;
	}

	public Series getProdIndexLine() {
		return prodIndexLine;
	}

	public Series getVolLine() {
		return volLine;
	}

	public List<Series> getMvAvgLineList() {
		return mvAvgLineList;
	}

}

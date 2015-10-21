package fwj.futures.resource.trend.vo;

import java.util.List;

import fwj.futures.resource.web.vo.Series;

public class ProdMA {
	
	private Series prodIndexLine;
	private List<Series> mvAvgLineList;
	
	public ProdMA(Series prodIndexLine, List<Series> mvAvgLineList) {
		this.prodIndexLine = prodIndexLine;
		this.mvAvgLineList = mvAvgLineList;
	}

	public Series getProdIndexLine() {
		return prodIndexLine;
	}

	public List<Series> getMvAvgLineList() {
		return mvAvgLineList;
	}

}

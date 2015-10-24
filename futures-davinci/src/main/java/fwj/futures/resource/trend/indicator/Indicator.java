package fwj.futures.resource.trend.indicator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fwj.futures.resource.price.vo.Price;

public interface Indicator {

	public void push(Date dt, BigDecimal price);
	
	public String getName();

	public List<Price> getLine();

}

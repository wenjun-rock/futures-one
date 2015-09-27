package fwj.futures.resource.web.vo;

import java.math.BigDecimal;
import java.util.Date;

public  class Price implements Comparable<Price> {
	private Date d;
	private BigDecimal p;

	public Price(Date dt, BigDecimal price) {
		d = dt;
		p = price;
	}

	public Date getD() {
		return d;
	}

	public BigDecimal getP() {
		return p;
	}

	@Override
	public int compareTo(Price that) {
		return this.d.compareTo(that.d);
	}
}

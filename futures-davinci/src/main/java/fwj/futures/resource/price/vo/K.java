package fwj.futures.resource.price.vo;

import java.math.BigDecimal;
import java.util.Date;

public interface K extends Comparable<K> {

	public abstract Date getDt();

	public abstract String getName();

	public abstract Integer getTradeVol();

	public abstract BigDecimal getOpenPrice();

	public abstract BigDecimal getEndPrice();

	public abstract BigDecimal getMaxPrice();

	public abstract BigDecimal getMinPrice();

	public default int compareTo(K that) {
		int cmp = this.getDt().compareTo(that.getDt());
		if (cmp == 0) {
			cmp = this.getName().compareTo(that.getName());
		}
		return cmp;
	}
	
}

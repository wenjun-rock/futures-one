package fwj.futures.resource.entity.price;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.jpa.domain.AbstractPersistable;

public abstract class AbstractKLine extends AbstractPersistable<Integer>implements Comparable<AbstractKLine> {

	private static final long serialVersionUID = -5258183004029039965L;

	public abstract String getCode();

	public abstract Integer getTradeVol();

	public abstract BigDecimal getOpenPrice();

	public abstract BigDecimal getEndPrice();

	public abstract BigDecimal getMaxPrice();

	public abstract BigDecimal getMinPrice();

	public abstract void setCode(String code);

	public abstract void setTradeVol(Integer tradeVol);

	public abstract void setOpenPrice(BigDecimal openPrice);

	public abstract void setEndPrice(BigDecimal endPrice);

	public abstract void setMaxPrice(BigDecimal maxPrice);

	public abstract void setMinPrice(BigDecimal minPrice);

	public abstract Date getDt();

	public abstract void setDt(Date dt);

	/*
	 * 按照dt升序
	 */
	@Override
	public int compareTo(AbstractKLine that) {
		int cmp = this.getDt().compareTo(that.getDt());
		if (cmp == 0) {
			cmp = this.getCode().compareTo(that.getCode());
		}
		return cmp;
	}
}

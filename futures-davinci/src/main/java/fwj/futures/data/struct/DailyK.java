package fwj.futures.data.struct;

import java.math.BigDecimal;

public class DailyK implements Comparable<DailyK> {

	private String dt;
	private BigDecimal openPrice;
	private BigDecimal endPrice;
	private BigDecimal maxPrice;
	private BigDecimal minPrice;
	private Long tradeVol;

	public String getDt() {
		return dt;
	}

	public BigDecimal getOpenPrice() {
		return openPrice;
	}

	public BigDecimal getEndPrice() {
		return endPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public Long getTradeVol() {
		return tradeVol;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public void setOpenPrice(BigDecimal openPrice) {
		this.openPrice = openPrice;
	}

	public void setEndPrice(BigDecimal endPrice) {
		this.endPrice = endPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public void setTradeVol(Long tradeVol) {
		this.tradeVol = tradeVol;
	}

	/*
	 * 按照dt升序
	 */
	@Override
	public int compareTo(DailyK that) {
		if (that == null || that.dt == null) {
			return -1;
		} else if (this.dt == null) {
			return 1;
		} else {
			return that.dt.compareTo(this.dt);
		}
	}
}

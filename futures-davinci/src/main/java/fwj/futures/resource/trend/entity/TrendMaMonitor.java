package fwj.futures.resource.trend.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class TrendMaMonitor extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 1267005822885448101L;

	@Column(length = 20)
	private String trend;

	@Column(length = 6)
	private String code;

	@Column(columnDefinition = "DATE")
	private Date calDate;

	@Column(precision = 10, scale = 2)
	private BigDecimal prodPrice;

	@Column(precision = 10, scale = 2)
	private BigDecimal maPrice;

	@Column(precision = 6, scale = 4)
	private BigDecimal diff;

	private Integer trendDays;

	private Integer returnDays;

	public String getTrend() {
		return trend;
	}

	public String getCode() {
		return code;
	}

	public Date getCalDate() {
		return calDate;
	}

	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	public BigDecimal getMaPrice() {
		return maPrice;
	}

	public BigDecimal getDiff() {
		return diff;
	}

	public Integer getTrendDays() {
		return trendDays;
	}

	public Integer getReturnDays() {
		return returnDays;
	}

	public void setTrend(String trend) {
		this.trend = trend;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCalDate(Date calDate) {
		this.calDate = calDate;
	}

	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}

	public void setMaPrice(BigDecimal maPrice) {
		this.maPrice = maPrice;
	}

	public void setDiff(BigDecimal diff) {
		this.diff = diff;
	}

	public void setTrendDays(Integer trendDays) {
		this.trendDays = trendDays;
	}

	public void setReturnDays(Integer returnDays) {
		this.returnDays = returnDays;
	}
}

package fwj.futures.resource.price.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "price_prod_spot", uniqueConstraints = {
		@UniqueConstraint(name = "prod_spot_uni", columnNames = { "dt", "code" }) })
public class ProdSpotPrice extends AbstractPersistable<Integer>implements Comparable<ProdSpotPrice> {

	private static final long serialVersionUID = 5924700652381116598L;

	@Column(columnDefinition = "DATE")
	private Date dt;

	@Column(length = 2)
	private String code;

	@Column(precision = 10, scale = 2)
	private BigDecimal spotPrice;

	@Column(length = 6)
	private String latestCon;

	@Column(precision = 10, scale = 2)
	private BigDecimal latestPrice;

	@Column(precision = 10, scale = 2)
	private BigDecimal latestDiff;

	@Column(precision = 6, scale = 4)
	private BigDecimal latestPerc;

	@Column(length = 6)
	private String mainCon;

	@Column(precision = 10, scale = 2)
	private BigDecimal mainPrice;

	@Column(precision = 10, scale = 2)
	private BigDecimal mainDiff;

	@Column(precision = 6, scale = 4)
	private BigDecimal mainPerc;

	public Date getDt() {
		return dt;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getSpotPrice() {
		return spotPrice;
	}

	public String getLatestCon() {
		return latestCon;
	}

	public BigDecimal getLatestPrice() {
		return latestPrice;
	}

	public BigDecimal getLatestDiff() {
		return latestDiff;
	}

	public BigDecimal getLatestPerc() {
		return latestPerc;
	}

	public String getMainCon() {
		return mainCon;
	}

	public BigDecimal getMainPrice() {
		return mainPrice;
	}

	public BigDecimal getMainDiff() {
		return mainDiff;
	}

	public BigDecimal getMainPerc() {
		return mainPerc;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setSpotPrice(BigDecimal spotPrice) {
		this.spotPrice = spotPrice;
	}

	public void setLatestCon(String latestCon) {
		this.latestCon = latestCon;
	}

	public void setLatestPrice(BigDecimal latestPrice) {
		this.latestPrice = latestPrice;
	}

	public void setLatestDiff(BigDecimal latestDiff) {
		this.latestDiff = latestDiff;
	}

	public void setLatestPerc(BigDecimal latestPerc) {
		this.latestPerc = latestPerc;
	}

	public void setMainCon(String mainCon) {
		this.mainCon = mainCon;
	}

	public void setMainPrice(BigDecimal mainPrice) {
		this.mainPrice = mainPrice;
	}

	public void setMainDiff(BigDecimal mainDiff) {
		this.mainDiff = mainDiff;
	}

	public void setMainPerc(BigDecimal mainPerc) {
		this.mainPerc = mainPerc;
	}

	/*
	 * 按照dt升序
	 */
	@Override
	public int compareTo(ProdSpotPrice that) {
		int cmp = this.dt.compareTo(that.dt);
		if (cmp == 0) {
			cmp = this.code.compareTo(that.code);
		}
		return cmp;
	}
}

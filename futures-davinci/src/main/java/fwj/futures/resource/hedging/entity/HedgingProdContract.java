package fwj.futures.resource.hedging.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
public class HedgingProdContract extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -2307883633602580303L;

	@Column(length = 2)
	private String code1;

	@Column(columnDefinition = "TINYINT")
	private int month1;
	
	@Column(length = 2)
	private String code2;

	@Column(columnDefinition = "TINYINT")
	private int month2;
	
	@Column(columnDefinition = "TINYINT")
	private int rank;
	
	@Column(columnDefinition = "TINYINT")
	private int type;

	@Column(precision = 10, scale = 4)
	private BigDecimal rate;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal curr;

	@Column(precision = 10, scale = 2)
	private BigDecimal max;

	@Column(precision = 10, scale = 2)
	private BigDecimal min;

	@Column(precision = 10, scale = 2)
	private BigDecimal up;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal down;
	
	@Column(columnDefinition = "DATE")
	private Date refreshDt;

	public String getCode1() {
		return code1;
	}

	public int getMonth1() {
		return month1;
	}

	public String getCode2() {
		return code2;
	}

	public int getMonth2() {
		return month2;
	}

	public int getRank() {
		return rank;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public BigDecimal getCurr() {
		return curr;
	}

	public BigDecimal getMax() {
		return max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public BigDecimal getUp() {
		return up;
	}

	public BigDecimal getDown() {
		return down;
	}

	public Date getRefreshDt() {
		return refreshDt;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public void setMonth1(int month1) {
		this.month1 = month1;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public void setMonth2(int month2) {
		this.month2 = month2;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
	public void setCurr(BigDecimal curr) {
		this.curr = curr;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public void setUp(BigDecimal up) {
		this.up = up;
	}

	public void setDown(BigDecimal down) {
		this.down = down;
	}

	public void setRefreshDt(Date refreshDt) {
		this.refreshDt = refreshDt;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}

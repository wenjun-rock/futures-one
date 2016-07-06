package fwj.futures.resource.price.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;

import fwj.futures.resource.price.vo.K;

@Entity
@Table(name = "price_contract_kline", uniqueConstraints = {
		@UniqueConstraint(name = "contract_kline_uni", columnNames = { "dt", "code", "contract_month" }) })
public class ContractKLine extends AbstractPersistable<Integer> implements K {

	private static final long serialVersionUID = 2664970167802802857L;

	@Column(columnDefinition = "DATE")
	private Date dt;

	@Column(name = "contract_month", columnDefinition = "TINYINT")
	private int month;

	@Column(length = 2)
	private String code;

	private Integer tradeVol;

	@Column(precision = 10, scale = 2)
	private BigDecimal openPrice;

	@Column(precision = 10, scale = 2)
	private BigDecimal endPrice;

	@Column(precision = 10, scale = 2)
	private BigDecimal maxPrice;

	@Column(precision = 10, scale = 2)
	private BigDecimal minPrice;

	public String getCode() {
		return code;
	}

	public Integer getTradeVol() {
		return tradeVol;
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

	public void setCode(String code) {
		this.code = code;
	}

	public void setTradeVol(Integer tradeVol) {
		this.tradeVol = tradeVol;
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

	public int getMonth() {
		return month;
	}

	public void setMonth(int contractMonth) {
		this.month = contractMonth;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getContractName() {
		return String.format("%s%02d", code, month);
	}
	
	@Override
	public String getName() {
		return String.format("%s%02d", this.code, this.month);
	}
}

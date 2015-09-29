package fwj.futures.resource.entity.price;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "price_contract_kline", uniqueConstraints = {
		@UniqueConstraint(name = "contract_kline_uni", columnNames = { "dt", "code", "contractMonth" }) })
public class ContractKLine extends AbstractPersistable<Integer>implements Comparable<ContractKLine> {

	private static final long serialVersionUID = 2664970167802802857L;

	@Column(columnDefinition = "DATE")
	private Date dt;

	@Column(columnDefinition = "TINYINT")
	private int contractMonth;

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

	public int getContractMonth() {
		return contractMonth;
	}

	public void setContractMonth(int contractMonth) {
		this.contractMonth = contractMonth;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getContractName() {
		if (contractMonth < 10) {
			return code + "0" + contractMonth;
		} else {
			return code + contractMonth;
		}
	}

	/*
	 * 按照dt升序
	 */
	@Override
	public int compareTo(ContractKLine that) {
		int cmp = this.dt.compareTo(that.dt);
		if (cmp == 0) {
			if (this.contractMonth == that.contractMonth) {
				cmp = 0;
			} else {
				cmp = this.contractMonth > that.contractMonth ? 1 : -1;
			}
		}
		return cmp;
	}
}

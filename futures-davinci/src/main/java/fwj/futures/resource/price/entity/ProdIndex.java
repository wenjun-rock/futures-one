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
@Table(name = "price_prod_index", uniqueConstraints = {
		@UniqueConstraint(name = "price_prod_index_uni", columnNames = { "dt", "code" }) })
public class ProdIndex extends AbstractPersistable<Integer> implements K {

	private static final long serialVersionUID = -7166262390048426516L;

	@Column(columnDefinition = "DATE")
	private Date dt;

	@Column(length = 2)
	private String code;

	@Column(precision = 10, scale = 2)
	private BigDecimal price;
	
	private Integer vol;

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getVol() {
		return vol;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	@Override
	public String getName() {
		return this.code;
	}

	@Override
	public Integer getTradeVol() {
		return this.vol;
	}

	@Override
	public BigDecimal getOpenPrice() {
		return this.price;
	}

	@Override
	public BigDecimal getEndPrice() {
		return this.price;
	}

	@Override
	public BigDecimal getMaxPrice() {
		return this.price;
	}

	@Override
	public BigDecimal getMinPrice() {
		return this.price;
	}

}

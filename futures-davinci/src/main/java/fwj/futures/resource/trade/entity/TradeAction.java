package fwj.futures.resource.trade.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class TradeAction extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -766066123417054685L;

	@ManyToOne()
	@JoinColumn(name = "trade_id", nullable = false)
	private Trade trade;
	
	@Column(columnDefinition = "DATE")
	private Date dt;
	
	@Column(length = 6)
	private String conCode;
	
	@Column(columnDefinition = "TINYINT")
	private int type;
	
	private Integer vol;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal price;
	
	public int getType() {
		return type;
	}

	public Integer getVol() {
		return vol;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public Trade getTrade() {
		return trade;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	public String getConCode() {
		return conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

}

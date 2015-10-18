package fwj.futures.resource.trade.vo;

import java.math.BigDecimal;
import java.util.Date;

public class ActionView {

	private Integer id;
	private Integer tradeId;
	private String conCode;
	private Date dt;
	private int type;
	private BigDecimal price;
	private Integer vol;

	public Integer getId() {
		return id;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public Date getDt() {
		return dt;
	}

	public int getType() {
		return type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getVol() {
		return vol;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	public String getConCode() {
		return conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}


}

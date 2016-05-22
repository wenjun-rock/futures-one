package fwj.futures.resource.trade.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fwj.futures.resource.trade.entity.TradeOrder;

public class TradeGroupView {

	private Integer id;
	private String name;
	private String comment;
	private Date firstTradeDt;
	private Date lastTradeDt;
	private BigDecimal fee;
	private BigDecimal profit;
	private BigDecimal amount;
	private Integer vol;
	private Integer openVol;
	
	private List<TradeOrder> orders;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getFirstTradeDt() {
		return firstTradeDt;
	}

	public void setFirstTradeDt(Date firstTradeDt) {
		this.firstTradeDt = firstTradeDt;
	}

	public Date getLastTradeDt() {
		return lastTradeDt;
	}

	public void setLastTradeDt(Date lastTradeDt) {
		this.lastTradeDt = lastTradeDt;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public List<TradeOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<TradeOrder> orders) {
		this.orders = orders;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getVol() {
		return vol;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	public Integer getOpenVol() {
		return openVol;
	}

	public void setOpenVol(Integer openVol) {
		this.openVol = openVol;
	}

}

package fwj.futures.resource.trade.vo;

import java.math.BigDecimal;

public class BalanceView {

	private Integer id;
	private Integer tradeId;
	private String conCode;
	private int type;
	private Integer vol;
	private BigDecimal avgCostPrice;
	private BigDecimal price;
	private Integer margin;
	private Integer floatProfit;
	private Integer completeProfit;
	private Integer profit;

	public Integer getId() {
		return id;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public String getConCode() {
		return conCode;
	}

	public int getType() {
		return type;
	}

	public Integer getVol() {
		return vol;
	}

	public BigDecimal getAvgCostPrice() {
		return avgCostPrice;
	}

	public Integer getMargin() {
		return margin;
	}

	public Integer getFloatProfit() {
		return floatProfit;
	}

	public Integer getCompleteProfit() {
		return completeProfit;
	}

	public Integer getProfit() {
		return profit;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	public void setAvgCostPrice(BigDecimal avgCostPrice) {
		this.avgCostPrice = avgCostPrice;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public void setFloatProfit(Integer floatProfit) {
		this.floatProfit = floatProfit;
	}

	public void setCompleteProfit(Integer completeProfit) {
		this.completeProfit = completeProfit;
	}

	public void setProfit(Integer profit) {
		this.profit = profit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}

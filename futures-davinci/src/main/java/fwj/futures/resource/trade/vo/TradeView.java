package fwj.futures.resource.trade.vo;

import java.math.BigDecimal;
import java.util.Date;

public class TradeView {

	private Integer id;
	private String name;
	private int type;
	private Date startDt;
	private Date endDt;
	private Integer vol;
	private Integer margin;
	private Integer floatProfit;
	private Integer completeProfit;
	private Integer profit;
	private Integer maxMargin;
	private BigDecimal roi;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public Integer getMaxMargin() {
		return maxMargin;
	}

	public Integer getCompleteProfit() {
		return completeProfit;
	}

	public Integer getFloatProfit() {
		return floatProfit;
	}

	public Integer getProfit() {
		return profit;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMaxMargin(Integer maxMargin) {
		this.maxMargin = maxMargin;
	}

	public void setCompleteProfit(Integer completeProfit) {
		this.completeProfit = completeProfit;
	}

	public void setFloatProfit(Integer floatProfit) {
		this.floatProfit = floatProfit;
	}

	public void setProfit(Integer profit) {
		this.profit = profit;
	}

	public Date getStartDt() {
		return startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public Integer getVol() {
		return vol;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public BigDecimal getRoi() {
		return roi;
	}

	public void setRoi(BigDecimal roi) {
		this.roi = roi;
	}

}

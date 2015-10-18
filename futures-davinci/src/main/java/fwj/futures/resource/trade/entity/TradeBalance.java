package fwj.futures.resource.trade.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class TradeBalance extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -3399930519859984827L;

	@ManyToOne()
	@JoinColumn(name = "trade_id", nullable = false)
	private Trade trade;
	
	@Column(length = 2)
	private String code;
	
	@Column(length = 6)
	private String conCode;
	
	@Column(columnDefinition = "TINYINT")
	private int type;
	
	private Integer vol;
	
	private Integer margin;

	private Integer completeProfit;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal avgCostPrice;
	
	private Integer totalCostValue;
	
	public String getCode() {
		return code;
	}

	public Integer getVol() {
		return vol;
	}

	public BigDecimal getAvgCostPrice() {
		return avgCostPrice;
	}

	public Integer getTotalCostValue() {
		return totalCostValue;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setVol(Integer vol) {
		this.vol = vol;
	}

	public void setAvgCostPrice(BigDecimal avgCostPrice) {
		this.avgCostPrice = avgCostPrice;
	}

	public void setTotalCostValue(Integer totalCostValue) {
		this.totalCostValue = totalCostValue;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getMargin() {
		return margin;
	}

	public Integer getCompleteProfit() {
		return completeProfit;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public void setCompleteProfit(Integer completeProfit) {
		this.completeProfit = completeProfit;
	}

}

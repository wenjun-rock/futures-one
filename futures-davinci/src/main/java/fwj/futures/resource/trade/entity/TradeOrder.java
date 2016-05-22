package fwj.futures.resource.trade.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class TradeOrder extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -7230382409707617510L;

	@Column(length = 6)
	private String conCode;

	@Column(columnDefinition = "DATETIME")
	private Date tradeDt;
	
	@Column(length = 10)
	private String serialNo;
	
	@Column(columnDefinition = "TINYINT")
	private Integer type; // 1:买开;2:卖开;3:卖平;4:买平
	
	@Column(precision = 10, scale = 2)
	private BigDecimal price;
	
	private Integer vol;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal amount;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal fee;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal profit;
	
	@Column(length = 255)
	private String comment;
	
	@ManyToOne()
	@JoinColumn(name = "group_id", nullable = true)
	private TradeGroup tradeGroup;

	public String getConCode() {
		return conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

	public Date getTradeDt() {
		return tradeDt;
	}

	public void setTradeDt(Date tradeDt) {
		this.tradeDt = tradeDt;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TradeGroup getTradeGroup() {
		return tradeGroup;
	}

	public void setTradeGroup(TradeGroup tradeGroup) {
		this.tradeGroup = tradeGroup;
	}

}

package fwj.futures.resource.trade.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;

@Entity
public class TradeOrderView {

	private Integer id;
	private String conCode;
	private Date tradeDt;
	private String serialNo;
	private Integer type; // 1:买开;2:卖开;3:卖平;4:买平
	private BigDecimal price;
	private Integer vol;
	private BigDecimal amount;
	private BigDecimal fee;
	private BigDecimal profit;
	private String comment;
	
	private Integer groupId;
	private String groupName;
	
	public Integer getId() {
		return id;
	}
	public String getConCode() {
		return conCode;
	}
	public Date getTradeDt() {
		return tradeDt;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public Integer getType() {
		return type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public Integer getVol() {
		return vol;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public BigDecimal getProfit() {
		return profit;
	}
	public String getComment() {
		return comment;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setConCode(String conCode) {
		this.conCode = conCode;
	}
	public void setTradeDt(Date tradeDt) {
		this.tradeDt = tradeDt;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public void setVol(Integer vol) {
		this.vol = vol;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	

}

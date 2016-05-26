package fwj.futures.resource.trade.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
	
	private List<Element> elmts;

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

	public List<Element> getElmts() {
		return elmts;
	}

	public void setElmts(List<Element> elmts) {
		this.elmts = elmts;
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
	
	public static class Element {

		private String conCode;
		private int type; // 1:多； 2：空
		private Date openDt;
		private Date closeDt;
		private BigDecimal openPrice;
		private BigDecimal closePrice;
		private BigDecimal diffPrice;

		public String getConCode() {
			return conCode;
		}

		public int getType() {
			return type;
		}

		public Date getOpenDt() {
			return openDt;
		}

		public Date getCloseDt() {
			return closeDt;
		}

		public BigDecimal getOpenPrice() {
			return openPrice;
		}

		public BigDecimal getClosePrice() {
			return closePrice;
		}

		public BigDecimal getDiffPrice() {
			return diffPrice;
		}

		public void setConCode(String conCode) {
			this.conCode = conCode;
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setOpenDt(Date openDt) {
			this.openDt = openDt;
		}

		public void setCloseDt(Date closeDt) {
			this.closeDt = closeDt;
		}

		public void setOpenPrice(BigDecimal openPrice) {
			this.openPrice = openPrice;
		}

		public void setClosePrice(BigDecimal closePrice) {
			this.closePrice = closePrice;
		}

		public void setDiffPrice(BigDecimal diffPrice) {
			this.diffPrice = diffPrice;
		}
	}

}

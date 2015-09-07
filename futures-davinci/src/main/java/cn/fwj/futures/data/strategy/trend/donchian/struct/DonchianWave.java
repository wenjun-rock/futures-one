package cn.fwj.futures.data.strategy.trend.donchian.struct;

import java.math.BigDecimal;

public class DonchianWave {

	private Direction direction;
	private String startDt;
	private String endDt;
	private String maeDt; // 最大衰退日期
	private String mfeDt; // 最大盈利日期
	private BigDecimal enterPrice;
	private BigDecimal exitPrice;
	private BigDecimal maePrice; // 最大衰退价格
	private BigDecimal mfePrice; // 最大盈利价格
	private long tradeVol;

	public DonchianWave(Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getStartDt() {
		return startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public String getMaeDt() {
		return maeDt;
	}

	public String getMfeDt() {
		return mfeDt;
	}

	public BigDecimal getEnterPrice() {
		return enterPrice;
	}

	public BigDecimal getExitPrice() {
		return exitPrice;
	}

	public BigDecimal getMaePrice() {
		return maePrice;
	}

	public BigDecimal getMfePrice() {
		return mfePrice;
	}

	public long getTradeVol() {
		return tradeVol;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public void setMaeDt(String maeDt) {
		this.maeDt = maeDt;
	}

	public void setMfeDt(String mfeDt) {
		this.mfeDt = mfeDt;
	}

	public void setEnterPrice(BigDecimal enterPrice) {
		this.enterPrice = enterPrice;
	}

	public void setExitPrice(BigDecimal exitPrice) {
		this.exitPrice = exitPrice;
	}

	public void setMaePrice(BigDecimal maePrice) {
		this.maePrice = maePrice;
	}

	public void setMfePrice(BigDecimal mfePrice) {
		this.mfePrice = mfePrice;
	}

	public void setTradeVol(long tradeVol) {
		this.tradeVol = tradeVol;
	}

	public enum Direction {
		UP, DOWN;
	}

}

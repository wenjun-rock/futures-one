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
	private boolean ending;

	public DonchianWave(Direction direction, String dt, BigDecimal price) {
		this.direction = direction;
		this.startDt = dt;
		this.endDt = dt;
		this.maeDt = dt;
		this.mfeDt = dt;
		this.enterPrice = price;
		this.exitPrice = price;
		this.maePrice = price;
		this.mfePrice = price;
		ending = false;
	}
	
	public void put(String dt, BigDecimal price) {
		if (this.direction == Direction.UP) {
			// 向上通道
			if (this.mfePrice.compareTo(price) < 0) {
				this.mfeDt = dt;
				this.mfePrice = price;
			}
			if (this.maePrice.compareTo(price) > 0) {
				this.maeDt = dt;
				this.maePrice = price;
			}
		} else if (this.direction == Direction.DOWN) {
			// 向下通道
			if (this.mfePrice.compareTo(price) > 0) {
				this.mfeDt = dt;
				this.mfePrice = price;
			}
			if (this.maePrice.compareTo(price) < 0) {
				this.maeDt = dt;
				this.maePrice = price;
			}
		}
		endDt = dt;
		exitPrice = price;
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

	public boolean isEnding() {
		return ending;
	}

	public void setEnding(boolean ending) {
		this.ending = ending;
	}

	public enum Direction {
		UP, DOWN;
	}

}

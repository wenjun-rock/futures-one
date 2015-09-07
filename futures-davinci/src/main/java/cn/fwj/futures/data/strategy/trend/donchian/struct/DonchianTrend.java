package cn.fwj.futures.data.strategy.trend.donchian.struct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianWave.Direction;
import cn.fwj.futures.data.struct.DailyK;

public class DonchianTrend {

	private Product product; // 产品
	private int enterBreakout;
	private int exitBreakout;
	private FixedSizeQueue enterQueue;
	private FixedSizeQueue exitQueue;

	private String startDt; // 统计开始日期
	private String endDt; // 统计结束日期
	private DonchianWave currentWave;
	private List<DonchianWave> histWave;

	public DonchianTrend(Product product, int enterBreakout, int exitBreakout) {
		this.product = product;
		this.enterBreakout = enterBreakout;
		this.exitBreakout = exitBreakout;
		this.enterQueue = new FixedSizeQueue(enterBreakout);
		this.exitQueue = new FixedSizeQueue(exitBreakout);
		this.histWave = new ArrayList<>();
	}

	public Product getProduct() {
		return product;
	}

	public String getStartDt() {
		return startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public int getEnterBreakout() {
		return enterBreakout;
	}

	public int getExitBreakout() {
		return exitBreakout;
	}

	public FixedSizeQueue getEnterQueue() {
		return enterQueue;
	}

	public FixedSizeQueue getExitQueue() {
		return exitQueue;
	}

	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public DonchianWave getCurrentWave() {
		return currentWave;
	}

	public List<DonchianWave> getHistWave() {
		return histWave;
	}

	public void setCurrentWave(DonchianWave currentWave) {
		this.currentWave = currentWave;
	}

	public void setHistWave(List<DonchianWave> histWave) {
		this.histWave = histWave;
	}

	public void addK(DailyK k) {
		this.enterQueue.enqueue(k);
		this.exitQueue.enqueue(k);
	}

	public void enter(Direction direction, String dt, BigDecimal price) {
		currentWave = new DonchianWave(direction);
		histWave.add(currentWave);
		currentWave.setStartDt(dt);
		currentWave.setMaeDt(dt);
		currentWave.setMfeDt(dt);
		currentWave.setEnterPrice(price);
		currentWave.setMaePrice(price);
		currentWave.setMfePrice(price);
		
	}

	public void exit(String dt, BigDecimal price) {
		currentWave.setEndDt(dt);
		currentWave.setExitPrice(price);
		currentWave = null;	
	}

}

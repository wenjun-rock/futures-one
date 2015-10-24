package fwj.futures.resource.hedging.vo;

import java.util.Date;
import java.util.List;

import fwj.futures.resource.price.vo.Price;

public class HedgingMonitor {

	private Integer id;
	private String name;
	private String formula;
	private Date startDt;
	private Date endDt;
	private int down;
	private int up;
	private List<Price> realtimePrices;
	private List<Price> klinePrices;

	public HedgingMonitor(Integer id, String name, String formula, Date startDt, Date endDt, int down, int up,
			List<Price> realtimePrices, List<Price> klinePrices) {
		this.id = id;
		this.name = name;
		this.formula = formula;
		this.startDt = startDt;
		this.endDt = endDt;
		this.down = down;
		this.up = up;
		this.realtimePrices = realtimePrices;
		this.klinePrices = klinePrices;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFormula() {
		return formula;
	}

	public Date getStartDt() {
		return startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public int getDown() {
		return down;
	}

	public int getUp() {
		return up;
	}

	public List<Price> getRealtimePrices() {
		return realtimePrices;
	}

	public List<Price> getKlinePrices() {
		return klinePrices;
	}
}

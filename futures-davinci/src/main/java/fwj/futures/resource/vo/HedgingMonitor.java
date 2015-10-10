package fwj.futures.resource.vo;

import java.util.List;

import fwj.futures.resource.web.vo.Price;

public class HedgingMonitor {

	private Integer id;
	private String name;
	private String formula;
	private int down;
	private int up;
	private List<Price> realtimePrices;
	private List<Price> klinePrices;

	public HedgingMonitor(Integer id, String name, String formula,
			int down, int up, List<Price> realtimePrices, List<Price> klinePrices) {
		this.id = id;
		this.name = name;
		this.formula = formula;
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

package fwj.futures.resource.web.vo;

import java.util.List;

import fwj.futures.resource.entity.hedging.Hedging;

public class HedgingMonitor {
	private Hedging hedging;
	private List<Price> prices;

	public HedgingMonitor(Hedging hedging, List<Price> prices) {
		this.hedging = hedging;
		this.prices = prices;
	}

	public Hedging getHedging() {
		return hedging;
	}

	public List<Price> getPrices() {
		return prices;
	}
}
package fwj.futures.data.struct;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fwj.futures.data.enu.ProdEnum;

public class DailyKLine {

	private ProdEnum prod;
	private Map<String, DailyK> prices;

	public DailyKLine(ProdEnum prod) {
		this.prod = prod;
		this.prices = new TreeMap<>();
	}

	public ProdEnum getProd() {
		return prod;
	}

	public DailyK getDailyK(String dt) {
		return this.prices.get(dt);
	}

	public void setDailyK(String dt, DailyK k) {
		this.prices.put(dt, k);
	}

	public Collection<DailyK> getAllDailyK() {
		return this.prices.values();
	}

	public XYLine createEndPriceXYLine() {
		return createEndPriceXYLine(0);
	}

	public XYLine createEndPriceXYLine(int range) {
		Map<String, BigDecimal> xy = new HashMap<>();
		for (Map.Entry<String, DailyK> entry : prices.entrySet()) {
			xy.put(entry.getKey(), entry.getValue().getEndPrice());
		}
		return new XYLine(prod, xy, range);
	}

}

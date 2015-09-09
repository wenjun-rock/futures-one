package fwj.futures.data.struct;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fwj.futures.data.enu.Product;

public class DailyKLine {

	private Product prod;
	private Map<String, DailyK> prices;

	public DailyKLine(Product prod) {
		this.prod = prod;
		this.prices = new TreeMap<>();
	}

	public Product getProd() {
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

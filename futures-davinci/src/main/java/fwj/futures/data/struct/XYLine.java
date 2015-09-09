package fwj.futures.data.struct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fwj.futures.data.enu.Product;

public class XYLine {

	private int range;
	private Product prod;
	private Map<String, BigDecimal> xy;

	public XYLine(Product prod, Map<String, BigDecimal> xy, int range) {
		this.prod = prod;
		this.xy = xy;
		this.range = range;
	}

	public Product getProd() {
		return prod;
	}

	public Map<String, BigDecimal> getXy() {
		return xy;
	}

	public int getRange() {
		return range;
	}

	public Map<String, BigDecimal> nomalize(String startDt, String endDt) throws Exception {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse(startDt);
		Date end = df.parse(endDt);

		Map<String, BigDecimal> sel = new HashMap<>();
		BigDecimal min = new BigDecimal(Integer.MAX_VALUE);
		BigDecimal max = new BigDecimal(Integer.MIN_VALUE);
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		while (!cal.getTime().after(end)) {
			String dt = df.format(cal.getTime());
			BigDecimal value = xy.get(dt);
			if (value != null) {
				sel.put(dt, value);
				max = value.compareTo(max) > 0 ? value : max;
				min = value.compareTo(min) < 0 ? value : min;
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		if (range > 0 && !sel.isEmpty()) {
			BigDecimal diff = max.subtract(min);
			BigDecimal rangeB = new BigDecimal(range);
			Map<String, BigDecimal> normal = new HashMap<>();
			for (Map.Entry<String, BigDecimal> entry : sel.entrySet()) {
				String key = entry.getKey();
				BigDecimal val = entry.getValue().subtract(min).multiply(rangeB).divide(diff, 0,
						RoundingMode.HALF_EVEN);
				normal.put(key, val);
			}
			return normal;
		} else {
			return sel;
		}

	}

}

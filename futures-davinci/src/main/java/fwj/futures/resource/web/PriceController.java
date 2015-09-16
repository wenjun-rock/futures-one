package fwj.futures.resource.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.data.struct.Formula;
import fwj.futures.data.struct.Formula.Multinomial;
import fwj.futures.resource.entity.Hedging;
import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.HedgingRepository;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.ProductRepository;
import fwj.futures.task.RealtimeHolder;
import fwj.futures.task.RealtimeHolder.UnitData;
import fwj.futures.task.RealtimeHolder.UnitDataGroup;

@RestController()
@RequestMapping("/price")
public class PriceController {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private HedgingRepository hedgingRepo;

	@Autowired
	private RealtimeHolder realtimeHolder;

	@RequestMapping("/daily/{codes}")
	public List<Series> findDailyByCodes(@PathVariable("codes") String codes) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return Stream.of(codes.split(",")).map(code -> {
			Product prod = productRepo.findByCode(code);
			if (prod == null) {
				return Series.EMPTY;
			} else {
				List<KLine> kLineList = kLineRepo.findByCode(code);
				Object[][] data = new Object[kLineList.size()][2];
				for (int i = 0; i < kLineList.size(); i++) {
					KLine kline = kLineList.get(i);
					try {
						data[i][0] = df.parse(kline.getDt()).getTime();
						data[i][1] = kline.getEndPrice();
					} catch (Exception e) {
						log.error("", e);
					}
				}
				return new Price(prod, data).toSeries();
			}
		}).collect(Collectors.toList());
	}

	@RequestMapping("/realtime/{codes}")
	public List<Series> findRealtimeByCodes(@PathVariable("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> {
			Product prod = productRepo.findByCode(code);
			if (prod == null) {
				return Series.EMPTY;
			} else {
				return realtimeHolder.findRealtimeByCode(prod).toSeries();
			}
		}).collect(Collectors.toList());
	}

	@RequestMapping("/monitor/{id}")
	public List<Series> monitorRealtimeHedging(@PathVariable("id") Integer id) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		Hedging hedging = hedgingRepo.findOne(id);
		Formula fomular = Formula.parse(hedging.getExpression());
		List<UnitDataGroup> unitDataGroupList = realtimeHolder.getRealtime();
		Object[][] data = new Object[unitDataGroupList.size()][2];
		int j = 0;
		for (int i = 0; i < unitDataGroupList.size(); i++) {
			UnitDataGroup unitDataGroup = unitDataGroupList.get(i);
			try {
				Map<String, BigDecimal> map = new HashMap<>();
				for (UnitData unitData : unitDataGroup.getUnitDataList()) {
					map.put(unitData.getCode(), unitData.getPrice());
				}
				boolean miss = false;
				BigDecimal result = fomular.getConstant();
				for (Multinomial multinomial : fomular.getMultinomials()) {
					if (map.get(multinomial.getCode()) == null) {
						miss = true;
						break;
					} else {
						result = result.add(multinomial.getCoefficient().multiply(map.get(multinomial.getCode())));
					}
				}
				if (!miss) {
					data[j][0] = df.parse(unitDataGroup.getDatetime());
					data[j][1] = result;
					j++;
				}
			} catch (ParseException e) {
				log.error("", e);
			}
		}
		return new Monitor(hedging.getUpLimit(), hedging.getDownLimit(), Arrays.copyOf(data, j)).toSeries();
	}

	public static class Monitor {
		private BigDecimal upLimit;
		private BigDecimal downLimit;
		private Object[][] data;

		public Monitor(BigDecimal upLimit, BigDecimal downLimit, Object[][] data) {
			this.upLimit = upLimit;
			this.downLimit = downLimit;
			this.data = data;
		}

		public List<Series> toSeries() {
			Object[][] upData = new Object[data.length][2];
			Object[][] downData = new Object[data.length][2];
			for (int i = 0; i < data.length; i++) {
				upData[i][0] = data[i][0];
				upData[i][1] = upLimit;
				downData[i][0] = data[i][0];
				downData[i][1] = downLimit;
			}
			return Arrays.asList(new Series("偏离值", data), new Series("上限", upData), new Series("下限", downData));
		}
	}

	public static class Price {
		private Product prod;
		private Object[][] data;

		public Price(Product prod, Object[][] data) {
			this.prod = prod;
			this.data = data;
		}

		public Product getProd() {
			return prod;
		}

		public Object[][] getData() {
			return data;
		}

		public void setProd(Product prod) {
			this.prod = prod;
		}

		public void setData(Object[][] data) {
			this.data = data;
		}

		public Series toSeries() {
			return new Series(prod.getCode() + "（" + prod.getName() + "）", data);
		}
	}

	public static class Series {

		private static Series EMPTY = new Series("", new Object[0][2]);

		private String name;
		private Object[][] data;

		public Series(String name, Object[][] data) {
			this.name = name;
			this.data = data;
		}

		public String getName() {
			return name;
		}

		public Object[][] getData() {
			return data;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setData(Object[][] data) {
			this.data = data;
		}
	}

}

package fwj.futures.task;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Resources;

import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.ProductRepository;
import fwj.futures.resource.web.PriceController.Price;

@Component
public class RealtimeHolder {

	private Logger log = Logger.getLogger(this.getClass());

	private final static String URI_5M = "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine5m?symbol=%s0";
	private final static String URI_RT = "http://hq.sinajs.cn/list=%s";

	@Autowired
	private ProductRepository productRepo;

	private int index = 0;
	private int rest = 0;
	private boolean first = true;
	private boolean running = false;
	private UnitDataGroup[] loopCache = null;

	/**
	 * 间隔1分钟调度
	 */
	@Scheduled(cron = "0 */1 * * * ?")
	public void refresh() {
		if (--rest > 0 || running) {
			log.info("skip refresh!");
			return;
		}
		try {
			running = true;
			if (first) {
				init();
			} else {
				update();
			}
		} catch (Exception e) {
			log.error("Wrong!", e);
		} finally {
			running = false;
		}
	}

	private boolean diff(UnitDataGroup current, UnitDataGroup last) {
		List<UnitData> currentList = current.getUnitDataList();
		List<UnitData> lastList = last.getUnitDataList();
		if (currentList.size() != lastList.size()) {
			return true;
		}
		for (int i = 0; i < currentList.size(); i++) {
			if (currentList.get(i).getPrice().equals(lastList.get(i).getPrice())) {
				return true;
			}
		}
		return false;
	}

	private void update() throws Exception {
		String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		List<String> lines = new ArrayList<>();
		List<Product> prodList = productRepo.findAllActive();
		int from = 0;
		while(from < prodList.size()) {
			int to = Math.min(from + 10, prodList.size());
			String params = prodList.subList(from, to).stream().map(prod -> prod.getCode() + "0").reduce((l, r) -> l + "," + r).get();
			try {
				lines.addAll(Resources.readLines(new URL(String.format(URI_RT, params)), StandardCharsets.UTF_8));
			} catch (Exception ex) {
				log.error("error when access " + String.format(URI_RT, params));
				rest = 5;
				return;
			}
			from +=10;
		}

		List<UnitData> list = new ArrayList<>();
		for (int i = 0; i < prodList.size(); i++) {
			String line = lines.get(i);
			if (line.indexOf(",") > -1) {
				BigDecimal price = new BigDecimal(line.split(",")[8]);
				String code = prodList.get(i).getCode();
				list.add(new UnitData(datetime, code, price));
			}
		}
		UnitDataGroup current = new UnitDataGroup(datetime, list);
		int lastIndex = (loopCache.length + index - 1) % loopCache.length;
		if (diff(current, loopCache[lastIndex])) {
			loopCache[index] = current;
			index++;
			log.info("change!");
		} else {
			rest = 5;
			log.info("not change!");
		}
	}

	private void init() throws Exception {
		List<UnitData> list = new ArrayList<>();
		for (Product prod : productRepo.findAllActive()) {
			String result = Resources.toString(new URL(String.format(URI_5M, prod.getCode())), StandardCharsets.UTF_8);
			JSONArray dailyKs = JSON.parseArray(result);
			if (dailyKs == null) {
				continue;
			}
			for (int i = 0; i < dailyKs.size(); i++) {
				JSONArray ele = dailyKs.getJSONArray(i);
				list.add(new UnitData(ele.getString(0), prod.getCode(), ele.getBigDecimal(4)));
			}
		}

		List<UnitDataGroup> resultList = list.stream()
				.collect(Collectors.groupingBy(UnitData::getDatetime, Collectors.toList())).entrySet().stream()
				.map(entry -> new UnitDataGroup(entry.getKey(), entry.getValue())).collect(Collectors.toList());
		Collections.sort(resultList);
		int toIndex = resultList.size();
		int fromIndex = Math.max(0, resultList.size() - 480);
		resultList = resultList.subList(fromIndex, toIndex);
		loopCache = new UnitDataGroup[resultList.size()];
		for (int i = 0; i < resultList.size(); i++) {
			loopCache[i] = resultList.get(i);
		}
		log.info("init loopCache with the size of " + resultList.size());
		first = false;
	}

	// public void addListener(Listener lis) {
	//
	// }

	public class UnitData implements Comparable<UnitData> {

		private String datetime;
		private String code;
		private BigDecimal price;

		public UnitData(String datetime, String code, BigDecimal price) {
			this.datetime = datetime;
			this.code = code;
			this.price = price;
		}

		public String getDatetime() {
			return datetime;
		}

		public String getCode() {
			return code;
		}

		public BigDecimal getPrice() {
			return price;
		}

		@Override
		public int compareTo(UnitData that) {
			int cp = this.datetime.compareTo(that.datetime);
			if (cp == 0) {
				cp = this.code.compareTo(that.code);
			}
			return cp;
		}

	}

	public class UnitDataGroup implements Comparable<UnitDataGroup> {

		private String datetime;
		private List<UnitData> unitDataList;

		public UnitDataGroup(String datetime, List<UnitData> unitDataList) {
			Collections.sort(unitDataList);
			this.datetime = datetime;
			this.unitDataList = unitDataList;
		}

		public String getDatetime() {
			return datetime;
		}

		public List<UnitData> getUnitDataList() {
			return unitDataList;
		}

		@Override
		public int compareTo(UnitDataGroup that) {
			return this.datetime.compareTo(that.datetime);
		}

	}

	public Price findRealtimeByCode(Product prod) {
		if (loopCache == null) {
			return new Price(prod, new Object[0][2]);
		}
		UnitDataGroup[] copy = Arrays.copyOf(loopCache, loopCache.length);
		List<UnitData> unitDataList = Stream.of(copy).flatMap(unitDataGroup -> unitDataGroup.getUnitDataList().stream())
				.filter(unitData -> unitData.getCode().equals(prod.getCode())).collect(Collectors.toList());
		Collections.sort(unitDataList);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Object[][] data = new Object[unitDataList.size()][2];
		for (int i = 0; i < data.length; i++) {
			try {
				data[i][0] = df.parse(unitDataList.get(i).getDatetime());
				data[i][1] = unitDataList.get(i).getPrice();
			} catch (ParseException e) {
				log.error("", e);
			}
		}
		return new Price(prod, data);
	}

	public List<UnitDataGroup> getRealtime() {
		if (loopCache == null) {
			return Collections.emptyList();
		}

		UnitDataGroup[] copy = Arrays.copyOf(loopCache, loopCache.length);
		List<UnitDataGroup> list = Arrays.asList(copy);
		Collections.sort(list);
		return list;
	}
}

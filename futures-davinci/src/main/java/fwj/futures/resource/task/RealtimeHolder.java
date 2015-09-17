package fwj.futures.resource.task;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Resources;

import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.ProductRepository;

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
			log.info(String.format("change! the product size from %s to %s", lastList.size(), currentList.size()));
			return true;
		}
		for (int i = 0; i < currentList.size(); i++) {
			if (!currentList.get(i).getPrice().equals(lastList.get(i).getPrice())) {
				log.info(String.format("change! %s %s changed to %s %s", lastList.get(i).getCode(),
						lastList.get(i).getPrice(), currentList.get(i).getCode(), currentList.get(i).getPrice()));
				return true;
			}
		}
		log.info("not change!");
		return false;
	}

	private void update() throws Exception {
		String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		List<String> lines = new ArrayList<>();
		List<Product> prodList = productRepo.findAllActive();
		int from = 0;
		while (from < prodList.size()) {
			int to = Math.min(from + 10, prodList.size());
			String params = prodList.subList(from, to).stream().map(prod -> prod.getCode() + "0")
					.reduce((l, r) -> l + "," + r).get();
			try {
				lines.addAll(Resources.readLines(new URL(String.format(URI_RT, params)), StandardCharsets.UTF_8));
			} catch (Exception ex) {
				log.error("error when access " + String.format(URI_RT, params));
				rest = 10;
				return;
			}
			from += 10;
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
		} else {
			rest = 5;
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

		public UnitData getUnitData(String code) {
			if (unitDataList == null) {
				return null;
			}
			return unitDataList.stream().filter(unitData -> unitData.getCode().equals(code)).findAny().orElse(null);
		}

		@Override
		public int compareTo(UnitDataGroup that) {
			return this.datetime.compareTo(that.datetime);
		}

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

package fwj.futures.resource.task;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
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
import com.google.common.io.Files;
import com.google.common.io.Resources;

import fwj.futures.resource.buss.ProductBuss;
import fwj.futures.resource.entity.Futures;
import fwj.futures.resource.vo.UnitData;
import fwj.futures.resource.vo.UnitDataGroup;

@Component
public class RealtimeHolder {

	private Logger log = Logger.getLogger(this.getClass());

	private final static String URI_5M = "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine5m?symbol=%s0";
	private final static String URI_RT = "http://hq.sinajs.cn/list=%s";
	private final static String BAK_PATH = "/home/fwj/bak/davinci-realtime";
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private ProductBuss productBuss;

	private int index = -1;
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
		Date datetime = new Date();

		List<String> lines = new ArrayList<>();
		List<Futures> prodList = productBuss.queryAllFutures();
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
		if (diff(current, loopCache[index])) {
			int nextIndex = (index + 1) % loopCache.length;
			loopCache[nextIndex] = current;
			index = nextIndex;
		} else {
			rest = 5;
		}
	}

	private void init() throws Exception {
		// List<UnitDataGroup> resultList = loadRealtime();
		List<UnitDataGroup> resultList = Collections.emptyList();
		log.info(resultList.size() + " UnitDataGroup was loaded from bak.");
		if (resultList.isEmpty()) {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			List<UnitData> list = new ArrayList<>();
			for (Futures futures : productBuss.queryAllFutures()) {
				String result = Resources.toString(new URL(String.format(URI_5M, futures.getCode())),
						StandardCharsets.UTF_8);
				JSONArray dailyKs = JSON.parseArray(result);
				if (dailyKs == null) {
					continue;
				}
				for (int i = 0; i < dailyKs.size(); i++) {
					JSONArray ele = dailyKs.getJSONArray(i);
					list.add(new UnitData(df.parse(ele.getString(0)), futures.getCode(), ele.getBigDecimal(4)));
				}
			}

			resultList = list.stream().collect(Collectors.groupingBy(UnitData::getDatetime, Collectors.toList()))
					.entrySet().stream().map(entry -> new UnitDataGroup(entry.getKey(), entry.getValue())).sorted()
					.collect(Collectors.toList());
			int toIndex = resultList.size();
			int fromIndex = Math.max(0, resultList.size() - 600);
			resultList = resultList.subList(fromIndex, toIndex);
		}

		UnitDataGroup[] temp = new UnitDataGroup[resultList.size()];
		for (int i = 0; i < resultList.size(); i++) {
			temp[i] = resultList.get(i);
		}
		loopCache = temp;
		index = loopCache.length - 1;
		log.info("init loopCache with the size of " + loopCache.length);
		first = false;
	}

	/**
	 * 每1小时保存一次Realtime
	 */
	@Scheduled(cron = "15 15 */1 * * ?")
	public void bakRealtime() {
		try {
			String data = JSON.toJSONString(getRealtime());
			Files.asCharSink(new File(BAK_PATH), StandardCharsets.UTF_8).write(data);
			log.info("success to save realtime data to " + BAK_PATH);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public List<UnitDataGroup> loadRealtime() {
		try {
			File bak = new File(BAK_PATH);
			if (bak.exists()) {
				String data = Files.asCharSource(bak, StandardCharsets.UTF_8).read();
				return JSON.parseArray(data, UnitDataGroup.class);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return Collections.emptyList();
	}

	public List<UnitDataGroup> getRealtime() {
		if (loopCache == null || index == -1) {
			return Collections.emptyList();
		}
		UnitDataGroup[] copy = Arrays.copyOf(loopCache, loopCache.length);
		List<UnitDataGroup> list = Arrays.asList(copy);
		Collections.sort(list);
		return list;
	}

	public UnitDataGroup getLatest() {
		return loopCache[index];
	}
}

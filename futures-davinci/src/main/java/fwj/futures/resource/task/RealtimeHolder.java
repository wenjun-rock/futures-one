package fwj.futures.resource.task;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import fwj.futures.resource.buss.ProductBuss;
import fwj.futures.resource.entity.price.RealtimeStore;
import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.entity.prod.FuturesTradeTime;
import fwj.futures.resource.repository.price.RealtimeRepository;
import fwj.futures.resource.vo.UnitData;
import fwj.futures.resource.vo.UnitDataGroup;

@Component
public class RealtimeHolder {

	private Logger log = Logger.getLogger(this.getClass());

	// private final static String URI_5M =
	// "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine5m?symbol=%s0";
	private final static String URI_RT = "http://hq.sinajs.cn/list=%s";
	private final static String BAK_PATH = "/home/fwj/bak/davinci-realtime";
	// private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private ProductBuss productBuss;

	@Autowired
	private RealtimeRepository realtimeRepository;

	private int tick = -1;
	private boolean first = true;
	private boolean running = false;
	private UnitDataGroup[] loopCache = new UnitDataGroup[900];

	/**
	 * 间隔1分钟调度
	 */
	@Scheduled(cron = "0 */1 * * * ?")
	public void refresh() {
		if (running) {
			log.info("Busy!");
			return;
		}
		try {
			running = true;
			if (first) {
				init();
			}
			update();
		} catch (Exception e) {
			log.error("Wrong!", e);
		} finally {
			running = false;
		}
	}

	private void update() throws Exception {

		List<String> codeList = this.getTradingCodes();
		if (codeList.isEmpty()) {
			log.info("colsed!");
		} else {
			long mills = System.currentTimeMillis();
			mills = (mills / 1000) * 1000;
			Date datetime = new Date(mills);

			List<String> lines = new ArrayList<>();
			int from = 0;
			while (from < codeList.size()) {
				int to = Math.min(from + 10, codeList.size());
				String params = codeList.subList(from, to).stream().map(code -> code + "0")
						.reduce((l, r) -> l + "," + r).get();
				try {
					lines.addAll(Resources.readLines(new URL(String.format(URI_RT, params)), StandardCharsets.UTF_8));
				} catch (Exception ex) {
					log.error("error when access " + String.format(URI_RT, params));
					return;
				}
				from += 10;
			}

			Map<String, UnitData> map = new HashMap<>();
			for (int i = 0; i < codeList.size(); i++) {
				String line = lines.get(i);
				int beg = line.indexOf("\"");
				int end = line.lastIndexOf("\"");
				if (end - beg > 1) {
					String data = line.substring(beg + 1, end);
					BigDecimal price = new BigDecimal(data.split(",")[8]);
					String code = codeList.get(i);
					map.put(code, new UnitData(datetime, code, price));
					RealtimeStore rt = new RealtimeStore();
					rt.setPriceTime(datetime);
					rt.setCode(code);
					rt.setData(data);
					realtimeRepository.save(rt);
				}
			}
			realtimeRepository.flush();
			UnitDataGroup current = new UnitDataGroup(datetime, map);
			int nextIndex = (tick + 1) % loopCache.length;
			loopCache[nextIndex] = current;
			tick++;
		}

	}

	private List<String> getTradingCodes() {
		String time = new SimpleDateFormat("HHmm").format(new Date());
		return productBuss.queryAllTradeTimes().stream().filter(tradeTime -> {
			return tradeTime.getStartTime().compareTo(time) <= 0 && tradeTime.getEndTime().compareTo(time) >= 0;
		}).map(FuturesTradeTime::getCode).collect(Collectors.toList());
	}

	private void init() throws Exception {
		first = false;
		List<RealtimeStore> storeList = realtimeRepository.findTop20000OrderByPriceTimeDesc();
		Map<Date, Map<String, UnitData>> map = storeList.stream()
				.map(store -> new UnitData(store.getPriceTime(), store.getCode(),
						new BigDecimal(store.getData().split(",")[8])))
				.collect(Collectors.groupingBy(UnitData::getDatetime, Collectors.toMap(UnitData::getCode, o -> o)));
		List<UnitDataGroup> groupList = map.entrySet().stream().map(entry -> new UnitDataGroup(entry.getKey(), entry.getValue())).sorted()
				.collect(Collectors.toList());

		List<UnitDataGroup> resultList = Collections.emptyList();
		log.info(resultList.size() + " UnitDataGroup was loaded from bak.");
		if (resultList.isEmpty()) {
			List<String> codes = productBuss.queryAllFutures().stream().map(Futures::getCode)
					.collect(Collectors.toList());
			update(codes);
		} else {
			for (int i = 0; i < resultList.size(); i++) {
				loopCache[i] = resultList.get(i);
			}
			tick = resultList.size() - 1;
			update();
		}
	}

	private void update(List<String> codeList) {
		long mills = System.currentTimeMillis();
		mills = (mills / 1000) * 1000;
		Date datetime = new Date(mills);

		List<String> lines = new ArrayList<>();
		int from = 0;
		while (from < codeList.size()) {
			int to = Math.min(from + 10, codeList.size());
			String params = codeList.subList(from, to).stream().map(code -> code + "0").reduce((l, r) -> l + "," + r)
					.get();
			try {
				lines.addAll(Resources.readLines(new URL(String.format(URI_RT, params)), StandardCharsets.UTF_8));
			} catch (Exception ex) {
				log.error("error when access " + String.format(URI_RT, params));
				return;
			}
			from += 10;
		}

		Map<String, UnitData> map = new HashMap<>();
		for (int i = 0; i < codeList.size(); i++) {
			String line = lines.get(i);
			if (line.indexOf(",") > -1) {
				BigDecimal price = new BigDecimal(line.split(",")[8]);
				String code = codeList.get(i);
				map.put(code, new UnitData(datetime, code, price));
			}
		}
		UnitDataGroup current = new UnitDataGroup(datetime, map);
		int nextIndex = (tick + 1) % loopCache.length;
		loopCache[nextIndex] = current;
		tick++;
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
		if (tick < 0) {
			return Collections.emptyList();
		} else if (tick + 1 < loopCache.length) {
			return Arrays.asList(Arrays.copyOf(loopCache, tick + 1));
		} else {
			UnitDataGroup[] copy = Arrays.copyOf(loopCache, loopCache.length);
			List<UnitDataGroup> list = Arrays.asList(copy);
			Collections.sort(list);
			return list;
		}
	}

	public UnitDataGroup getLatest() {
		if (tick < 0) {
			return null;
		} else {
			return loopCache[tick % loopCache.length];
		}
	}
}

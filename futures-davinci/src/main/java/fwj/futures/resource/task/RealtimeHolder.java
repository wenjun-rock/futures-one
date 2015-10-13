package fwj.futures.resource.task;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.io.Resources;

import fwj.futures.resource.buss.HolidayBuss;
import fwj.futures.resource.buss.ProductBuss;
import fwj.futures.resource.entity.price.RealtimeStore;
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
	private final static int CACHE_SIZE = 900;

	@Autowired
	private ProductBuss productBuss;

	@Autowired
	private HolidayBuss holidayBuss;

	@Autowired
	private RealtimeRepository realtimeRepository;

	private int tick = -1;
	private boolean first = true;
	private boolean running = false;
	private UnitDataGroup[] loopCache = new UnitDataGroup[CACHE_SIZE];

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
		if (onHoliday()) {
			log.info("on holiday!");
			return;
		} else if (inWeekend()) {
			log.info("in the weekend!");
			return;
		}

		List<String> codeList = this.getTradingCodes();
		if (codeList.isEmpty()) {
			log.info("in the rest!");
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
					lines.addAll(Resources.readLines(new URL(String.format(URI_RT, params)), Charset.forName("GBK")));
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
					if (price.compareTo(BigDecimal.ZERO) > 0) {
						map.put(code, new UnitData(datetime, code, price));
					}
					RealtimeStore rt = new RealtimeStore();
					rt.setPriceTime(datetime);
					rt.setCode(code);
					rt.setData(data);
//					realtimeRepository.save(rt);
				}
			}
//			realtimeRepository.flush();
			UnitDataGroup current = new UnitDataGroup(datetime, map);
			int nextIndex = (tick + 1) % loopCache.length;
			loopCache[nextIndex] = current;
			tick++;
			log.info(String.format("%6d | get %s codes (%s)", tick, map.size(), String.join(",", map.keySet())));
		}

	}

	private List<String> getTradingCodes() {
		DateFormat df = new SimpleDateFormat("HHmm");
		Calendar cal = Calendar.getInstance();
		String now = df.format(cal.getTime());
		cal.add(Calendar.MINUTE, -1); // 闭市延后1秒
		String prev = df.format(cal.getTime());
		return productBuss.queryAllTradeTimes().stream().filter(tradeTime -> {
			String startTime = tradeTime.getStartTime();
			String endTime = tradeTime.getEndTime();
			if (startTime.compareTo(endTime) < 0) {
				// 未跨日
				return startTime.compareTo(now) <= 0 && endTime.compareTo(prev) >= 0;
			} else {
				// 跨日
				return startTime.compareTo(now) <= 0 || endTime.compareTo(now) >= 0 || endTime.compareTo(prev) >= 0;
			}

		}).map(FuturesTradeTime::getCode).collect(Collectors.toList());
	}

	private boolean onHoliday() {
		Date now = new Date();
		return holidayBuss.queryAll().stream().filter(holiday -> {
			return now.compareTo(holiday.getActualStartTime()) >= 0 && now.compareTo(holiday.getActualEndTime()) < 0;
		}).findAny().isPresent();
	}

	private boolean inWeekend() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -9);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
	}

	private void init() throws Exception {
		first = false;
		// List<RealtimeStore> storeList =
		// realtimeRepository.findTop20000OrderByPriceTimeDesc();
		List<RealtimeStore> storeList = realtimeRepository
				.findAll(new PageRequest(0, 20000, Direction.DESC, "priceTime")).getContent();

		Map<Date, Map<String, UnitData>> map = storeList.stream()
				.map(store -> new UnitData(store.getPriceTime(), store.getCode(),
						new BigDecimal(store.getData().split(",")[8])))
				.filter(unitData -> unitData.getPrice().compareTo(BigDecimal.ZERO) > 0)
				.collect(Collectors.groupingBy(UnitData::getDatetime, Collectors.toMap(UnitData::getCode, o -> o)));
		List<UnitDataGroup> groupList = map.entrySet().stream()
				.map(entry -> new UnitDataGroup(entry.getKey(), entry.getValue())).sorted()
				.collect(Collectors.toList());
		if (groupList.size() > CACHE_SIZE) {
			int to = groupList.size();
			int from = groupList.size() - CACHE_SIZE;
			groupList = groupList.subList(from, to);
		}
		for (int i = 0; i < groupList.size(); i++) {
			loopCache[i] = groupList.get(i);
		}
		tick = groupList.size() - 1;
		log.info(groupList.size() + " UnitDataGroup was loaded.");
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

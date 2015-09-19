package fwj.futures.resource.task;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Resources;

import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Futures;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.FuturesRepository;

@Component
public class KLineRefresher {

	private Logger log = Logger.getLogger(this.getClass());

	private static final String URI_DAILY = "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=%s0";

	@Autowired
	private KLineRepository kLineRepository;

	@Autowired
	private FuturesRepository futuresRepository;

	/**
	 * 每天6时调度。
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void doTask() {
		for (Futures prod : futuresRepository.findAllActive()) {
			log.info("Downloading " + prod.getCode());

			KLine latest = kLineRepository.findTopByCodeOrderByDtDesc(prod.getCode());
			String jsonStr = null;
			try {
				jsonStr = Resources.toString(new URL(String.format(URI_DAILY, prod.getCode())), StandardCharsets.UTF_8);
			} catch (Exception e) {
				log.trace("Bad boy " + prod.getCode(), e);
				continue;
			}
			JSONArray dailyKs = JSON.parseArray(jsonStr);
			if (dailyKs == null) {
				log.info("Can't get " + prod.getCode());
				continue;
			}
			List<KLine> createList = new ArrayList<>();
			for (int i = 0; i < dailyKs.size(); i++) {
				JSONArray ele = dailyKs.getJSONArray(i);
				if (ele.getInteger(5) == null || ele.getIntValue(5) <= 0) {
					// 过滤交易为0的日K
					continue;
				}
				if (latest == null || latest.getDt().compareTo(ele.getString(0)) <= 0) {
					KLine daily = new KLine();
					if (latest != null && latest.getDt().equals(ele.getString(0))) {
						daily = latest;
					}
					daily.setCode(prod.getCode());
					daily.setDt(ele.getString(0));
					daily.setOpenPrice(ele.getBigDecimal(1));
					daily.setMaxPrice(ele.getBigDecimal(2));
					daily.setMinPrice(ele.getBigDecimal(3));
					daily.setEndPrice(ele.getBigDecimal(4));
					daily.setTradeVol(ele.getInteger(5));
					createList.add(daily);
				}
			}
			kLineRepository.save(createList);
			log.info(String.format("Download %s of %s", createList.size(), prod.getCode()));
		}
		log.info("Done!");
	}
}

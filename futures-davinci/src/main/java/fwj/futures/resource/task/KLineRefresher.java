package fwj.futures.resource.task;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Resources;

import fwj.futures.resource.buss.DailyPriceBuss;
import fwj.futures.resource.entity.price.ContractKLine;
import fwj.futures.resource.entity.price.KLine;
import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.repository.price.ContractKLineRepository;
import fwj.futures.resource.repository.price.KLineRepository;
import fwj.futures.resource.repository.prod.FuturesRepository;

@Component
public class KLineRefresher {

	private Logger log = Logger.getLogger(this.getClass());

	private static final String URI_DAILY = "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=%s0";
	private static final String CONTRACT_URI_DAILY = "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=%s";

	@Autowired
	private KLineRepository kLineRepository;

	@Autowired
	private ContractKLineRepository contractKLineRepository;

	@Autowired
	private FuturesRepository futuresRepository;

	@Autowired
	private DailyPriceBuss dailyPriceBuss;

	/**
	 * 每天15时10分调度。
	 */
	@Scheduled(cron = "0 10 15 * * ?")
	public void doTask() {
		refreshKLine();
		refreshContractKLine(false);
		log.info("Done!");
	}

	public void refreshContractKLine(boolean includeHist) {
		DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		List<Futures> allFutures = futuresRepository.findAllActive();
		for (Futures prod : allFutures) {
			log.info("Downloading contract of " + prod.getCode());

			int startMonth = includeHist ? -12 : 0;
			int endMonth = 12;
			DateFormat df = new SimpleDateFormat("yyMM");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, startMonth - 1);
			for (int month = startMonth; month <= endMonth; month++) {
				cal.add(Calendar.MONTH, 1);
				String contract = prod.getCode() + df.format(cal.getTime());
				String jsonStr = null;
				try {
					jsonStr = Resources.toString(new URL(String.format(CONTRACT_URI_DAILY, contract)),
							StandardCharsets.UTF_8);
				} catch (Exception e) {
					log.error("Bad boy " + prod.getCode(), e);
					continue;
				}
				JSONArray dailyKs = JSON.parseArray(jsonStr);
				if (dailyKs == null) {
					continue;
				}
				List<ContractKLine> createList = new ArrayList<>();
				int start = Math.max(0, dailyKs.size() - 300);
				for (int i = start; i < dailyKs.size(); i++) {
					JSONArray ele = dailyKs.getJSONArray(i);
					if (ele.getInteger(5) == null || ele.getIntValue(5) <= 0) {
						// 过滤交易为0的日K
						continue;
					}
					ContractKLine latest = contractKLineRepository.findTopByContractOrderByDtDesc(contract);
					try {
						Date dt = yyyyMMdd.parse(ele.getString(0));
						if (latest == null || latest.getDt().compareTo(dt) <= 0) {
							ContractKLine daily = new ContractKLine();
							if (latest != null && latest.getDt().equals(ele.getString(0))) {
								daily = latest;
							}
							daily.setCode(prod.getCode());
							daily.setContract(contract);
							daily.setDt(dt);
							daily.setOpenPrice(ele.getBigDecimal(1));
							daily.setMaxPrice(ele.getBigDecimal(2));
							daily.setMinPrice(ele.getBigDecimal(3));
							daily.setEndPrice(ele.getBigDecimal(4));
							daily.setTradeVol(ele.getInteger(5));
							createList.add(daily);
						}
					} catch (ParseException e) {
						log.error("oops", e);
					}
				}
				contractKLineRepository.save(createList);
				contractKLineRepository.flush();
				log.info(String.format("Download %s of %s", createList.size(), contract));
			}

		}
		dailyPriceBuss.reload();
	}

	public void refreshKLine() {
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
			kLineRepository.flush();
			log.info(String.format("Download %s of %s", createList.size(), prod.getCode()));
		}
		dailyPriceBuss.reload();
	}
}

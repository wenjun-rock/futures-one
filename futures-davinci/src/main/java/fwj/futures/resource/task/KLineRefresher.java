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
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Resources;

import fwj.futures.resource.price.buss.ContractDailyPriceBuss;
import fwj.futures.resource.price.buss.DailyPriceBuss;
import fwj.futures.resource.price.buss.ProdIndexBuss;
import fwj.futures.resource.price.entity.ContractKLine;
import fwj.futures.resource.price.entity.GlobalKLine;
import fwj.futures.resource.price.entity.KLine;
import fwj.futures.resource.price.repos.ContractKLineRepos;
import fwj.futures.resource.price.repos.GlobalKLineRepos;
import fwj.futures.resource.price.repos.KLineRepos;
import fwj.futures.resource.prod.entity.Futures;
import fwj.futures.resource.prod.entity.GlobalFutures;
import fwj.futures.resource.prod.repos.FuturesRepos;
import fwj.futures.resource.prod.repos.GlobalFuturesRepos;

@Component
public class KLineRefresher {

	private Logger log = Logger.getLogger(this.getClass());

	private static final String URI_DAILY = "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=%s0";
	private static final String CONTRACT_URI_DAILY = "http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesDailyKLine?symbol=%s";
	private static final String GLOBAL_URI_DAILY = "http://stock2.finance.sina.com.cn/futures/api/json.php/GlobalFuturesService.getGlobalFuturesDailyKLine?symbol=%s";

	@Autowired
	private KLineRepos kLineRepository;

	@Autowired
	private GlobalKLineRepos globalKLineRepository;

	@Autowired
	private ContractKLineRepos contractKLineRepository;

	@Autowired
	private FuturesRepos futuresRepository;

	@Autowired
	private GlobalFuturesRepos globalFuturesRepository;

	@Autowired
	private DailyPriceBuss dailyPriceBuss;

	@Autowired
	private ContractDailyPriceBuss contractDailyPriceBuss;

	@Autowired
	ProdIndexBuss prodIndexBuss;

	/**
	 * 每天15时45分调度。
	 */
	@Scheduled(cron = "0 45 15 * * ?")
	public void doTask() {
		refreshKLine();
		refreshGlobalKLine();
		refreshContractKLine(false);
		refreshProdIndex();
		log.info("Done!");
	}

	private void refreshProdIndex() {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String todayStr = df.format(new Date());
			Date today = df.parse(todayStr);
			for (Futures prod : futuresRepository.findAllActive()) {
				log.info(String.format("update %s for %s", prod.getCode(), todayStr));
				prodIndexBuss.updateProdIndex(prod.getCode(), today, today);
			}
		} catch (ParseException e) {
			log.error("", e);
		}
	}

	public void refreshGlobalKLine() {
		DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		for (GlobalFutures prod : globalFuturesRepository.findAll()) {
			log.info("Downloading " + prod.getGlobalCode());

			String jsonStr = null;
			try {
				jsonStr = Resources.toString(new URL(String.format(GLOBAL_URI_DAILY, prod.getGlobalCode())),
						StandardCharsets.ISO_8859_1);
			} catch (Exception e) {
				log.error("Bad boy " + prod.getCode(), e);
				continue;
			}
			JSONArray dailyKs = JSON.parseArray(jsonStr);
			if (dailyKs == null) {
				log.info("Can't get " + prod.getGlobalCode());
				continue;
			}
			GlobalKLine latest = globalKLineRepository.findTopByCodeOrderByDtDesc(prod.getGlobalCode());
			List<GlobalKLine> createList = new ArrayList<>();
			for (int i = 0; i < dailyKs.size(); i++) {
				JSONObject ele = dailyKs.getJSONObject(i);
				if (ele.getInteger("volume") == null || ele.getInteger("volume") <= 0) {
					// 过滤交易为0的日K
					continue;
				}
				try {
					Date dt = yyyyMMdd.parse(ele.getString("date"));
					if (latest == null || latest.getDt().compareTo(dt) <= 0) {
						GlobalKLine daily = new GlobalKLine();
						if (latest != null && latest.getDt().compareTo(dt) == 0) {
							daily = latest;
						}
						daily.setCode(prod.getGlobalCode());
						daily.setDt(dt);
						daily.setOpenPrice(ele.getBigDecimal("open"));
						daily.setMaxPrice(ele.getBigDecimal("high"));
						daily.setMinPrice(ele.getBigDecimal("low"));
						daily.setEndPrice(ele.getBigDecimal("close"));
						daily.setTradeVol(ele.getInteger("volume"));
						createList.add(daily);
					}
				} catch (ParseException e) {
					log.error("oops", e);
				}
			}
			globalKLineRepository.save(createList);
			globalKLineRepository.flush();
			log.info(String.format("Download %s of %s,%s", createList.size(), prod.getCode(), prod.getGlobalCode()));
		}
	}

	public void refreshContractKLine(boolean includeHist) {
		DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		List<Futures> allFutures = futuresRepository.findAllActive();
		for (Futures prod : allFutures) {
			log.info("Downloading contract of " + prod.getCode());

			int startMonth = includeHist ? -120 : 0;
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
					log.error("Bad boy " + contract, e);
					continue;
				}
				JSONArray dailyKs = JSON.parseArray(jsonStr);
				if (dailyKs == null) {
					continue;
				}
				int contractMonth = Integer.parseInt(contract.substring(contract.length() - 2));
				ContractKLine latest = contractKLineRepository
						.findTopByCodeAndContractMonthOrderByDtDesc(prod.getCode(), contractMonth);
				List<ContractKLine> createList = new ArrayList<>();
				for (int i = 0; i < dailyKs.size(); i++) {
					JSONArray ele = dailyKs.getJSONArray(i);
					if (ele.getInteger(5) == null || ele.getIntValue(5) <= 0) {
						// 过滤交易为0的日K
						continue;
					}

					try {
						Date dt = yyyyMMdd.parse(ele.getString(0));
						if (latest == null || latest.getDt().compareTo(dt) <= 0) {
							ContractKLine daily = new ContractKLine();
							if (latest != null && latest.getDt().compareTo(dt) == 0) {
								daily = latest;
							}
							daily.setCode(prod.getCode());
							daily.setContractMonth(contractMonth);
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
		contractDailyPriceBuss.reload();
	}

	public void refreshKLine() {
		DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		for (Futures prod : futuresRepository.findAllActive()) {
			log.info("Downloading " + prod.getCode());

			KLine latest = kLineRepository.findTopByCodeOrderByDtDesc(prod.getCode());
			String jsonStr = null;
			try {
				jsonStr = Resources.toString(new URL(String.format(URI_DAILY, prod.getCode())), StandardCharsets.UTF_8);
			} catch (Exception e) {
				log.error("Bad boy " + prod.getCode(), e);
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
				try {
					Date dt = yyyyMMdd.parse(ele.getString(0));
					if (latest == null || latest.getDt().compareTo(dt) <= 0) {
						KLine daily = new KLine();
						if (latest != null && latest.getDt().compareTo(dt) == 0) {
							daily = latest;
						}
						daily.setCode(prod.getCode());
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
			kLineRepository.save(createList);
			kLineRepository.flush();
			log.info(String.format("Download %s of %s", createList.size(), prod.getCode()));
		}
		dailyPriceBuss.reload();
	}
}

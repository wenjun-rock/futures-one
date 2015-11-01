package fwj.futures.resource.price.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import fwj.futures.resource.price.entity.ProdSpotPrice;
import fwj.futures.resource.price.repos.ProdSpotPriceRepos;
import fwj.futures.resource.price.vo.Price;
import fwj.futures.resource.price.vo.ProdSpot;
import fwj.futures.resource.prod.buss.ProdBuss;
import fwj.futures.resource.prod.entity.Futures;

@Component
public class ProdSpotPriceBuss {

	private static final String PATH = "http://www.100ppi.com/sf/day-%s.html";

	private Logger log = Logger.getLogger(this.getClass());

	private Map<String, String> prodNameCodeMap;

	@Autowired
	private ProdSpotPriceRepos prodSpotPriceRepos;

	@Autowired
	private ProdBuss productBuss;

	public ProdSpotPriceBuss() {
		prodNameCodeMap = new HashMap<>();
		prodNameCodeMap.put("铜", "CU");
		prodNameCodeMap.put("螺纹钢", "RB");
		prodNameCodeMap.put("锌", "ZN");
		prodNameCodeMap.put("铝", "AL");
		prodNameCodeMap.put("黄金", "AU");
		prodNameCodeMap.put("天然橡胶", "RU");
		prodNameCodeMap.put("铅", "PB");
		prodNameCodeMap.put("白银", "AG");
		prodNameCodeMap.put("石油沥青", "BU");
		prodNameCodeMap.put("热轧卷板", "HC");
		prodNameCodeMap.put("镍", "NI");
		prodNameCodeMap.put("锡", "SN");
		prodNameCodeMap.put("PTA", "TA");
		prodNameCodeMap.put("白糖", "SR");
		prodNameCodeMap.put("棉花", "CF");
		prodNameCodeMap.put("菜籽油OI", "OI");
		prodNameCodeMap.put("玻璃", "FG");
		prodNameCodeMap.put("菜籽粕", "RM");
		prodNameCodeMap.put("动力煤", "ZC");
		prodNameCodeMap.put("甲醇MA", "MA");
		prodNameCodeMap.put("棕榈油", "P");
		prodNameCodeMap.put("聚氯乙烯", "V");
		prodNameCodeMap.put("聚乙烯", "L");
		prodNameCodeMap.put("豆一", "A");
		prodNameCodeMap.put("豆粕", "M");
		prodNameCodeMap.put("豆油", "Y");
		prodNameCodeMap.put("玉米", "C");
		prodNameCodeMap.put("焦炭", "J");
		prodNameCodeMap.put("焦煤", "JM");
		prodNameCodeMap.put("铁矿石", "I");
		prodNameCodeMap.put("鸡蛋", "JD");
		prodNameCodeMap.put("聚丙烯", "PP");
		prodNameCodeMap.put("玉米淀粉", "CS");
	}

	@CacheEvict(value = "ProdSpotPriceBuss.queryProdSpotPrice", allEntries = true)
	public void updateProdSpotPrice(Date startDt, Date endDt) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDt);
		while (!cal.getTime().after(endDt)) {
			String date = df.format(cal.getTime());
			log.info(date);
			String url = String.format(PATH, date);
			Document doc = null;
			try {
				doc = Jsoup.connect(url).timeout(0).get();
			} catch (Exception ex) {
				log.error(ex);
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			Elements eles = doc.select("tr[align=center][bgcolor=#fafdff]");
			for (Element ele : eles) {
				String name = ele.select("td:eq(0) a").first().text();
				if (!prodNameCodeMap.containsKey(name)) {
					continue;
				}
				try {
					String code = prodNameCodeMap.get(name);
					BigDecimal spotPrice = new BigDecimal(ele.select("td:eq(1)").first().text().replaceAll(" ", ""));
					String latestCon = ele.select("td:eq(2)").first().text().replaceAll(" ", "");
					BigDecimal latestPrice = new BigDecimal(ele.select("td:eq(3)").first().text().replaceAll(" ", ""));
					BigDecimal latestDiff = spotPrice.subtract(latestPrice);
					BigDecimal latestPerc = latestDiff.divide(latestPrice, 4, RoundingMode.FLOOR);
					String mainCon = ele.select("td:eq(5)").first().text().replaceAll(" ", "");
					BigDecimal mainPrice = new BigDecimal(ele.select("td:eq(6)").first().text().replaceAll(" ", ""));
					BigDecimal mainDiff = spotPrice.subtract(mainPrice);
					BigDecimal mainPerc = mainDiff.divide(mainPrice, 4, RoundingMode.FLOOR);
					log.info(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", date, code, spotPrice, latestCon,
							latestPrice, latestDiff, latestPerc, mainCon, mainPrice, mainDiff, mainPerc));
					ProdSpotPrice ProdSpotPrice = new ProdSpotPrice();
					ProdSpotPrice.setDt(cal.getTime());
					ProdSpotPrice.setCode(code);
					ProdSpotPrice.setSpotPrice(spotPrice);
					ProdSpotPrice.setLatestCon(latestCon);
					ProdSpotPrice.setLatestPrice(latestPrice);
					ProdSpotPrice.setLatestDiff(latestDiff);
					ProdSpotPrice.setLatestPerc(latestPerc);
					ProdSpotPrice.setMainCon(mainCon);
					ProdSpotPrice.setMainPrice(mainPrice);
					ProdSpotPrice.setMainDiff(mainDiff);
					ProdSpotPrice.setMainPerc(mainPerc);
					prodSpotPriceRepos.save(ProdSpotPrice);
				} catch (Exception ex) {
					log.error(ex);
				}
			}
			prodSpotPriceRepos.flush();
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

	}

	@Cacheable(value = "ProdSpotPriceBuss.queryProdSpotPrice")
	public ProdSpot queryProdSpotPrice(String code, int month) {
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return null;
		} else {
			List<ProdSpotPrice> priceList = null;
			if (month < 0) {
				// 所有
				priceList = prodSpotPriceRepos.findByCodeOrderByDtAsc(code);
			} else {
				// 指定月数
				Calendar cal = Calendar.getInstance();
				Date endDt = cal.getTime();
				cal.add(Calendar.MONTH, -month);
				Date startDt = cal.getTime();
				priceList = prodSpotPriceRepos.findByCodeAndDtBetweenOrderByDtAsc(code, startDt, endDt);
			}

			List<Price> spotPriceList = new ArrayList<>();
			List<Price> latestPriceList = new ArrayList<>();
			List<Price> mainPriceList = new ArrayList<>();
			List<Price> latestPercList = new ArrayList<>();
			List<Price> mainPercList = new ArrayList<>();
			ProdSpot prodSpot = new ProdSpot(prod.getCode(), prod.getName(), spotPriceList, latestPriceList,
					mainPriceList, latestPercList, mainPercList);
			priceList.forEach(spot -> {
				spotPriceList.add(new Price(spot.getDt(), spot.getSpotPrice()));
				latestPriceList.add(new Price(spot.getDt(), spot.getLatestPrice()));
				mainPriceList.add(new Price(spot.getDt(), spot.getMainPrice()));
				latestPercList.add(new Price(spot.getDt(), spot.getLatestPerc().multiply(new BigDecimal(100))));
				mainPercList.add(new Price(spot.getDt(), spot.getMainPerc().multiply(new BigDecimal(100))));
			});

			return prodSpot;
		}
	}

}

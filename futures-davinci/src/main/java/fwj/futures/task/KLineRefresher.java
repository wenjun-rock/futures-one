package fwj.futures.task;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Resources;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataURI;
import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.ProductRepository;

@Component
public class KLineRefresher extends AbstractBaseLaunch {

	@Autowired
	private DataURI dataURI;

	@Autowired
	private KLineRepository kLineRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	protected void execute() throws Exception {

		for (Product prod : productRepository.findAll()) {
			log.info(String.format("Downloading %s...", prod.getCode()));

			KLine latest = kLineRepository.findTopByCodeOrderByDtDesc(prod.getCode());
			log.info(JSON.toJSONString(latest));
			String jsonStr = Resources.toString(dataURI.getDailyKLineUrl(prod.getCode()), StandardCharsets.UTF_8);
			JSONArray dailyKs = JSON.parseArray(jsonStr);
			if (dailyKs == null) {
				return;
			}
			List<KLine> createList = new ArrayList<>();
			for (int i = 0; i < dailyKs.size(); i++) {
				JSONArray ele = dailyKs.getJSONArray(i);
				if (ele.getInteger(5) == null || ele.getIntValue(5) <= 0) {
					continue;
				}
				if (latest == null || latest.getDt().compareTo(ele.getString(0)) <= 0) {
					KLine daily = new KLine();
					if(latest != null && latest.getDt().equals(ele.getString(0))) {
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
			log.info(String.format("put %s...", createList.size(), prod.getCode()));
			kLineRepository.save(createList);
			log.info(String.format("put done %s...", createList.size(), prod.getCode()));

			log.info(String.format("Download %s of %s...", createList.size(), prod.getCode()));
		}
		System.out.println("Done!");
	}

	public static void main(String[] args) {
		launch(KLineRefresher.class);
	}
}

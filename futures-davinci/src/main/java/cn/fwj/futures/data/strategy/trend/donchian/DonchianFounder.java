package cn.fwj.futures.data.strategy.trend.donchian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.launch.AbstractBaseLaunch;
import cn.fwj.futures.data.repository.KLineRepo;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianTrend;
import cn.fwj.futures.data.struct.DailyKLine;

@Component
public class DonchianFounder extends AbstractBaseLaunch {

	@Autowired
	private KLineRepo kLineRepo;

	@Override
	protected void execute() throws Exception {
		DailyKLine kLine = kLineRepo.loadDailyKLine(Product.HuangJin);
//		DonchianTrend trend = new DonchianTrendBuilder("2015-06-16", "2016-01-01", 20, 10).createDonchianTrend(kLine);
		DonchianTrend trend = new DonchianTrendBuilder("2015-05-18", "2016-01-01", 20, 10).createDonchianTrend(kLine);
		System.out.println(JSON.toJSONString(trend));
	}

	public static void main(String[] args) {
		launch(DonchianFounder.class);
	}

}

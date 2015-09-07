package cn.fwj.futures.data.strategy.trend.donchian;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.launch.AbstractBaseLaunch;
import cn.fwj.futures.data.repository.KLineRepo;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianTrend;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianWave;
import cn.fwj.futures.data.strategy.trend.donchian.struct.DonchianWave.Direction;
import cn.fwj.futures.data.struct.DailyKLine;

@Component
public class DonchianFounder extends AbstractBaseLaunch {

	@Autowired
	private KLineRepo kLineRepo;

	@Override
	protected void execute() throws Exception {

		for (Product product : Product.values()) {
			DailyKLine kLine = kLineRepo.loadDailyKLine(product);
			DonchianTrend trend = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10)
					.createDonchianTrend(kLine);

			BigDecimal total = BigDecimal.ZERO;
			int size = 0;
			for (DonchianWave wave : trend.getHistWave()) {
				if (wave.getExitPrice() == null) {
					continue;
				}
				size++;
				if (wave.getDirection() == Direction.UP) {
					total = total.add(wave.getExitPrice()).subtract(wave.getEnterPrice());
				} else if (wave.getDirection() == Direction.DOWN) {
					total = total.add(wave.getEnterPrice()).subtract(wave.getExitPrice());
				}
			}
			System.out.println(String.format("%s\t%s\t%s", product, size, total));
		}
	}

	public static void main(String[] args) {
		launch(DonchianFounder.class);
	}

}

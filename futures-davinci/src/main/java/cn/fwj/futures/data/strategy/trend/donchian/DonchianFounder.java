package cn.fwj.futures.data.strategy.trend.donchian;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	protected void execute() throws Exception {

		DonchianTrendBuilder builder = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10);
		DailyKLine kLine = kLineRepo.loadDailyKLine(Product.DouYou);
		DonchianTrend trend = builder.createEndPriceDonchianTrend(kLine);

		BigDecimal total = BigDecimal.ZERO;
		int size = 0;
		for (DonchianWave wave : trend.getHistWave()) {
			size++;
			 if (wave.getDirection() == Direction.UP) {
			  total = wave.getExitPrice().subtract(wave.getEnterPrice());
			 } else if (wave.getDirection() == Direction.DOWN) {
				 total = wave.getEnterPrice().subtract(wave.getExitPrice());
			 }
			System.out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s", size,total, wave.getDirection(), wave.getStartDt(),
					wave.getEndDt(), wave.getEnterPrice(), wave.getExitPrice()));
		}

	}

	protected void execute2() throws Exception {

		DonchianTrendBuilder builder = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10);
		for (Product product : Product.values()) {
			DailyKLine kLine = kLineRepo.loadDailyKLine(product);
			DonchianTrend trend = builder.createEndPriceDonchianTrend(kLine);

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

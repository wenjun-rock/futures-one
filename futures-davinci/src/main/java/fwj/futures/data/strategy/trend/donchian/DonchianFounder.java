package fwj.futures.data.strategy.trend.donchian;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.repository.KLineRepo;
import fwj.futures.data.strategy.trend.donchian.struct.DonchianTrend;
import fwj.futures.data.strategy.trend.donchian.struct.DonchianWave;
import fwj.futures.data.strategy.trend.donchian.struct.DonchianWave.Direction;
import fwj.futures.data.struct.DailyKLine;

@Component
public class DonchianFounder extends AbstractBaseLaunch {

	@Autowired
	private KLineRepo kLineRepo;
	
	protected void execute11() throws Exception {

		DonchianTrendBuilder builder = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10);
		for (ProdEnum product : ProdEnum.values()) {
			DailyKLine kLine = kLineRepo.loadDailyKLine(product);
			DonchianTrend trendMinMax = builder.createMinMaxPriceDonchianTrend(kLine);
			if(trendMinMax.getCurrentWave() == null) {
				System.out.println(String.format("%-8s\t%s\t%s\t%s", product, trendMinMax.getPrepare(), trendMinMax.getHistWave().size(),
						trendMinMax.totalProfit()));
			} else {
				System.out.println(String.format("%-8s\t%s\t%s\t%s", product, trendMinMax.getCurrentWave(), trendMinMax.getHistWave().size(),
						trendMinMax.totalProfit()));
			}
		}
	}

	protected void compare() throws Exception {

		DonchianTrendBuilder builder = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10);
		for (ProdEnum product : ProdEnum.values()) {
			DailyKLine kLine = kLineRepo.loadDailyKLine(product);
			DonchianTrend trendMinMax = builder.createMinMaxPriceDonchianTrend(kLine);
			DonchianTrend trendEnd = builder.createEndPriceDonchianTrend(kLine);
			System.out.println(String.format("%s\t%s\t%s\t%s\t%s", product, trendMinMax.getHistWave().size(),
					trendMinMax.totalProfit(), trendEnd.getHistWave().size(), trendEnd.totalProfit()));
		}
	}

	protected void execute() throws Exception {

		DonchianTrendBuilder builder = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10);
		DailyKLine kLine = kLineRepo.loadDailyKLine(ProdEnum.Lv);
		// DonchianTrend trend = builder.createMinMaxPriceDonchianTrend(kLine);
		DonchianTrend trend = builder.createEndPriceDonchianTrend(kLine);

		BigDecimal profit = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		int size = 0;
		for (DonchianWave wave : trend.getHistWave()) {
			size++;
			if (wave.getDirection() == Direction.UP) {
				profit = wave.getExitPrice().subtract(wave.getEnterPrice());
			} else if (wave.getDirection() == Direction.DOWN) {
				profit = wave.getEnterPrice().subtract(wave.getExitPrice());
			}
			total = total.add(profit);
			System.out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s", size, profit, wave.getDirection(),
					wave.getStartDt(), wave.getEndDt(), wave.getEnterPrice(), wave.getExitPrice()));
		}
		System.out.println(total);

	}

	protected void execute2() throws Exception {

		DonchianTrendBuilder builder = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10);
		DailyKLine kLine = kLineRepo.loadDailyKLine(ProdEnum.DouYou);
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
			System.out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s", size, total, wave.getDirection(),
					wave.getStartDt(), wave.getEndDt(), wave.getEnterPrice(), wave.getExitPrice()));
		}

	}

	protected void execute2bak() throws Exception {

		DonchianTrendBuilder builder = new DonchianTrendBuilder("2005-01-01", "2016-01-01", 20, 10);
		for (ProdEnum product : ProdEnum.values()) {
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

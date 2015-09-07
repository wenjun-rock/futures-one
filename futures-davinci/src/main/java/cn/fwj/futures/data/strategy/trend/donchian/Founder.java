package cn.fwj.futures.data.strategy.trend.donchian;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.launch.AbstractBaseLaunch;
import cn.fwj.futures.data.repository.KLineRepo;
import cn.fwj.futures.data.struct.DailyK;
import cn.fwj.futures.data.struct.DailyKLine;

@Component
public class Founder extends AbstractBaseLaunch {

	@Autowired
	private KLineRepo kLineRepo;

	@Override
	protected void execute() throws Exception {
		List<DonchianResult> list = new ArrayList<>();
		for (Product product : Product.values()) {
			list.add(createDonchianResult(product, 20));
		}
		Collections.sort(list);
		for (DonchianResult donchianResult : list) {
			System.out.println(donchianResult);
		}
	}

	private DonchianResult createDonchianResult(Product product, int range) throws Exception {
		DailyKLine kLine = kLineRepo.loadDailyKLine(product);
		ArrayList<DailyK> kList = new ArrayList<>(kLine.getAllDailyK());
		int startIndex = (kList.size() - 1 < range) ? 0 : (kList.size() - 1 - range);
		BigDecimal minPrice = new BigDecimal(Integer.MAX_VALUE);
		BigDecimal maxPrice = new BigDecimal(Integer.MIN_VALUE);
		for (int i = startIndex; i < kList.size() - 1; i++) {
			DailyK temp = kList.get(i);
			if (temp.getMaxPrice().compareTo(maxPrice) > 0) {
				maxPrice = temp.getMaxPrice();
			}
			if (temp.getMinPrice().compareTo(minPrice) < 0) {
				minPrice = temp.getMinPrice();
			}
		}
		DailyK currentDailyK = kList.get(kList.size() - 1);
		return new DonchianResult(range, product, minPrice, maxPrice, currentDailyK.getEndPrice(),
				currentDailyK.getDt(), currentDailyK.getTradeVol());
	}

	public static void main(String[] args) {
		launch(Founder.class);
	}

	public static class DonchianResult implements Comparable<DonchianResult> {

		private int range;
		private Product product;
		private BigDecimal minPrice;
		private BigDecimal maxPrice;
		private BigDecimal currentPrice;
		private BigDecimal position;
		private String dt;
		private long tradeVol;

		public DonchianResult(int range, Product product, BigDecimal minPrice, BigDecimal maxPrice,
				BigDecimal currentPrice, String dt, long tradeVol) {
			this.range = range;
			this.product = product;
			this.currentPrice = currentPrice;
			this.minPrice = minPrice;
			this.maxPrice = maxPrice;
			this.dt = dt;
			this.tradeVol = tradeVol;
			this.position = currentPrice.subtract(minPrice).divide(maxPrice.subtract(minPrice), 2, RoundingMode.UP);
		}

		public Product getProduct() {
			return product;
		}

		public int getRange() {
			return range;
		}

		public BigDecimal getCurrentPrice() {
			return currentPrice;
		}

		public BigDecimal getMinPrice() {
			return minPrice;
		}

		public BigDecimal getMaxPrice() {
			return maxPrice;
		}

		public BigDecimal getPosition() {
			return position;
		}

		public void setProduct(Product product) {
			this.product = product;
		}

		public void setRange(int range) {
			this.range = range;
		}

		public void setCurrentPrice(BigDecimal currentPrice) {
			this.currentPrice = currentPrice;
		}

		public void setMinPrice(BigDecimal minPrice) {
			this.minPrice = minPrice;
		}

		public void setMaxPrice(BigDecimal maxPrice) {
			this.maxPrice = maxPrice;
		}

		public void setPosition(BigDecimal position) {
			this.position = position;
		}

		public String getDt() {
			return dt;
		}

		public long getTradeVol() {
			return tradeVol;
		}

		public void setDt(String dt) {
			this.dt = dt;
		}

		public void setTradeVol(long tradeVol) {
			this.tradeVol = tradeVol;
		}

		public String toString() {
			return String.format("%s\t%10s\t%10s\t%10s\t%10s\t%10s\t%10s", position, product, minPrice, maxPrice,
					currentPrice, dt, tradeVol);
		}

		@Override
		public int compareTo(DonchianResult that) {
			if (that == null || that.position == null) {
				return -1;
			} else if (this.position == null) {
				return 1;
			} else {
				return this.position.compareTo(that.position);
			}
		}

	}

}

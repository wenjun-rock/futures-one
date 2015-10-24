package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.price.buss.DailyPriceBuss;
import fwj.futures.resource.price.entity.KLine;
import fwj.futures.resource.prod.buss.ProductBuss;
import fwj.futures.resource.vo.UnitData;
import fwj.futures.resource.web.vo.ProductPriceAggre;

@Component
public class ProdPriceBuss {

	@Autowired
	private ProductBuss productBuss;

	@Autowired
	private DailyPriceBuss dailyPriceBuss;

	@Autowired
	private RealTimePriceBuss realTimePriceBuss;

	public List<ProductPriceAggre> queryPrice(List<String> codeList) {
		return codeList.stream().map(code -> {
			Futures futures = productBuss.queryFuturesByCode(code);
			if (futures == null) {
				return new ProductPriceAggre();
			} else {
				ProductPriceAggre prod = new ProductPriceAggre();
				prod.setCode(futures.getCode());
				prod.setName(futures.getName());
				List<KLine> kLineList = dailyPriceBuss.queryDescByCode(code);
				prod.setLast1KIncPct(this.lastKIncPct(kLineList, 1));
				prod.setLast5KIncPct(this.lastKIncPct(kLineList, 5));
				prod.setLast10KIncPct(this.lastKIncPct(kLineList, 10));
				prod.setLast20KIncPct(this.lastKIncPct(kLineList, 20));
				prod.setLast30KIncPct(this.lastKIncPct(kLineList, 30));
				prod.setLast60KIncPct(this.lastKIncPct(kLineList, 60));
				prod.setLast120KIncPct(this.lastKIncPct(kLineList, 120));
				prod.setLast250KIncPct(this.lastKIncPct(kLineList, 250));
				List<UnitData> unitData = realTimePriceBuss.queryDescByCode(code);
				prod.setLast1RIncPct(this.lastRIncPct(unitData, 1));
				prod.setLast5RIncPct(this.lastRIncPct(unitData, 5));
				prod.setLast10RIncPct(this.lastRIncPct(unitData, 10));
				prod.setLast20RIncPct(this.lastRIncPct(unitData, 20));
				prod.setLast30RIncPct(this.lastRIncPct(unitData, 30));
				prod.setLast60RIncPct(this.lastRIncPct(unitData, 60));
				prod.setLast120RIncPct(this.lastRIncPct(unitData, 120));
				prod.setLast250RIncPct(this.lastRIncPct(unitData, 250));
				if (unitData.isEmpty()) {
					prod.setPrice(BigDecimal.ZERO);
					prod.setPriceTime(0);
				} else {
					prod.setPrice(unitData.get(0).getPrice());
					prod.setPriceTime(unitData.get(0).getDatetime().getTime());
				}
				return prod;
			}
		}).collect(Collectors.toList());
	}

	private BigDecimal lastRIncPct(List<UnitData> unitDataList, int i) {
		if (unitDataList == null || unitDataList.size() == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal newPrice = unitDataList.get(0).getPrice();
		BigDecimal oldPrice = unitDataList.get(Math.min(unitDataList.size(), i + 1) - 1).getPrice();
		if (oldPrice.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		return newPrice.subtract(oldPrice).divide(oldPrice, 4, RoundingMode.DOWN);
	}

	private BigDecimal lastKIncPct(List<KLine> kLineList, int i) {
		if (kLineList == null || kLineList.size() == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal newPrice = kLineList.get(0).getEndPrice();
		BigDecimal oldPrice = kLineList.get(Math.min(kLineList.size(), i) - 1).getOpenPrice();
		if (oldPrice.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		return newPrice.subtract(oldPrice).divide(oldPrice, 4, RoundingMode.DOWN);
	}

}

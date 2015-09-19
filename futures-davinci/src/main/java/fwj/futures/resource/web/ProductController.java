package fwj.futures.resource.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.buss.KLineBuss;
import fwj.futures.resource.buss.ProductBuss;
import fwj.futures.resource.buss.RealTimePriceBuss;
import fwj.futures.resource.entity.Futures;
import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.task.RealtimeHolder.UnitData;
import fwj.futures.resource.vo.ProductInfo;
import fwj.futures.resource.vo.ProductLabel;
import fwj.futures.resource.web.vo.ProductPrice;

@RestController()
@RequestMapping("/web/product")
public class ProductController {

	@Autowired
	private ProductBuss productBuss;

	@Autowired
	private KLineBuss kLineBuss;

	@Autowired
	private RealTimePriceBuss realTimePriceBuss;

	@RequestMapping("/labels")
	public List<ProductLabel> queryAllLabels() {
		return productBuss.queryAllLabels();
	}
	
	@RequestMapping("/info/code/{code}")
	public ProductInfo queryInfoByCode(@PathVariable("code") String code) {
		return productBuss.queryInfoByCode(code);
	}

	@RequestMapping("/price/label/{id}")
	public List<ProductPrice> queryPriceByLabel(@PathVariable("id") Integer id) {
		List<String> codes = id == 0 ? productBuss.queryAllCode() : productBuss.queryCodeByLabelId(id);
		return queryPrice(codes);
	}

	@RequestMapping("/price/code/{code}")
	public ProductPrice queryPriceByCode(@PathVariable("code") String code) {
		return queryPrice(Arrays.asList(code)).get(0);
	}

	private List<ProductPrice> queryPrice(List<String> codeList) {
		return codeList.stream().map(code -> {
			Futures futures = productBuss.queryFuturesByCode(code);
			if (futures == null) {
				return new ProductPrice();
			} else {
				ProductPrice prod = new ProductPrice();
				prod.setCode(futures.getCode());
				prod.setName(futures.getName());
				List<KLine> kLineList = kLineBuss.queryLatest60ByCode(code);
				prod.setLast1KIncPct(this.lastKIncPct(kLineList, 1));
				prod.setLast5KIncPct(this.lastKIncPct(kLineList, 5));
				prod.setLast10KIncPct(this.lastKIncPct(kLineList, 10));
				prod.setLast30KIncPct(this.lastKIncPct(kLineList, 30));
				prod.setLast60KIncPct(this.lastKIncPct(kLineList, 60));
				List<UnitData> unitData = realTimePriceBuss.queryReverseByCode(code);
				prod.setLast1RIncPct(this.lastRIncPct(unitData, 1));
				prod.setLast5RIncPct(this.lastRIncPct(unitData, 5));
				prod.setLast10RIncPct(this.lastRIncPct(unitData, 10));
				prod.setLast30RIncPct(this.lastRIncPct(unitData, 30));
				prod.setLast60RIncPct(this.lastRIncPct(unitData, 60));
				if (unitData.isEmpty()) {
					prod.setPrice(BigDecimal.ZERO);
					prod.setPriceTime("");
				} else {
					prod.setPrice(unitData.get(0).getPrice());
					prod.setPriceTime(unitData.get(0).getDatetime());
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
		if (oldPrice.equals(BigDecimal.ZERO)) {
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
		if (oldPrice.equals(BigDecimal.ZERO)) {
			return BigDecimal.ZERO;
		}
		return newPrice.subtract(oldPrice).divide(oldPrice, 4, RoundingMode.DOWN);
	}
}

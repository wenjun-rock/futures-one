package fwj.futures.resource.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.ProductRepository;
import fwj.futures.resource.task.RealtimeHolder;
import fwj.futures.resource.task.RealtimeHolder.UnitData;
import fwj.futures.resource.task.RealtimeHolder.UnitDataGroup;
import fwj.futures.resource.web.vo.Futures;

@RestController()
@RequestMapping("/web/futures")
public class FuturesController {

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private RealtimeHolder realtimeHolder;

	@RequestMapping("/all")
	public List<Futures> queryAllFutures() {
		String[] codes = productRepo.findAllActive().stream().map(Product::getCode).toArray(String[]::new);
		return queryFutures(codes);
	}

	@RequestMapping("/code/{codes}")
	public List<Futures> queryFutures(@PathVariable("codes") String codes) {
		return queryFutures(codes.split(","));
	}

	private List<Futures> queryFutures(String[] codes) {

		List<UnitDataGroup> unitDataGroupList = realtimeHolder.getRealtime();

		return Stream.of(codes).map(code -> {
			Product prod = productRepo.findByCode(code);
			if (prod == null) {
				return new Futures();
			} else {
				Futures f = new Futures();
				f.setProduct(prod);
				List<KLine> kLineList = kLineRepo.findTop60ByCodeOrderByDtDesc(code);
				f.setLast1KIncPct(this.lastKIncPct(kLineList, 1));
				f.setLast5KIncPct(this.lastKIncPct(kLineList, 5));
				f.setLast10KIncPct(this.lastKIncPct(kLineList, 10));
				f.setLast30KIncPct(this.lastKIncPct(kLineList, 30));
				f.setLast60KIncPct(this.lastKIncPct(kLineList, 60));

				List<UnitData> unitData = unitDataGroupList.stream().flatMap(group -> group.getUnitDataList().stream())
						.filter(unit -> code.equals(unit.getCode())).sorted().collect(Collectors.toList());
				Collections.reverse(unitData);
				f.setLast1RIncPct(this.lastRIncPct(unitData, 1));
				f.setLast5RIncPct(this.lastRIncPct(unitData, 5));
				f.setLast10RIncPct(this.lastRIncPct(unitData, 10));
				f.setLast30RIncPct(this.lastRIncPct(unitData, 30));
				f.setLast60RIncPct(this.lastRIncPct(unitData, 60));
				f.setPrice(unitData.isEmpty() ? BigDecimal.ZERO : unitData.get(0).getPrice());
				return f;
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

package fwj.futures.resource.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Futures;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.FuturesRepository;
import fwj.futures.resource.task.RealtimeHolder;
import fwj.futures.resource.task.RealtimeHolder.UnitDataGroup;
import fwj.futures.resource.web.vo.ProductPrice;
import fwj.futures.resource.web.vo.Series;

@RestController()
@RequestMapping("/web/price")
public class PriceController {

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private FuturesRepository productRepo;

	@Autowired
	private RealtimeHolder realtimeHolder;

	@RequestMapping("/daily/{codes}")
	public List<Series> findDailyByCodes(@PathVariable("codes") String codes) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return Stream.of(codes.split(",")).map(code -> {
			Futures prod = productRepo.findByCode(code);
			if (prod == null) {
				return Series.EMPTY;
			} else {
				List<KLine> kLineList = kLineRepo.findByCode(code);
				List<Object[]> data = kLineList.stream().map(kLine -> {
					long time = 0;
					try {
						time = df.parse(kLine.getDt()).getTime();
					} catch (Exception e) {
					}
					BigDecimal price = kLine.getEndPrice();
					return new Object[] { time, price };
				}).collect(Collectors.toList());
				return new ProductPrice(prod, data.toArray(new Object[0][2])).toSeries();
			}
		}).collect(Collectors.toList());
	}

	@RequestMapping("/realtime/{codes}")
	public List<Series> findRealtimeByCodes(@PathVariable("codes") String codes) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return Stream.of(codes.split(",")).map(code -> {
			Futures prod = productRepo.findByCode(code);
			if (prod == null) {
				return Series.EMPTY;
			} else {
				List<UnitDataGroup> unitDataGroupList = realtimeHolder.getRealtime();
				List<Object[]> data = unitDataGroupList.stream()
						.flatMap(unitDataGroup -> unitDataGroup.getUnitDataList().stream())
						.filter(unitData -> unitData.getCode().equals(prod.getCode())).sorted().map(unitData -> {
					long time = 0;
					try {
						time = df.parse(unitData.getDatetime()).getTime();
					} catch (Exception e) {
					}
					BigDecimal price = unitData.getPrice();
					return new Object[] { time, price };
				}).collect(Collectors.toList());
				return new ProductPrice(prod, data.toArray(new Object[0][2])).toSeries();
			}
		}).collect(Collectors.toList());
	}

}

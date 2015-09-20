package fwj.futures.resource.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.buss.DailyPriceBuss;
import fwj.futures.resource.buss.RealTimePriceBuss;
import fwj.futures.resource.web.vo.Series;

@RestController()
@RequestMapping("/web/price")
public class PriceController {
	
	@Autowired
    private DailyPriceBuss dailyPriceBuss;
	
	@Autowired
    private RealTimePriceBuss realTimePriceBuss;

	@RequestMapping("/daily/{codes}")
	public List<Series> findDailyByCodes(@PathVariable("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> dailyPriceBuss.querySeriesByCode(code))
				.collect(Collectors.toList());
	}

	@RequestMapping("/realtime/{codes}")
	public List<Series> findRealtimeByCodes(@PathVariable("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> realTimePriceBuss.querySeriesByCode(code))
				.collect(Collectors.toList());
	}

}

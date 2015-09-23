package fwj.futures.resource.web.ctrl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.buss.DailyPriceBuss;
import fwj.futures.resource.buss.RealTimePriceBuss;
import fwj.futures.resource.web.vo.Series;

@RestController()
@RequestMapping("/price")
public class PriceCtrl {

	@Autowired
	private DailyPriceBuss dailyPriceBuss;

	@Autowired
	private RealTimePriceBuss realTimePriceBuss;

	@RequestMapping(value = "/daily", method = RequestMethod.GET)
	public List<Series> findDailyByCodes(@RequestParam("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> dailyPriceBuss.querySeriesByCode(code))
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/realtime", method = RequestMethod.GET)
	public List<Series> findRealtimeByCodes(@RequestParam("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> realTimePriceBuss.querySeriesByCode(code))
				.collect(Collectors.toList());
	}

}

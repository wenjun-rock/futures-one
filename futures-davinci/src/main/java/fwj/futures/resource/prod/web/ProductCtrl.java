package fwj.futures.resource.prod.web;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.buss.ContractDailyPriceBuss;
import fwj.futures.resource.buss.ProdPriceBuss;
import fwj.futures.resource.buss.ProductBuss;
import fwj.futures.resource.buss.RealTimePriceBuss;
import fwj.futures.resource.price.buss.DailyPriceBuss;
import fwj.futures.resource.price.vo.ProdDailyPrice;
import fwj.futures.resource.vo.ProductInfo;
import fwj.futures.resource.vo.ProductLabel;
import fwj.futures.resource.vo.UnitDataGroup;
import fwj.futures.resource.web.vo.ProdContracts;
import fwj.futures.resource.web.vo.ProductPriceAggre;
import fwj.futures.resource.web.vo.Series;

@RestController()
@RequestMapping("/product")
public class ProductCtrl {

	@Autowired
	private ProductBuss productBuss;

	@Autowired
	private DailyPriceBuss dailyPriceBuss;

	@Autowired
	private ContractDailyPriceBuss contractDailyPriceBuss;

	@Autowired
	private RealTimePriceBuss realTimePriceBuss;

	@Autowired
	private ProdPriceBuss prodPriceBuss;

	@RequestMapping(value = "/labels", method = RequestMethod.GET)
	public List<ProductLabel> queryAllLabels() {
		return productBuss.queryAllLabels();
	}

	@RequestMapping(value = "/prod-info", method = RequestMethod.GET)
	public ProductInfo queryInfoByCode(@RequestParam("code") String code) {
		return productBuss.queryInfoByCode(code);
	}

	@RequestMapping(value = "/price-label-aggre", method = RequestMethod.GET)
	public List<ProductPriceAggre> queryPriceByLabel(@RequestParam("id") Integer id) {
		List<String> codes = id == 0 ? productBuss.queryAllCode() : productBuss.queryCodeByLabelId(id);
		return prodPriceBuss.queryPrice(codes);
	}

	@RequestMapping(value = "/price-prod-aggre", method = RequestMethod.GET)
	public ProductPriceAggre queryPriceByCode(@RequestParam("code") String code) {
		return prodPriceBuss.queryPrice(Arrays.asList(code)).get(0);
	}

	@RequestMapping(value = "/price-label-lastday", method = RequestMethod.GET)
	public List<Series> queryLastdayPriceByLabel(@RequestParam("id") Integer id) {
		List<String> codes = id == 0 ? productBuss.queryAllCode() : productBuss.queryCodeByLabelId(id);
		return codes.stream().map(code -> realTimePriceBuss.queryLastDaySeriesByCode(code))
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/price-latest", method = RequestMethod.GET)
	public UnitDataGroup queryLatestPrice() {
		return realTimePriceBuss.queryLatest();
	}

	@RequestMapping(value = "/price-prod-daily2", method = RequestMethod.GET)
	public List<Series> findDailyByCodes(@RequestParam("codes") String codes,
			@RequestParam(value = "month", defaultValue = "-1") int month) {
		return Stream.of(codes.split(",")).map(code -> dailyPriceBuss.querySeriesByCode(code, month))
				.collect(Collectors.toList());
	}
	
	@RequestMapping(value = "/price-prod-daily", method = RequestMethod.GET)
	public ProdDailyPrice queryDailyByCode(@RequestParam("code") String code,
			@RequestParam(value = "month", defaultValue = "-1") int month) {
		return dailyPriceBuss.queryProdDailyPrice(code, month);
	}

	@RequestMapping(value = "/price-prod-realtime", method = RequestMethod.GET)
	public List<Series> findRealtimeByCodes(@RequestParam("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> realTimePriceBuss.querySeriesByCode(code))
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/price-contract-daily", method = RequestMethod.GET)
	public ProdContracts findContractDailyByCode(@RequestParam("code") String code) {
		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		cal.add(Calendar.YEAR, -4);
		Date startDate = cal.getTime();
		return contractDailyPriceBuss.getConstractsByCode(code, startDate, endDate);
	}

}

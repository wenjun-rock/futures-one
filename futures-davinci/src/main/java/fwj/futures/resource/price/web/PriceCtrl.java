package fwj.futures.resource.price.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.price.buss.ContractDailyPriceBuss;
import fwj.futures.resource.price.vo.Price;
import fwj.futures.resource.prod.buss.ProdBuss;
import fwj.futures.resource.prod.entity.ProdMainCon;
import fwj.futures.resource.util.YearTimeLineContainer;

@RestController()
@RequestMapping("/price")
public class PriceCtrl {

	@Autowired
	private ProdBuss productBuss;

	@Autowired
	private ContractDailyPriceBuss conPriceBuss;

	@RequestMapping(value = "/main-contract-year", method = RequestMethod.GET)
	public List<YearTimeLineContainer<Price>> queryMainConYear(@RequestParam("code") String code,
			@RequestParam(value = "year", defaultValue = "3") int year) throws Exception {
		Calendar cal = Calendar.getInstance();
		Date end = cal.getTime();
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse((cal.get(Calendar.YEAR) - year) + "-01-01");

		List<ProdMainCon> conList = productBuss.queryMainConByCode(code);
		return conList.stream().map(con -> {
			return conPriceBuss.queryWithRangeInYear(con.getCode(), con.getMonth(), start, end);
		}).collect(Collectors.toList());
	}

}

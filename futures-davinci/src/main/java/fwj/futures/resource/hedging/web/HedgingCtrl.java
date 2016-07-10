package fwj.futures.resource.hedging.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.data.struct.Formula;
import fwj.futures.resource.hedging.buss.HedgingBuss;
import fwj.futures.resource.hedging.buss.HedgingContractBuss;
import fwj.futures.resource.hedging.buss.HedgingExperimentBuss;
import fwj.futures.resource.hedging.buss.HedgingProdContractBuss;
import fwj.futures.resource.hedging.entity.HedgingContract;
import fwj.futures.resource.hedging.entity.HedgingProdContract;
import fwj.futures.resource.hedging.vo.HedgingContractContainer;
import fwj.futures.resource.hedging.vo.HedgingExperimentMonitor;
import fwj.futures.resource.hedging.vo.HedgingExperimentView;
import fwj.futures.resource.price.buss.ContractDailyPriceBuss;
import fwj.futures.resource.price.buss.KLineBuss;
import fwj.futures.resource.price.entity.ContractKLine;
import fwj.futures.resource.price.vo.Series;
import fwj.futures.resource.prod.buss.ProdBuss;
import fwj.futures.resource.prod.entity.Futures;

/**
 * 对冲套利相关WEB服务
 * 
 * @author Administrator
 *
 */
@RestController()
@RequestMapping("/hedging")
public class HedgingCtrl {

	@Autowired
	private HedgingBuss hedgingBuss;

	@Autowired
	private HedgingContractBuss hedgingContractBuss;

	@Autowired
	private HedgingExperimentBuss hedgingExperimentBuss;

	@Autowired
	private ProdBuss productBuss;

	@Autowired
	private HedgingProdContractBuss hedgingProdContractBuss;

	@Autowired
	private ContractDailyPriceBuss conPriceBuss;

	@Autowired
	private KLineBuss kLineBuss;

	@RequestMapping(value = "/contracts", method = RequestMethod.GET)
	public List<HedgingContractContainer> queryHedgingContractByCode(@RequestParam("code") String prodCode) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date endDt = df.parse(df.format(new Date()));
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDt);
			cal.add(Calendar.YEAR, -3);
			Date startDt = cal.getTime();
			return hedgingContractBuss.queryHedgingContractByCode(startDt, endDt, prodCode);
		} catch (ParseException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@RequestMapping(value = "/contracts-basic", method = RequestMethod.GET)
	public List<HedgingContract> queryHedgingContractBasic(@RequestParam("code") String code) {
		return hedgingContractBuss.queryHedgingContractBasic(code);
	}

	@RequestMapping(value = "/prod-experiments", method = RequestMethod.GET)
	public List<HedgingExperimentView> queryProdExperiments() {
		return hedgingExperimentBuss.queryProdExperiments(new BigDecimal("0.8"));
	}

	@RequestMapping(value = "/prod-experiment", method = RequestMethod.GET)
	public HedgingExperimentMonitor monitorProdExperiment(@RequestParam("id") Integer id) {
		return hedgingExperimentBuss.monitorProdExperiment(id);
	}

	@RequestMapping(value = "/prod-compare", method = RequestMethod.GET)
	public List<Series> compareProd(@RequestParam("code1") String code1, @RequestParam("code2") String code2) {
		Futures f1 = productBuss.queryFuturesByCode(code1);
		Futures f2 = productBuss.queryFuturesByCode(code2);
		return hedgingBuss.compareProd(f1, f2);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/contract-compare", method = RequestMethod.GET)
	public List<Series> compareContract(@RequestParam("contract1") String contract1,
			@RequestParam("contract2") String contract2) {
		String code1 = contract1.substring(0, contract1.length() - 4);
		int month1 = Integer.parseInt(contract1.substring(contract1.length() - 2));
		String code2 = contract2.substring(0, contract2.length() - 4);
		int month2 = Integer.parseInt(contract2.substring(contract2.length() - 2));
		List<ContractKLine> kLine1 = conPriceBuss.queryByCode(code1, month1);
		List<ContractKLine> kLine2 = conPriceBuss.queryByCode(code2, month2);

		String name1 = kLine1.get(0).getName();
		String name2 = kLine2.get(0).getName();
		Formula formula = Formula.create().putConstant(BigDecimal.ZERO).putMultinomial(name1, "1").putMultinomial(name2,
				"-1");
		Series series0 = kLineBuss.calculateSeries(name1 + "-" + name2, formula, kLine1, kLine2);
		Series series1 = kLineBuss.transform2Series(kLine1);
		Series series2 = kLineBuss.transform2Series(kLine2);

		return Arrays.asList(series1, series2, series0);
	}

	@RequestMapping(value = "/list-hedging-contract", method = RequestMethod.GET)
	public List<HedgingProdContract> listHedgingProdContract() {
		return hedgingProdContractBuss.listAll();
	}

	@RequestMapping(value = "/get-hedging-contract-series", method = RequestMethod.GET)
	public Series getHedgingProdContractSeries(Integer id,
			@RequestParam(value = "range", defaultValue = "18") Integer range) {
		Calendar cal = Calendar.getInstance();
		Date end = cal.getTime();
		cal.add(Calendar.MONTH, -range);
		Date start = cal.getTime();
		return hedgingProdContractBuss.getHedgingSeries(id, start, end);
	}
}

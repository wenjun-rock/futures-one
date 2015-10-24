package fwj.futures.resource.hedging.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.buss.HedgingBuss;
import fwj.futures.resource.buss.HedgingContractBuss;
import fwj.futures.resource.buss.HedgingExperimentBuss;
import fwj.futures.resource.hedging.entity.HedgingContract;
import fwj.futures.resource.hedging.vo.HedgingContractContainer;
import fwj.futures.resource.hedging.vo.HedgingExperimentMonitor;
import fwj.futures.resource.hedging.vo.HedgingExperimentView;
import fwj.futures.resource.hedging.vo.HedgingMonitor;
import fwj.futures.resource.hedging.vo.HedgingView;
import fwj.futures.resource.price.vo.Series;
import fwj.futures.resource.prod.buss.ProductBuss;
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
	private ProductBuss productBuss;

	// @RequestMapping(value = "/realtime/{id}", method = RequestMethod.GET)
	// public HedgingMonitor monitorRealtime(@PathVariable("id") Integer id) {
	// Hedging hedging = hedgingBuss.getById(id);
	// List<UnitDataGroup> unitDataGroupList = realTimePriceBuss.queryAllAsc();
	// List<Price> prices =
	// hedgingBuss.calculateUnitData(hedging.getExpression(),
	// unitDataGroupList);
	// return null;
	// // return new HedgingMonitor(hedging, prices);
	// }
	//
	// @RequestMapping(value = "/daily/{id}", method = RequestMethod.GET)
	// public HedgingMonitor monitorDaily(@PathVariable("id") Integer id) {
	// Hedging hedging = hedgingBuss.getById(id);
	// List<KLineGroup> groupList = kLineBuss.queryAllGroup();
	// List<Price> prices = hedgingBuss.calculateKLine(hedging.getExpression(),
	// groupList);
	// return null;
	//// return new HedgingMonitor(hedging, prices);
	// }

	@RequestMapping(value = "/contracts", method = RequestMethod.GET)
	public List<HedgingContractContainer> queryHedgingContractByCode(@RequestParam("code") String code) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date endDt = df.parse(df.format(new Date()));
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDt);
			cal.add(Calendar.YEAR, -5);
			Date startDt = cal.getTime();
			return hedgingContractBuss.queryHedgingContractByCode(startDt, endDt, code);
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<HedgingView> queryHedging() {
		return hedgingBuss.queryHedging();
	}

	@RequestMapping(value = "/monitor", method = RequestMethod.GET)
	public HedgingMonitor monitorHedging(@RequestParam("id") Integer id) {
		return hedgingBuss.monitorHedging(id);
	}
}

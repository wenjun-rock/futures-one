package fwj.futures.resource.web.ctrl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.data.struct.Formula;
import fwj.futures.resource.buss.DailyPriceBuss;
import fwj.futures.resource.buss.HedgingBuss;
import fwj.futures.resource.buss.HedgingContractBuss;
import fwj.futures.resource.buss.RealTimePriceBuss;
import fwj.futures.resource.entity.hedging.Hedging;
import fwj.futures.resource.entity.hedging.HedgingContract;
import fwj.futures.resource.vo.HedgingContractContainer;
import fwj.futures.resource.vo.KLineGroup;
import fwj.futures.resource.vo.UnitDataGroup;
import fwj.futures.resource.web.vo.HedgingMonitor;
import fwj.futures.resource.web.vo.Price;

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
	private DailyPriceBuss kLineBuss;

	@Autowired
	private HedgingBuss hedgingBuss;

	@Autowired
	private HedgingContractBuss hedgingContractBuss;

	@Autowired
	private RealTimePriceBuss realTimePriceBuss;

	@RequestMapping(value = "/realtime/{id}", method = RequestMethod.GET)
	public HedgingMonitor monitorRealtime(@PathVariable("id") Integer id) {
		Hedging hedging = hedgingBuss.getById(id);
		Formula fomular = Formula.parse(hedging.getExpression());
		List<UnitDataGroup> unitDataGroupList = realTimePriceBuss.queryAllAsc();
		List<Price> prices = unitDataGroupList.stream()
				.map(group -> new Price(group.getDatetime(), hedgingBuss.calculate(fomular, group)))
				.filter(price -> price.getP() != null).collect(Collectors.toList());
		return new HedgingMonitor(hedging, prices);
	}

	@RequestMapping(value = "/daily/{id}", method = RequestMethod.GET)
	public HedgingMonitor monitorDaily(@PathVariable("id") Integer id) {
		Hedging hedging = hedgingBuss.getById(id);
		Formula fomular = Formula.parse(hedging.getExpression());
		List<KLineGroup> groupList = kLineBuss.queryAllGroup();
		List<Price> prices = groupList.stream()
				.map(group -> new Price(group.getDt(), hedgingBuss.calculate(fomular, group)))
				.filter(price -> price.getP() != null).collect(Collectors.toList());
		return new HedgingMonitor(hedging, prices);
	}

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

	@RequestMapping(value = "/contractBasic", method = RequestMethod.GET)
	public List<HedgingContract> queryHedgingContractBasic(@RequestParam("code") String code) {
		return hedgingContractBuss.queryHedgingContractBasic(code);
	}

}

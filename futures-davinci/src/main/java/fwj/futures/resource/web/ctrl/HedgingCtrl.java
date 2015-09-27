package fwj.futures.resource.web.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.data.struct.Formula;
import fwj.futures.resource.buss.DailyPriceBuss;
import fwj.futures.resource.buss.HedgingBuss;
import fwj.futures.resource.buss.RealTimePriceBuss;
import fwj.futures.resource.entity.hedging.Hedging;
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

}

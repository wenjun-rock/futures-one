package fwj.futures.resource.web.ctrl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import fwj.futures.resource.web.vo.Series;

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
	public List<Series> monitorRealtime(@PathVariable("id") Integer id) {
		Hedging hedging = hedgingBuss.getById(id);
		Formula fomular = Formula.parse(hedging.getExpression());

		List<UnitDataGroup> unitDataGroupList = realTimePriceBuss.queryAllAsc();
		List<Object[]> data = unitDataGroupList.stream().map(unitDataGroup -> {
			BigDecimal result = hedgingBuss.calculate(fomular, unitDataGroup);
			if (result == null) {
				return new Object[0];
			}
			try {
				long time = unitDataGroup.getDatetime().getTime();
				return new Object[] { time, result };
			} catch (Exception e) {
				return new Object[0];
			}
		}).filter(ele -> ele.length > 0).collect(Collectors.toList());
		return new HedgingMonitor(hedging, data.toArray(new Object[0][2])).toSeries();
	}

	@RequestMapping(value = "/daily/{id}", method = RequestMethod.GET)
	public List<Series> monitorDaily(@PathVariable("id") Integer id) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Hedging hedging = hedgingBuss.getById(id);
		Formula fomular = Formula.parse(hedging.getExpression());

		List<KLineGroup> groupList = kLineBuss.queryAllGroup();
		List<Object[]> data = groupList.stream().map(group -> {
			BigDecimal result = hedgingBuss.calculate(fomular, group);
			if (result == null) {
				return new Object[0];
			}
			try {
				long time = df.parse(group.getDt()).getTime();
				return new Object[] { time, result };
			} catch (Exception e) {
				return new Object[0];
			}
		}).filter(ele -> ele.length > 0).collect(Collectors.toList());
		return new HedgingMonitor(hedging, data.toArray(new Object[0][2])).toSeries();
	}

}
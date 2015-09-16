package fwj.futures.resource.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.data.struct.Formula;
import fwj.futures.data.struct.Formula.Multinomial;
import fwj.futures.resource.buss.KLineBuss;
import fwj.futures.resource.buss.KLineBuss.KLineDtCodeGroup;
import fwj.futures.resource.entity.Hedging;
import fwj.futures.resource.repository.HedgingRepository;
import fwj.futures.resource.web.vo.HedgingMonitor;
import fwj.futures.resource.web.vo.Series;
import fwj.futures.task.RealtimeHolder;
import fwj.futures.task.RealtimeHolder.UnitData;
import fwj.futures.task.RealtimeHolder.UnitDataGroup;

/**
 * 对冲套利相关WEB服务
 * 
 * @author Administrator
 *
 */
@RestController()
@RequestMapping("/web/hedging")
public class HedgingController {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private KLineBuss kLineBuss;

	@Autowired
	private HedgingRepository hedgingRepo;

	@Autowired
	private RealtimeHolder realtimeHolder;

	@RequestMapping("/realtime/{id}")
	public List<Series> monitorRealtime(@PathVariable("id") Integer id) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		Hedging hedging = hedgingRepo.findOne(id);
		Formula fomular = Formula.parse(hedging.getExpression());

		List<UnitDataGroup> unitDataGroupList = realtimeHolder.getRealtime();
		List<Object[]> data = unitDataGroupList.stream().map(unitDataGroup -> {
			Map<String, BigDecimal> map = unitDataGroup.getUnitDataList().stream()
					.collect(Collectors.toMap(UnitData::getCode, UnitData::getPrice));
			BigDecimal result = fomular.getConstant();
			for (Multinomial multinomial : fomular.getMultinomials()) {
				if (map.get(multinomial.getCode()) == null) {
					return new Object[0];
				} else {
					result = result.add(multinomial.getCoefficient().multiply(map.get(multinomial.getCode())));
				}
			}
			long time = 0;
			try {
				time = df.parse(unitDataGroup.getDatetime()).getTime();
			} catch (Exception e) {
				log.error("", e);
			}
			return new Object[] { time, result };
		}).filter(ele -> ele.length > 0).collect(Collectors.toList());
		return new HedgingMonitor(hedging, data.toArray(new Object[0][2])).toSeries();
	}

	@RequestMapping("/daily/{id}")
	public List<Series> monitorDaily(@PathVariable("id") Integer id) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		Hedging hedging = hedgingRepo.findOne(id);
		Formula fomular = Formula.parse(hedging.getExpression());

		List<String> codeList = fomular.getMultinomials().stream().map(Multinomial::getCode)
				.collect(Collectors.toList());
		List<KLineDtCodeGroup> groupList = kLineBuss.queryEndPrice(codeList);
		List<Object[]> data = groupList.stream().filter(group -> group.getkLineMap().size() == codeList.size())
				.map(group -> {
					BigDecimal result = fomular.getConstant();
					for (Multinomial multinomial : fomular.getMultinomials()) {
						result = result.add(multinomial.getCoefficient()
								.multiply(group.getkLineMap().get(multinomial.getCode()).getEndPrice()));
					}
					long time = 0;
					try {
						time = df.parse(group.getDt()).getTime();
					} catch (Exception e) {
					}
					return new Object[] { time, result };
				}).collect(Collectors.toList());
		return new HedgingMonitor(hedging, data.toArray(new Object[0][2])).toSeries();
	}

}

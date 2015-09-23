package fwj.futures.resource.buss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.task.RealtimeHolder;
import fwj.futures.resource.vo.UnitData;
import fwj.futures.resource.vo.UnitDataGroup;
import fwj.futures.resource.web.vo.Series;

@Component
public class RealTimePriceBuss {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private ProductBuss productBuss;

	@Autowired
	private RealtimeHolder realtimeHolder;

	public List<UnitDataGroup> queryAllAsc() {
		return realtimeHolder.getRealtime();
	}

	public List<UnitData> queryDescByCode(String code) {
		List<UnitData> unitDatas = realtimeHolder.getRealtime().stream().map(group -> group.getUnitData(code))
				.filter(unit -> unit != UnitData.DUMMY).collect(Collectors.toList());
		Collections.reverse(unitDatas);
		return unitDatas;
	}

	public List<UnitData> queryAscByCode(String code) {
		return realtimeHolder.getRealtime().stream().map(group -> group.getUnitData(code))
				.filter(unit -> unit != UnitData.DUMMY).collect(Collectors.toList());
	}

	public Series querySeriesByCode(String code) {
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return Series.EMPTY;
		} else {
			List<Object[]> data = this.queryAscByCode(code).stream()
					.map(unit -> new Object[] { unit.getDatetime().getTime(), unit.getPrice() })
					.collect(Collectors.toList());
			return new Series(prod.getCode(), prod.getName(), data.toArray(new Object[0][2]));
		}
	}

	public Series queryLastDaySeriesByCode(String code) {
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return Series.EMPTY;
		}
		try {
			List<UnitData> fullList = this.queryDescByCode(code);
			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] datetime = df.format(now).split(" ");
			Date startDt = df.parse(datetime[0] + " 21:00:00");
			if (startDt.after(now)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDt);
				cal.add(Calendar.DATE, -1);
				startDt = cal.getTime();
			}
			int endIndex = 0;
			while (endIndex < (fullList.size() -1 )) {
				Date tmp = fullList.get(endIndex).getDatetime();
				if (tmp.before(startDt)) {
					break;
				}
				endIndex++;
			}
			List<Object[]> data = fullList.subList(0, endIndex + 1).stream().sorted()
					.map(unit -> new Object[] { unit.getDatetime().getTime(), unit.getPrice() })
					.collect(Collectors.toList());
			return new Series(prod.getCode(), prod.getName(), data.toArray(new Object[0][2]));
		} catch (Exception e) {
			log.error("", e);
			return Series.EMPTY;
		}
	}

	public UnitDataGroup queryLatest() {
		return realtimeHolder.getLatest();
	}

}

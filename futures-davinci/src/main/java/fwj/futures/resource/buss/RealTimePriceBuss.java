package fwj.futures.resource.buss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.price.vo.Price;
import fwj.futures.resource.price.vo.Series;
import fwj.futures.resource.price.vo.UnitData;
import fwj.futures.resource.price.vo.UnitDataGroup;
import fwj.futures.resource.prod.buss.ProductBuss;
import fwj.futures.resource.prod.entity.Futures;
import fwj.futures.resource.task.RealtimeHolder;

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
			List<Price> prices = this.queryAscByCode(code).stream()
					.map(unit -> new Price(unit.getDatetime(), unit.getPrice())).collect(Collectors.toList());
			return new Series(prod.getCode(), prod.getName(), prices);
		}
	}

	public Series queryLastDaySeriesByCode(String code) {
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return Series.EMPTY;
		}
		try {
			List<UnitData> fullList = this.queryDescByCode(code);
			Date latest = fullList.get(0).getDatetime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] datetime = df.format(latest).split(" ");
			Date startDt = df.parse(datetime[0] + " 21:00:00");
			if (startDt.after(latest)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDt);
				cal.add(Calendar.DATE, -1);
				startDt = cal.getTime();
			}
			int endIndex = 0;
			while (endIndex < (fullList.size() - 1)) {
				Date tmp = fullList.get(endIndex).getDatetime();
				if (tmp.before(startDt)) {
					break;
				}
				endIndex++;
			}
			List<Price> prices = fullList.subList(0, endIndex + 1).stream().sorted()
					.map(unit -> new Price(unit.getDatetime(), unit.getPrice())).collect(Collectors.toList());
			return new Series(prod.getCode(), prod.getName(), prices);
		} catch (Exception e) {
			log.error("", e);
			return Series.EMPTY;
		}
	}

	public UnitDataGroup queryLatest() {
		return realtimeHolder.getLatest();
	}

	public UnitData queryLatest(String code) {
		List<UnitData> list = this.queryAscByCode(code);
		if (list.size() > 0) {
			return list.get(list.size() - 1);
		} else {
			return null;
		}
	}
	
	public UnitData queryLatestContract(String conCode) {
		Map<String, UnitData> map =  realtimeHolder.getLatestContractPrice();
		return map == null ? null : map.get(conCode);
	}
	
	public void registerContract(String code, String conCode) {
		realtimeHolder.registerContract(code, conCode);
	}
	
}

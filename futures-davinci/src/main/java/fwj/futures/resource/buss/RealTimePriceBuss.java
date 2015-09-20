package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.Futures;
import fwj.futures.resource.task.RealtimeHolder;
import fwj.futures.resource.vo.UnitData;
import fwj.futures.resource.vo.UnitDataGroup;
import fwj.futures.resource.web.vo.Series;

@Component
public class RealTimePriceBuss {
	
	@Autowired
	private ProductBuss productBuss;

	@Autowired
	private RealtimeHolder realtimeHolder;
	
	public List<UnitDataGroup> queryAllAsc() {
		return realtimeHolder.getRealtime();
	}
	
	public List<UnitData> queryDescByCode(String code) {
		List<UnitData> unitDatas = realtimeHolder.getRealtime().stream()
				.flatMap(group -> group.getUnitDataList().stream()).filter(unit -> code.equals(unit.getCode()))
				.sorted().collect(Collectors.toList());
		Collections.reverse(unitDatas);
		return unitDatas;
	}
	
	public List<UnitData> queryAscByCode(String code) {
		List<UnitData> unitDatas = realtimeHolder.getRealtime().stream()
				.flatMap(group -> group.getUnitDataList().stream()).filter(unit -> code.equals(unit.getCode()))
				.sorted().collect(Collectors.toList());
		return unitDatas;
	}
	
	public Series querySeriesByCode(String code) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Futures prod = productBuss.queryFuturesByCode(code);
		if (prod == null) {
			return Series.EMPTY;
		} else {
			List<Object[]> data = this.queryAscByCode(code).stream().map(unit -> {
				long time = 0;
				try {
					time = df.parse(unit.getDatetime()).getTime();
				} catch (Exception e) {
				}
				BigDecimal price = unit.getPrice();
				return new Object[] { time, price };
			}).collect(Collectors.toList());
			return new Series(prod.getName(), data.toArray(new Object[0][2]));
		}
	}

}

package fwj.futures.resource.buss;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.task.RealtimeHolder;
import fwj.futures.resource.task.RealtimeHolder.UnitData;

@Component
public class RealTimePriceBuss {

	@Autowired
	private RealtimeHolder realtimeHolder;

	public static class KLineDtCodeGroup implements Comparable<KLineDtCodeGroup> {

		private String dt;
		private Map<String, KLine> kLineMap;

		public KLineDtCodeGroup(String dt, Map<String, KLine> kLineMap) {
			this.dt = dt;
			this.kLineMap = kLineMap;
		}

		public String getDt() {
			return dt;
		}

		public Map<String, KLine> getkLineMap() {
			return kLineMap;
		}

		@Override
		public int compareTo(KLineDtCodeGroup that) {
			return this.dt.compareTo(that.dt);
		}
	}

	public List<UnitData> queryReverseByCode(String code) {
		List<UnitData> unitData = realtimeHolder.getRealtime().stream()
				.flatMap(group -> group.getUnitDataList().stream()).filter(unit -> code.equals(unit.getCode()))
				.collect(Collectors.toList());
		Collections.reverse(unitData);
		return unitData;
	}

}

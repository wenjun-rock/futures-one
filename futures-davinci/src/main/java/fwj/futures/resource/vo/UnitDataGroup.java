package fwj.futures.resource.vo;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class UnitDataGroup implements Comparable<UnitDataGroup> {

	private Date datetime;
	private Map<String, UnitData> unitDataMap;

	public UnitDataGroup(Date datetime, Map<String, UnitData> unitDataMap) {
		this.datetime = datetime;
		this.unitDataMap = Collections.unmodifiableMap(unitDataMap);
	}

	public Date getDatetime() {
		return datetime;
	}

	public Map<String, UnitData> getUnitDataMap() {
		return unitDataMap;
	}

	public UnitData getUnitData(String code) {
		UnitData result = unitDataMap.get(code);
		return result == null ? UnitData.DUMMY : result;
	}

	@Override
	public int compareTo(UnitDataGroup that) {
		return this.datetime.compareTo(that.datetime);
	}

}
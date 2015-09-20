package fwj.futures.resource.vo;

import java.util.Collections;
import java.util.List;

public class UnitDataGroup implements Comparable<UnitDataGroup> {

	private String datetime;
	private List<UnitData> unitDataList;

	public UnitDataGroup(String datetime, List<UnitData> unitDataList) {
		Collections.sort(unitDataList);
		this.datetime = datetime;
		this.unitDataList = Collections.unmodifiableList(unitDataList);
	}

	public String getDatetime() {
		return datetime;
	}

	public List<UnitData> getUnitDataList() {
		return unitDataList;
	}

	public UnitData getUnitData(String code) {
		if (unitDataList == null) {
			return null;
		}
		return unitDataList.stream().filter(unitData -> unitData.getCode().equals(code)).findAny().orElse(null);
	}

	@Override
	public int compareTo(UnitDataGroup that) {
		return this.datetime.compareTo(that.datetime);
	}

}
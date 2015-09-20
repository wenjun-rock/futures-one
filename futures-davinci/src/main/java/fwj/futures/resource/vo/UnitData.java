package fwj.futures.resource.vo;

import java.math.BigDecimal;

public class UnitData implements Comparable<UnitData> {

	private String datetime;
	private String code;
	private BigDecimal price;

	public UnitData(String datetime, String code, BigDecimal price) {
		this.datetime = datetime;
		this.code = code;
		this.price = price;
	}

	public String getDatetime() {
		return datetime;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public int compareTo(UnitData that) {
		int cp = this.datetime.compareTo(that.datetime);
		if (cp == 0) {
			cp = this.code.compareTo(that.code);
		}
		return cp;
	}

}
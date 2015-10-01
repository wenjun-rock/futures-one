package fwj.futures.resource.vo;

import java.math.BigDecimal;
import java.util.Date;

public class HedgingContractDiff {

	private BigDecimal d;
	private Integer vol1;
	private Integer vol2;
	private String y;
	private Date dt;

	public HedgingContractDiff(BigDecimal diff, Integer tradeVol1, Integer tradeVol2, String year, Date dt) {
		this.d = diff;
		this.dt = dt;
		this.y = year;
		this.vol1 = tradeVol1;
		this.vol2 = tradeVol2;
	}

	public BigDecimal getD() {
		return d;
	}

	public Integer getVol1() {
		return vol1;
	}

	public Integer getVol2() {
		return vol2;
	}

	public String getY() {
		return y;
	}

	public Date getDt() {
		return dt;
	}

}

package fwj.futures.resource.hedging.vo;

import java.math.BigDecimal;
import java.util.Date;

import fwj.futures.resource.util.TimeLineable;

public class HedgingContractDiff implements TimeLineable{

	private BigDecimal d;
	private Integer vol1;
	private Integer vol2;
	private Date dt;

	public HedgingContractDiff(BigDecimal diff, Integer tradeVol1, Integer tradeVol2, Date dt) {
		this.d = diff;
		this.dt = dt;
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

	public Date getDt() {
		return dt;
	}

}

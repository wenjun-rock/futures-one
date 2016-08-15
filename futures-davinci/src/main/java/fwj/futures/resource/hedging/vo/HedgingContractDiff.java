package fwj.futures.resource.hedging.vo;

import java.math.BigDecimal;
import java.util.Date;

import fwj.futures.resource.util.TimeLineable;

public class HedgingContractDiff implements TimeLineable {

	private BigDecimal p;
	private Integer vol1;
	private Integer vol2;
	private Date d;

	public HedgingContractDiff(BigDecimal diff, Integer tradeVol1, Integer tradeVol2, Date d) {
		this.p = diff;
		this.d = d;
		this.vol1 = tradeVol1;
		this.vol2 = tradeVol2;
	}

	public BigDecimal getP() {
		return p;
	}

	public Integer getVol1() {
		return vol1;
	}

	public Integer getVol2() {
		return vol2;
	}

	@Override
	public Date getD() {
		return d;
	}



}

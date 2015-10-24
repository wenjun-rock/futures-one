package fwj.futures.resource.hedging.vo;

import java.math.BigDecimal;

public class HedgingView {

	private Integer id;
	private String name;
	private String formula;
	private BigDecimal complete;
	private BigDecimal diffKline;
	private BigDecimal diffRealtime;
	private Integer up;
	private Integer down;
	private Integer mid;
	private Integer q1;
	private Integer q3;

	public HedgingView(Integer id, String name, String formula, BigDecimal complete, BigDecimal diffKline,
			BigDecimal diffRealtime, Integer up, Integer down, Integer mid, Integer q1, Integer q3) {
		this.id = id;
		this.name = name;
		this.formula = formula;
		this.complete = complete;
		this.diffKline = diffKline;
		this.diffRealtime = diffRealtime;
		this.up = up;
		this.down = down;
		this.mid = mid;
		this.q1 = q1;
		this.q3 = q3;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFormula() {
		return formula;
	}

	public BigDecimal getComplete() {
		return complete;
	}

	public BigDecimal getDiffKline() {
		return diffKline;
	}

	public BigDecimal getDiffRealtime() {
		return diffRealtime;
	}

	public Integer getUp() {
		return up;
	}

	public Integer getDown() {
		return down;
	}

	public Integer getMid() {
		return mid;
	}

	public Integer getQ1() {
		return q1;
	}

	public Integer getQ3() {
		return q3;
	}

}

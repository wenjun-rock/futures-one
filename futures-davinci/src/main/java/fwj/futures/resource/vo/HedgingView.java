package fwj.futures.resource.vo;

import java.math.BigDecimal;

public class HedgingView {

	private Integer id;
	private String name;
	private String formula;
	private BigDecimal stdError;
	private BigDecimal diffKline;
	private BigDecimal diffRealtime;

	public HedgingView(Integer id, String name, String formula, BigDecimal stdError, BigDecimal diffKline,
			BigDecimal diffRealtime) {
		this.id = id;
		this.name = name;
		this.formula = formula;
		this.stdError = stdError;
		this.diffKline = diffKline;
		this.diffRealtime = diffRealtime;
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

	public BigDecimal getStdError() {
		return stdError;
	}

	public BigDecimal getDiffKline() {
		return diffKline;
	}

	public BigDecimal getDiffRealtime() {
		return diffRealtime;
	}

}

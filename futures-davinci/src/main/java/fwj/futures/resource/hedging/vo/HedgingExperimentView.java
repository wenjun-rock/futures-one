package fwj.futures.resource.hedging.vo;

import java.math.BigDecimal;

public class HedgingExperimentView {

	private Integer id;
	private String name;
	private String startDt;
	private String endDt;
	private BigDecimal rsquared;
	private String formula1;
	private String formula2;
	private BigDecimal stdError1;
	private BigDecimal stdError2;

	public HedgingExperimentView(Integer id, String name, String startDt, String endDt, BigDecimal rsquared,
			String formula1, String formula2, BigDecimal stdError1, BigDecimal stdError2) {
		this.id = id;
		this.name = name;
		this.startDt = startDt;
		this.endDt = endDt;
		this.rsquared = rsquared;
		this.formula1 = formula1;
		this.formula2 = formula2;
		this.stdError1 = stdError1;
		this.stdError2 = stdError2;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStartDt() {
		return startDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public BigDecimal getRsquared() {
		return rsquared;
	}

	public String getFormula1() {
		return formula1;
	}

	public String getFormula2() {
		return formula2;
	}

	public BigDecimal getStdError1() {
		return stdError1;
	}

	public BigDecimal getStdError2() {
		return stdError2;
	}

}

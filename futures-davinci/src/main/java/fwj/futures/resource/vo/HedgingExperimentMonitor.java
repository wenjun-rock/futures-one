package fwj.futures.resource.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fwj.futures.resource.web.vo.Price;

public class HedgingExperimentMonitor {

	private Integer id;
	private String name;
	private Date startDt;
	private Date endDt;
	private String formula1;
	private String formula2;
	private BigDecimal stdError1;
	private BigDecimal stdError2;
	private List<Price> prices1;
	private List<Price> prices2;

	public HedgingExperimentMonitor(Integer id, String name, Date startDt, Date endDt, String formula1, String formula2,
			BigDecimal stdError1, BigDecimal stdError2, List<Price> prices1, List<Price> prices2) {
		this.id = id;
		this.name = name;
		this.startDt = startDt;
		this.endDt = endDt;
		this.formula1 = formula1;
		this.formula2 = formula2;
		this.stdError1 = stdError1;
		this.stdError2 = stdError2;
		this.prices1 = prices1;
		this.prices2 = prices2;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getStartDt() {
		return startDt;
	}

	public Date getEndDt() {
		return endDt;
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

	public List<Price> getPrices1() {
		return prices1;
	}

	public List<Price> getPrices2() {
		return prices2;
	}

}

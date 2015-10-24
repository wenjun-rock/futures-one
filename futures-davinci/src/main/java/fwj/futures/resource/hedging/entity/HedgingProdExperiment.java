package fwj.futures.resource.hedging.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
public class HedgingProdExperiment extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 9159028314031656955L;

	@ManyToOne()
	@JoinColumn(name = "batch_id", nullable = false)
	private HedgingProdBatch hedgingProdBatch;

	private String name;

	@Column(length = 400)
	private String expression1;

	@Column(length = 400)
	private String expression2;

	private BigDecimal stdError1;

	private BigDecimal stdError2;

	private BigDecimal rsquared;

	public HedgingProdBatch getHedgingProdBatch() {
		return hedgingProdBatch;
	}

	public String getName() {
		return name;
	}

	public String getExpression1() {
		return expression1;
	}

	public String getExpression2() {
		return expression2;
	}

	public BigDecimal getStdError1() {
		return stdError1;
	}

	public BigDecimal getStdError2() {
		return stdError2;
	}

	public BigDecimal getRsquared() {
		return rsquared;
	}

	public void setHedgingProdBatch(HedgingProdBatch hedgingProdBatch) {
		this.hedgingProdBatch = hedgingProdBatch;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExpression1(String expression1) {
		this.expression1 = expression1;
	}

	public void setExpression2(String expression2) {
		this.expression2 = expression2;
	}

	public void setStdError1(BigDecimal stdError1) {
		this.stdError1 = stdError1;
	}

	public void setStdError2(BigDecimal stdError2) {
		this.stdError2 = stdError2;
	}

	public void setRsquared(BigDecimal rsquared) {
		this.rsquared = rsquared;
	}

}

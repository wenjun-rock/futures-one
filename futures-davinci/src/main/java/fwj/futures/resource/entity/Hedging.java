package fwj.futures.resource.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
public class Hedging extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 7666569451056780578L;

	@Column(length = 10)
	private String name;

	@Column(length = 50)
	private String description;

	@Column(length = 400)
	private String expression;

	@Column(precision = 10, scale = 2)
	private BigDecimal upLimit;

	@Column(precision = 10, scale = 2)
	private BigDecimal downLimit;
	
	@Column(length = 10)
	private String modelStartDt;
	
	@Column(length = 10)
	private String modelEndDt;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getExpression() {
		return expression;
	}

	public BigDecimal getUpLimit() {
		return upLimit;
	}

	public BigDecimal getDownLimit() {
		return downLimit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String description) {
		this.description = description;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setUpLimit(BigDecimal upLimit) {
		this.upLimit = upLimit;
	}

	public void setDownLimit(BigDecimal downLimit) {
		this.downLimit = downLimit;
	}

	public String getModelStartDt() {
		return modelStartDt;
	}

	public String getModelEndDt() {
		return modelEndDt;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setModelStartDt(String modelStartDt) {
		this.modelStartDt = modelStartDt;
	}

	public void setModelEndDt(String modelEndDt) {
		this.modelEndDt = modelEndDt;
	}


}

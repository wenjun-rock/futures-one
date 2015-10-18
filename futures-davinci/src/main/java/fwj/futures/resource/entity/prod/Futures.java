package fwj.futures.resource.entity.prod;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
@Table(name="prod_futures")
public class Futures extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = 6047007431809346528L;

	@Column(length = 4)
	private String code;

	@Column(length = 10)
	private String name;

	@Column(length = 4)
	private String exchange;

	@Column(columnDefinition = "TINYINT")
	private int active;

	private int unit;

	@Column(length = 10)
	private String unitDesc;
	
	@Column(precision = 4, scale = 4)
	private BigDecimal marginRate;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getExchange() {
		return exchange;
	}

	public int getUnit() {
		return unit;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public BigDecimal getMarginRate() {
		return marginRate;
	}

	public void setMarginRate(BigDecimal marginRate) {
		this.marginRate = marginRate;
	}

}

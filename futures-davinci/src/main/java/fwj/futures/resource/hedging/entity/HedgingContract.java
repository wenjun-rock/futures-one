package fwj.futures.resource.hedging.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
public class HedgingContract extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -1938039962221602205L;

	@Column(length = 10)
	private String name;
	
	@Column(length = 2)
	private String code;

	@Column(columnDefinition = "TINYINT")
	private int contractMonth1;

	@Column(columnDefinition = "TINYINT")
	private int contractMonth2;

	public String getCode() {
		return code;
	}

	public int getContractMonth1() {
		return contractMonth1;
	}

	public int getContractMonth2() {
		return contractMonth2;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setContractMonth1(int contractMonth1) {
		this.contractMonth1 = contractMonth1;
	}

	public void setContractMonth2(int contractMonth2) {
		this.contractMonth2 = contractMonth2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

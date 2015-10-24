package fwj.futures.resource.prod.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
@Table(name="prod_main_contract")
public class ProdMainCon extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -2174994813490377497L;

	@Column(length = 2)
	private String code;

	@Column(columnDefinition = "TINYINT")
	private int month;

	public String getCode() {
		return code;
	}

	public int getMonth() {
		return month;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMonth(int month) {
		this.month = month;
	}	
}

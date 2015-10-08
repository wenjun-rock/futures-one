package fwj.futures.resource.entity.prod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity()
@Table(name = "prod_global_futures")
public class GlobalFutures extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -6435854451264021832L;

	@Column(length = 4)
	private String code;

	@Column(length = 4)
	private String globalCode;

	@Column(length = 10)
	private String globalName;

	@Column(length = 4)
	private String exchange;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGlobalCode() {
		return globalCode;
	}

	public void setGlobalCode(String globalCode) {
		this.globalCode = globalCode;
	}

	public String getGlobalName() {
		return globalName;
	}

	public void setGlobalName(String globalName) {
		this.globalName = globalName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

}

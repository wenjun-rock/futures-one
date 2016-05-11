package fwj.futures.resource.trade.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class TradeGroup extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -7230382409707617510L;

	@Column(length = 50)
	private String name;

	@Column(length = 255)
	private String comment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}

package fwj.futures.resource.trade.entity;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class TradeGroupOrder extends AbstractPersistable<Integer> {

	private static final long serialVersionUID = -7230382409707617510L;

	private Integer groupId;
	private Integer orderId;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}

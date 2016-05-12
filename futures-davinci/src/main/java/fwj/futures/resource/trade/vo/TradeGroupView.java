package fwj.futures.resource.trade.vo;

import java.util.List;

import fwj.futures.resource.trade.entity.TradeOrder;

public class TradeGroupView {

	private Integer id;
	private String name;
	private String comment;
	
	private List<TradeOrder> orders;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public List<TradeOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<TradeOrder> orders) {
		this.orders = orders;
	}

}

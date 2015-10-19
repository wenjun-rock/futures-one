package fwj.futures.resource.trade.vo;

import java.util.List;

public class TradeDetail {

	private TradeView trade;
	private List<BalanceView> balanceList;
	private List<ActionView> actionList;

	public TradeDetail(TradeView trade, List<BalanceView> balanceList, List<ActionView> actionList) {
		this.trade = trade;
		this.balanceList = balanceList;
		this.actionList = actionList;
	}

	public TradeView getTrade() {
		return trade;
	}

	public List<BalanceView> getBalanceList() {
		return balanceList;
	}

	public List<ActionView> getActionList() {
		return actionList;
	}

}

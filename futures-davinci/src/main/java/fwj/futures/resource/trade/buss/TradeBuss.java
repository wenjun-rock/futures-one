package fwj.futures.resource.trade.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.buss.ProductBuss;
import fwj.futures.resource.buss.RealTimePriceBuss;
import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.trade.entity.Trade;
import fwj.futures.resource.trade.entity.TradeAction;
import fwj.futures.resource.trade.entity.TradeBalance;
import fwj.futures.resource.trade.repos.TradeActionRepos;
import fwj.futures.resource.trade.repos.TradeBalanceRepos;
import fwj.futures.resource.trade.repos.TradeRepos;
import fwj.futures.resource.trade.vo.ActionView;
import fwj.futures.resource.trade.vo.TradeView;
import fwj.futures.resource.vo.UnitData;

@Component
public class TradeBuss {

	@Autowired
	private TradeRepos tradeRepos;

	@Autowired
	private TradeBalanceRepos tradeBalanceRepos;

	@Autowired
	private TradeActionRepos tradeActionRepos;

	@Autowired
	private RealTimePriceBuss realTimePriceBuss;

	@Autowired
	private ProductBuss productBuss;

	public List<TradeView> listTrade() {
		return tradeRepos.findAll().stream().map(trade -> {
			TradeView view = new TradeView();
			view.setId(trade.getId());
			view.setName(trade.getName());
			view.setType(trade.getType());
			view.setStartDt(trade.getStartDt());
			view.setEndDt(trade.getEndDt());
			view.setVol(trade.getVol());
			view.setMargin(trade.getMargin());
			view.setMaxMargin(trade.getMaxMargin());
			view.setCompleteProfit(trade.getCompleteProfit());
			if (trade.getEndDt() == null) {
				int floatProfile = tradeBalanceRepos.findByTrade(trade).stream()
						.collect(Collectors.summingInt(balance -> {
					if (balance.getVol() == 0) {
						return 0;
					}
					UnitData unitData = realTimePriceBuss.queryLatestContract(balance.getConCode());
					Futures futures = productBuss.queryFuturesByCode(balance.getCode());
					int valuePerVol = unitData.getPrice().multiply(new BigDecimal(futures.getUnit())).intValue();
					int diff = valuePerVol * balance.getVol() - balance.getTotalCostValue();
					return balance.getType() == 1 ? diff : 0 - diff;
				}));
				view.setFloatProfit(floatProfile);
				view.setProfit(trade.getCompleteProfit() + floatProfile);
			} else {
				view.setFloatProfit(0);
				view.setProfit(trade.getCompleteProfit());
			}
			if (view.getMaxMargin() == 0) {
				view.setRoi(BigDecimal.ZERO);
			} else {
				view.setRoi(new BigDecimal(view.getProfit()).divide(new BigDecimal(view.getMaxMargin()), 4,
						RoundingMode.FLOOR));
			}
			return view;
		}).collect(Collectors.toList());
	}

	public TradeView addTrade(TradeView tradeView) {
		Trade trade = new Trade();
		trade.setName(tradeView.getName());
		trade.setType(tradeView.getType());
		trade.setMaxMargin(0);
		trade.setCompleteProfit(0);
		trade.setMargin(0);
		trade.setVol(0);
		Trade newTrade = tradeRepos.saveAndFlush(trade);
		tradeView.setId(newTrade.getId());
		tradeView.setMaxMargin(0);
		tradeView.setCompleteProfit(0);
		tradeView.setFloatProfit(0);
		tradeView.setProfit(0);
		return tradeView;
	}

	public ActionView addAction(ActionView actionView) {

		Integer tradeId = actionView.getTradeId();
		Trade trade = tradeRepos.findOne(tradeId);
		if (trade == null || trade.getEndDt() != null) {
			return null;
		}
		String conCode = actionView.getConCode();
		String code = conCode.substring(0, conCode.length() - 4);
		Futures futures = productBuss.queryFuturesByCode(code);
		if (futures == null) {
			return null;
		}

		TradeAction action = new TradeAction();
		action.setTrade(trade);
		action.setConCode(actionView.getConCode());
		action.setDt(actionView.getDt());
		action.setType(actionView.getType());
		action.setPrice(actionView.getPrice());
		action.setVol(actionView.getVol());
		List<TradeAction> actionList = tradeActionRepos.findByTradeAndConCodeOrderByDtAsc(trade, conCode);
		actionList.add(action);
		LinkedList<BigDecimal> unitList = new LinkedList<>();
		BigDecimal complete = BigDecimal.ZERO;
		for (TradeAction ele : actionList) {
			BigDecimal price = ele.getType() == 1 ? ele.getPrice().negate() : ele.getPrice();
			for (int i = 0; i < ele.getVol(); i++) {
				if (unitList.isEmpty()
						|| unitList.getFirst().compareTo(BigDecimal.ZERO) == price.compareTo(BigDecimal.ZERO)) {
					unitList.add(price);
				} else {
					BigDecimal rm = unitList.removeFirst();
					complete = complete.add(rm).add(price);
				}
			}
		}

		TradeBalance balance = tradeBalanceRepos.findByTradeAndConCode(trade, conCode);
		int preVol = balance == null ? 0 : balance.getVol();
		int preMargin = balance == null ? 0 : balance.getMargin();
		int preCompleteProfit = balance == null ? 0 : balance.getCompleteProfit();
		if (balance == null) {
			balance = new TradeBalance();
			balance.setTrade(trade);
			balance.setCode(code);
			balance.setConCode(conCode);
		}
		int completeProfit = complete.multiply(new BigDecimal(futures.getUnit())).intValue();
		int vol = unitList.size();
		int margin = 0;
		int totalCostValue = 0;
		BigDecimal avgCostPrice = BigDecimal.ZERO;
		if (vol > 0) {
			balance.setType(unitList.getFirst().compareTo(BigDecimal.ZERO) < 0 ? 1 : 2);
			totalCostValue = unitList.stream().collect(Collectors.summingInt(price -> {
				return price.abs().multiply(new BigDecimal(futures.getUnit())).intValue();
			}));
			avgCostPrice = new BigDecimal(totalCostValue).divide(new BigDecimal(futures.getUnit() * vol), 2,
					RoundingMode.FLOOR);
			margin = (int) (totalCostValue * futures.getMarginRate().doubleValue());
		}
		balance.setVol(vol);
		balance.setAvgCostPrice(avgCostPrice);
		balance.setTotalCostValue(totalCostValue);
		balance.setMargin(margin);
		balance.setCompleteProfit(completeProfit);

		if (trade.getStartDt() == null) {
			trade.setStartDt(actionView.getDt());
		}
		trade.setCompleteProfit(trade.getCompleteProfit() + completeProfit - preCompleteProfit);
		trade.setMargin(trade.getMargin() + margin - preMargin);
		if (trade.getMargin() > trade.getMaxMargin()) {
			trade.setMaxMargin(trade.getMargin());
		}
		trade.setVol(trade.getVol() + vol - preVol);
		if (trade.getVol() == 0) {
			trade.setEndDt(actionView.getDt());
		}

		// to DB
		TradeAction retAction = tradeActionRepos.saveAndFlush(action);
		tradeBalanceRepos.saveAndFlush(balance);
		tradeRepos.saveAndFlush(trade);

		// register contract
		realTimePriceBuss.registerContract(code, conCode);

		actionView.setId(retAction.getId());
		return actionView;
	}

}

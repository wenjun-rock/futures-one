package fwj.futures.resource.trade.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.trade.buss.TradeBuss;
import fwj.futures.resource.trade.vo.ActionView;
import fwj.futures.resource.trade.vo.TradeView;

@RestController()
@RequestMapping("/trade")
public class TradeCtrl {

	@Autowired
	private TradeBuss tradeBuss;

	@RequestMapping(value = "/list-trade", method = RequestMethod.GET)
	public List<TradeView> listTrade() {
		return tradeBuss.listTrade();
	}

	@RequestMapping(value = "/add-trade", method = RequestMethod.POST)
	public TradeView addTrade(@RequestBody TradeView body) {
		return tradeBuss.addTrade(body);
	}
	
	@RequestMapping(value = "/add-action", method = RequestMethod.POST)
	public ActionView addAction(@RequestBody ActionView body) {
		return tradeBuss.addAction(body);
	}

}

package fwj.futures.resource.trade.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fwj.futures.resource.trade.buss.TradeBuss;
import fwj.futures.resource.trade.buss.TradeOrderBuss;
import fwj.futures.resource.trade.entity.TradeOrder;
import fwj.futures.resource.trade.vo.ActionView;
import fwj.futures.resource.trade.vo.TradeDetail;
import fwj.futures.resource.trade.vo.TradeView;

@RestController()
@RequestMapping("/trade")
public class TradeCtrl {

	@Autowired
	private TradeBuss tradeBuss;
	
	@Autowired
	private TradeOrderBuss tradeOrderBuss;

	@RequestMapping(value = "/list-trade", method = RequestMethod.GET)
	public List<TradeView> listTrade() {
		return tradeBuss.listTrade();
	}
	
	@RequestMapping(value = "/detail-trade", method = RequestMethod.GET)
	public TradeDetail detailTrade(@RequestParam("id") Integer tradeId) {
		return tradeBuss.detailTrade(tradeId);
	}

	@RequestMapping(value = "/add-trade", method = RequestMethod.POST)
	public TradeView addTrade(@RequestBody TradeView body) {
		return tradeBuss.addTrade(body);
	}
	
	@RequestMapping(value = "/add-action", method = RequestMethod.POST)
	public ActionView addAction(@RequestBody ActionView body) {
		return tradeBuss.addAction(body);
	}
	
	@RequestMapping(value = "/upload-trade-order", method = RequestMethod.POST)
	public int uploadTradeOrder(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
            try {
            	return tradeOrderBuss.saveTradeOrderInExcel(file.getBytes());
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
		return 0;
	}
	
	@RequestMapping(value = "/list-trade-order", method = RequestMethod.GET)
	public List<TradeOrder> listTradeOrder() {
		return tradeOrderBuss.listTradeOrder();
	}

}

package fwj.futures.resource.trend.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.trend.buss.MovingAvgBuss;
import fwj.futures.resource.trend.entity.TrendMaMonitor;
import fwj.futures.resource.trend.vo.ProdMA;

@RestController()
@RequestMapping("/trend")
public class TrendCtrl {

	@Autowired
	private MovingAvgBuss movingAvgBuss;

	@RequestMapping(value = "/prod-ma", method = RequestMethod.GET)
	public ProdMA getProdMA(@RequestParam("code") String code,
			@RequestParam(value = "month", defaultValue = "-1") int month) {
		return movingAvgBuss.calProdMovingAverage(code, month);
	}

	@RequestMapping(value = "/contract-ma", method = RequestMethod.GET)
	public ProdMA getContractMA(@RequestParam("code") String code, @RequestParam("contract") int contract,
			@RequestParam(value = "month", defaultValue = "-1") int month) {
		return movingAvgBuss.calContractMovingAverage(code, contract, month);
	}

	@RequestMapping(value = "/monitor-trend-ma", method = RequestMethod.GET)
	public List<TrendMaMonitor> queryTrendMaMonitor() {
		return movingAvgBuss.queryTrendMaMonitorList();
	}

}

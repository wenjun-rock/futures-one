package fwj.futures.data.simulate.thrend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.trend.buss.MovingAvgBuss;
import fwj.futures.resource.trend.indicator.EMA;
import fwj.futures.resource.trend.vo.ProdMA;
import fwj.futures.resource.web.vo.Price;

@Component
public class CheckMovingAverage extends AbstractBaseLaunch {

	@Autowired
	MovingAvgBuss movingAvgBuss;

	@Override
	protected void execute() throws Exception {
		// Calendar cal = Calendar.getInstance();
		// Date endDt = cal.getTime();
		// cal.add(Calendar.YEAR, -20);
		// Date startDt = cal.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDt = df.parse("2015-10-21");
		Date endDt = df.parse("2015-10-22");

		ProdMA prodMA = movingAvgBuss.calProdMovingAverage("RB", -1, new EMA(20));
		List<Price> prodPriceList = prodMA.getProdIndexLine().getPrices();
		List<Price> EMAList = prodMA.getMvAvgLineList().get(0).getPrices();
		

		
		
	}

	public static void main(String[] args) {
		launch(CheckMovingAverage.class);
	}
}

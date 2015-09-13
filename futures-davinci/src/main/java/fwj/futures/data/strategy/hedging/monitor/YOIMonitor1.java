package fwj.futures.data.strategy.hedging.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataProcessor;
import fwj.futures.data.struct.Formula;

@Component
public class YOIMonitor1  extends AbstractBaseLaunch {
	
	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		Formula formula = Formula.create().putConstant("1029").putMultinomials(ProdEnum.DouYou, "1")
				.putMultinomials(ProdEnum.CaiYou, "-1.119");
		dataProcessor.monitorEndPriceFormula("2015-01-01", formula);
	}

	public static void main(String[] args) {
		launch(YOIMonitor1.class);
	}

}

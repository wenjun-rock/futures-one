package fwj.futures.data.strategy.hedging.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.Product;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataProcessor;
import fwj.futures.data.struct.Formula;

@Component
public class YPMonitor1 extends AbstractBaseLaunch {

	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		Formula formula = Formula.create().putConstant("-1073").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.ZongLvYou, "-1");
		dataProcessor.monitorEndPriceFormula("2004-09-03", formula);
	}

	public static void main(String[] args) {
		launch(YPMonitor1.class);
	}

}

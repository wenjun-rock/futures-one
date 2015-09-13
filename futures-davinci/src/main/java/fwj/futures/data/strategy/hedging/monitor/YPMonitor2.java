package fwj.futures.data.strategy.hedging.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataProcessor;
import fwj.futures.data.struct.Formula;

@Component
public class YPMonitor2 extends AbstractBaseLaunch {

	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		Formula formula = Formula.create().putConstant("-587.08732").putMultinomials(ProdEnum.DouYou, "1")
				.putMultinomials(ProdEnum.ZongLvYou, "-1.03179");
		dataProcessor.monitorEndPriceFormula("2014-03-03", formula);
	}

	public static void main(String[] args) {
		launch(YPMonitor2.class);
	}

}

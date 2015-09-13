package fwj.futures.data.strategy.hedging.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataProcessor;
import fwj.futures.data.struct.Formula;

@Component
public class JJMMonitor1  extends AbstractBaseLaunch {
	
	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		Formula formula = Formula.create().putConstant("131.45166").putMultinomials(ProdEnum.JiaoTan, "1")
				.putMultinomials(ProdEnum.JiaoMei, "-1.55585");

		dataProcessor.monitorEndPriceFormula("2015-01-01", formula);
	}

	public static void main(String[] args) {
		launch(JJMMonitor1.class);
	}

}

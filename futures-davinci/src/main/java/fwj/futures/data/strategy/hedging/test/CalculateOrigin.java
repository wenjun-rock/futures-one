package fwj.futures.data.strategy.hedging.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.Product;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataProcessor;
import fwj.futures.data.struct.Formula;

@Component
public class CalculateOrigin extends AbstractBaseLaunch {

	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		Formula formula = Formula.create().putConstant("162.31").putMultinomials(Product.JiaoTan, "1")
				.putMultinomials(Product.JiaoMei, "-1.60584");
		dataProcessor.testEndPriceFormula("2014-01-01", "2016-09-02", formula);
	}

	public static void main(String[] args) {
		launch(CalculateOrigin.class);
	}

}

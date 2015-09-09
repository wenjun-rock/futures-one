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
		Formula formula = Formula.create().putConstant("105.45166").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.CaiYou, "-0.78489").putMultinomials(Product.ZongLvYou, "-0.21779");
		dataProcessor.testEndPriceFormula("2015-01-01", "2015-09-02", formula);
	}

	public static void main(String[] args) {
		launch(CalculateOrigin.class);
	}

}

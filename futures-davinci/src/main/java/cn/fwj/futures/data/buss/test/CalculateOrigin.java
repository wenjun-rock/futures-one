package cn.fwj.futures.data.buss.test;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.process.DataProcessor;
import cn.fwj.futures.data.vo.Formula;

public class CalculateOrigin {

	public static void main(String[] args) throws Exception {
		Formula formula = Formula.create().putConstant("105.45166").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.CaiYou, "-0.78489").putMultinomials(Product.ZongLvYou, "-0.21779");
		DataProcessor.get().testEndPriceFormula("2015-01-01", "2015-09-02", formula);
	}

}

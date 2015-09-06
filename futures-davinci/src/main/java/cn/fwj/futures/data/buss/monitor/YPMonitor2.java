package cn.fwj.futures.data.buss.monitor;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.process.DataProcessor;
import cn.fwj.futures.data.vo.Formula;

public class YPMonitor2 {

	public static void main(String[] args) throws Exception {
		Formula formula = Formula.create().putConstant("-587.08732").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.ZongLvYou, "-1.03179");
		DataProcessor.get().monitorEndPriceFormula("2014-03-03", formula);
	}

}

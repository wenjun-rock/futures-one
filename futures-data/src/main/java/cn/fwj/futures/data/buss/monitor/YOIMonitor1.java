package cn.fwj.futures.data.buss.monitor;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.process.DataProcessor;
import cn.fwj.futures.data.vo.Formula;

public class YOIMonitor1 {
	
	public static void main(String[] args) throws Exception {
		Formula formula = Formula.create().putConstant("1029").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.CaiYou, "-1.119");
		DataProcessor.get().monitorEndPriceFormula("2015-01-01", formula);
	}


}

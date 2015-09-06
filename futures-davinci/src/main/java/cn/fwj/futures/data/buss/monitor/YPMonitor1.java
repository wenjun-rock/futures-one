package cn.fwj.futures.data.buss.monitor;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.process.DataProcessor;
import cn.fwj.futures.data.vo.Formula;

public class YPMonitor1 {
	
	public static void main(String[] args) throws Exception {

		Formula formula = Formula.create().putConstant("-1073").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.ZongLvYou, "-1");
		DataProcessor.get().monitorEndPriceFormula("2004-09-03", formula);
	}


}

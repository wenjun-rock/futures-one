package cn.fwj.futures.data.buss.model;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.process.DataProcessor;

public class ExportNormal {

	public static void main(String[] args) throws Exception {
		DataProcessor.get().exportEndPriceNormal("2004-03-03", "2015-09-02", 10000, Product.DouYou, Product.ZongLvYou, Product.CaiYou);
	}

}

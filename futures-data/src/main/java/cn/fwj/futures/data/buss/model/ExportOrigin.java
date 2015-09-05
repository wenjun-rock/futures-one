package cn.fwj.futures.data.buss.model;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.process.DataProcessor;

public class ExportOrigin {
	
	public static void main(String[] args) throws Exception {
		DataProcessor.get().exportEndPrice("2015-01-01", "2015-09-02", Product.DouYou, Product.ZongLvYou, Product.CaiYou);
	}

}

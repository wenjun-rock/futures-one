package cn.fwj.futures.data.buss.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.launch.AbstractBaseLaunch;
import cn.fwj.futures.data.process.DataProcessor;

@Component
public class ExportOrigin extends AbstractBaseLaunch {

	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		dataProcessor.exportEndPrice("2015-01-01", "2015-09-02", Product.DouYou, Product.ZongLvYou, Product.CaiYou);
	}

	public static void main(String[] args) {
		launch(ExportOrigin.class);
	}
}

package fwj.futures.data.strategy.hedging.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.Product;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataProcessor;

@Component
public class ExportOrigin extends AbstractBaseLaunch {

	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		dataProcessor.exportEndPrice("2010-09-01", "2016-09-02", Product.DouPo, Product.CaiPo);
	}

	public static void main(String[] args) {
		launch(ExportOrigin.class);
	}
}

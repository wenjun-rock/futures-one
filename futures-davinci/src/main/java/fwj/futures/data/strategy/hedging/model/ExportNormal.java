package fwj.futures.data.strategy.hedging.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataProcessor;

@Component
public class ExportNormal extends AbstractBaseLaunch {

	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		dataProcessor.exportEndPriceNormal("2004-03-03", "2015-09-02", 10000, ProdEnum.DouYou, ProdEnum.ZongLvYou,
				ProdEnum.CaiYou);
	}

	public static void main(String[] args) {
		launch(ExportNormal.class);
	}

}

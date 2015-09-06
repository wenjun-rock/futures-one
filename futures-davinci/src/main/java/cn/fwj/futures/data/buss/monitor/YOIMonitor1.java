package cn.fwj.futures.data.buss.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.launch.AbstractBaseLaunch;
import cn.fwj.futures.data.process.DataProcessor;
import cn.fwj.futures.data.vo.Formula;

@Component
public class YOIMonitor1  extends AbstractBaseLaunch {
	
	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		Formula formula = Formula.create().putConstant("1029").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.CaiYou, "-1.119");
		dataProcessor.monitorEndPriceFormula("2015-01-01", formula);
	}

	public static void main(String[] args) {
		launch(YOIMonitor1.class);
	}

}

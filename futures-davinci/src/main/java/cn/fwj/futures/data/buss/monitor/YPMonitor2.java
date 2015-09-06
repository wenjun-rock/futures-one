package cn.fwj.futures.data.buss.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.launch.AbstractBaseLaunch;
import cn.fwj.futures.data.process.DataProcessor;
import cn.fwj.futures.data.vo.Formula;

@Component
public class YPMonitor2 extends AbstractBaseLaunch {

	@Autowired
	private DataProcessor dataProcessor;

	@Override
	protected void execute() throws Exception {
		Formula formula = Formula.create().putConstant("-587.08732").putMultinomials(Product.DouYou, "1")
				.putMultinomials(Product.ZongLvYou, "-1.03179");
		dataProcessor.monitorEndPriceFormula("2014-03-03", formula);
	}

	public static void main(String[] args) {
		launch(YPMonitor2.class);
	}

}

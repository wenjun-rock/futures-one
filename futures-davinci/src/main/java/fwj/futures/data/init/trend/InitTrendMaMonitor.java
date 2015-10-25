package fwj.futures.data.init.trend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.trend.buss.MovingAvgBuss;

@Component
public class InitTrendMaMonitor extends AbstractBaseLaunch {

	@Autowired
	MovingAvgBuss movingAvgBuss;

	@Override
	protected void execute() throws Exception {
		int i = 0;
		for (ProdEnum prod : ProdEnum.values()) {
			log.info(String.format("%3d InitTrendMaMonitor for %s ", ++i, prod.getCode()));
			movingAvgBuss.calTrendMaMonitor(prod.getCode());
		}
	}

	public static void main(String[] args) {
		launch(InitTrendMaMonitor.class);
	}
}

package fwj.futures.data.init.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.task.KLineRefresher;

@Component
public class GlobalKLineDownload extends AbstractBaseLaunch {

	@Autowired
	KLineRefresher kLineRefresher;

	@Override
	protected void execute() throws Exception {
		kLineRefresher.refreshGlobalKLine();
	}

	public static void main(String[] args) {
		launch(GlobalKLineDownload.class);
	}
}

package fwj.futures.data.init.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.task.KLineRefresher;

@Component
public class KLineDownload extends AbstractBaseLaunch {

	@Autowired
	KLineRefresher kLineRefresher;

	@Override
	protected void execute() throws Exception {
		kLineRefresher.doTask();
	}

	public static void main(String[] args) {
		launch(KLineDownload.class);
	}
}

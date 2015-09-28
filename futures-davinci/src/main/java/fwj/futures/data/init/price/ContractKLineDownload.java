package fwj.futures.data.init.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.task.KLineRefresher;

@Component
public class ContractKLineDownload extends AbstractBaseLaunch {

	@Autowired
	KLineRefresher kLineRefresher;

	@Override
	protected void execute() throws Exception {
		kLineRefresher.refreshContractKLine(false);
	}

	public static void main(String[] args) {
		launch(ContractKLineDownload.class);
	}
}

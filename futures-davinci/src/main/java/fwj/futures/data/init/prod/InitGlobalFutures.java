package fwj.futures.data.init.prod;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.entity.prod.GlobalFutures;
import fwj.futures.resource.repository.prod.GlobalFuturesRepository;

@Component
public class InitGlobalFutures extends AbstractBaseLaunch {

	@Autowired
	private GlobalFuturesRepository globalFuturesRepo;

	@Override
	protected void execute() throws Exception {

		Stream.of(//
				new Input("PB", "PBD", "LME铅", "LME"), //
				new Input("ZN", "ZSD", "LME锌", "LME"), //
				new Input("NI", "NID", "LME镍", "LME"), //
				new Input("SN", "SND", "LME锡", "LME"), //
				new Input("AL", "AHD", "LME铝", "LME"), //
				new Input("CU", "CAD", "LME铜", "LME")//
		).forEach(input -> {
			GlobalFutures globalFutures = globalFuturesRepo.findByCode(input.code);
			if (globalFutures == null) {
				globalFutures = new GlobalFutures();
			}
			globalFutures.setCode(input.code);
			globalFutures.setGlobalCode(input.globalCode);
			globalFutures.setGlobalName(input.globalName);
			globalFutures.setExchange(input.exchange);
			globalFuturesRepo.save(globalFutures);
		});
		System.out.println("done!");
	}

	public static void main(String[] args) {
		launch(InitGlobalFutures.class);
	}

	private class Input {
		private String code;
		private String globalCode;
		private String globalName;
		private String exchange;

		public Input(String code, String globalCode, String globalName, String exchange) {
			this.code = code;
			this.globalCode = globalCode;
			this.globalName = globalName;
			this.exchange = exchange;
		}
	}
}

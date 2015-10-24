package fwj.futures.data.init.prod;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.prod.entity.ProdMainCon;
import fwj.futures.resource.prod.repos.ProdMainConRepos;

@Component
public class InitPordMainCon extends AbstractBaseLaunch {

	@Autowired
	private ProdMainConRepos prodMainConRepos;

	@Override
	protected void execute() throws Exception {
		prodMainConRepos.deleteAllInBatch();
		Stream.of(//
					// 上海
				new MainConConf("AG", 6, 12), //
				new MainConConf("AL", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), //
				new MainConConf("AU", 6, 12), //
				new MainConConf("BU", 6, 9, 12), //
				new MainConConf("CU", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), //
				new MainConConf("HC", 1, 5, 10), //
				new MainConConf("NI", 1, 7, 9), //
				new MainConConf("PB", 6, 10), //
				new MainConConf("RB", 1, 5, 10), //
				new MainConConf("RU", 1, 5, 9), //
				new MainConConf("SN", 7, 9), //
				new MainConConf("ZN", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), //
				// 大连
				new MainConConf("A", 1, 5, 9), //
				new MainConConf("C", 1, 5, 9), //
				new MainConConf("CS", 1, 5, 9), //
				new MainConConf("I", 1, 5, 9), //
				new MainConConf("J", 1, 5, 9), //
				new MainConConf("JD", 1, 5, 9), //
				new MainConConf("JM", 1, 5, 9), //
				new MainConConf("L", 1, 5, 9), //
				new MainConConf("M", 1, 5, 9), //
				new MainConConf("P", 1, 5, 9), //
				new MainConConf("PP", 1, 5, 9), //
				new MainConConf("V", 1, 5, 9), //
				new MainConConf("Y", 1, 5, 9), //
				// 郑州
				new MainConConf("CF", 1, 5, 9), //
				new MainConConf("FG", 1, 5, 6, 6, 9), //
				new MainConConf("MA", 1, 6, 9), //
				new MainConConf("OI", 1, 5, 9), //
				new MainConConf("RM", 1, 5, 9), //
				new MainConConf("SR", 1, 5, 9), //
				new MainConConf("TA", 1, 5, 9), //
				new MainConConf("WH", 1, 5, 9), //
				new MainConConf("ZC", 1, 5, 9)).forEach(prod -> {
					Arrays.stream(prod.month).forEach(month -> {
						ProdMainCon contract = new ProdMainCon();
						contract.setCode(prod.code);
						contract.setMonth(month);
						prodMainConRepos.save(contract);
					});
				});
		prodMainConRepos.flush();

	}

	public static void main(String[] args) {
		launch(InitPordMainCon.class);
	}

	private class MainConConf {
		private String code;
		private int[] month;

		private MainConConf(String code, int... month) {
			this.code = code;
			this.month = month;
		}
	}

}

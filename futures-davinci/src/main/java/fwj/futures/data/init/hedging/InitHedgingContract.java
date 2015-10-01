package fwj.futures.data.init.hedging;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.entity.hedging.HedgingContract;
import fwj.futures.resource.repository.hedging.HedgingContractRepository;

@Component
public class InitHedgingContract extends AbstractBaseLaunch {

	@Autowired
	private HedgingContractRepository hedgingContractRepo;

	@Override
	protected void execute() throws Exception {
		hedgingContractRepo.deleteAllInBatch();
		Stream.of(//
					// 上海
				new ProdContract("AG", new int[][] { { 6, 12 } }), //
				new ProdContract("AL",
						new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 8 }, { 8, 9 },
								{ 9, 10 }, { 10, 11 }, { 11, 12 }, { 12, 1 } }), //
				new ProdContract("AU", new int[][] { { 6, 12 } }), //
				new ProdContract("BU", new int[][] { { 6, 9 }, { 9, 12 }, { 12, 6 } }), //
				new ProdContract("CU",
						new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 8 }, { 8, 9 },
								{ 9, 10 }, { 10, 11 }, { 11, 12 }, { 12, 1 } }), //
				new ProdContract("HC", new int[][] { { 1, 5 }, { 5, 10 }, { 10, 1 } }), //
				new ProdContract("NI", new int[][] { { 1, 7 }, { 7, 9 }, { 9, 1 } }), //
				new ProdContract("PB", new int[][] { { 6, 10 } }), //
				new ProdContract("RB", new int[][] { { 1, 5 }, { 5, 10 }, { 10, 1 } }), //
				new ProdContract("RU", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("SN", new int[][] { { 7, 9 } }), //
				new ProdContract("ZN",
						new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 8 }, { 8, 9 },
								{ 9, 10 }, { 10, 11 }, { 11, 12 }, { 12, 1 } }), //
				// 大连
				new ProdContract("A", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("C", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("CS", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("I", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("J", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("JD", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("JM", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("L", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("M", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("P", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("PP", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("V", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("Y", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				// 郑州
				new ProdContract("CF", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("FG", new int[][] { { 1, 5 }, { 5, 6 }, { 6, 9 }, { 9, 1 } }), //
				new ProdContract("MA", new int[][] { { 1, 6 }, { 6, 9 }, { 9, 1 } }), //
				new ProdContract("OI", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("RM", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("SR", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("TA", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("WH", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }), //
				new ProdContract("ZC", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } })
		).forEach(prod -> {
			Stream.of(prod.contracts).forEach(ele -> {
				HedgingContract contract = new HedgingContract();
				contract.setCode(prod.code);
				contract.setContractMonth1(ele[0]);
				contract.setContractMonth2(ele[1]);
				contract.setName(String.format("%s%02d-%s%02d", prod.code, ele[0], prod.code, ele[1]));
				hedgingContractRepo.save(contract);
			});
		});
		hedgingContractRepo.flush();

	}

	public static void main(String[] args) {
		launch(InitHedgingContract.class);
	}

	private class ProdContract {
		private String code;
		private int[][] contracts;

		private ProdContract(String code, int[][] contracts) {
			this.code = code;
			this.contracts = contracts;
		}
	}

}

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
				new ProdContract("A", new int[][] { { 1, 5 }, { 5, 9 }, { 9, 1 } }) //
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

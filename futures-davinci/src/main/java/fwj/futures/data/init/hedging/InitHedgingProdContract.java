package fwj.futures.data.init.hedging;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.hedging.buss.HedgingProdContractBuss;
import fwj.futures.resource.hedging.entity.HedgingProdContract;
import fwj.futures.resource.hedging.enu.HedgingProContractType;
import fwj.futures.resource.hedging.repos.HedgingProdContractRepos;

@Component
public class InitHedgingProdContract extends AbstractBaseLaunch {
	
	@Autowired
	private HedgingProdContractBuss hedgingProdContractBuss;

	@Autowired
	private HedgingProdContractRepos hedgingProdContractRepos;

	@Override
	protected void execute() throws Exception {
		Stream.of(//
				new ProdContract("A", 5, "A", 1, 4, HedgingProContractType.SP), //
				new ProdContract("A", 9, "A", 5, 4, HedgingProContractType.SP), //
				new ProdContract("A", 1, "A", 9, 4, HedgingProContractType.SP), //
				new ProdContract("C", 5, "C", 1, 4, HedgingProContractType.SP), //
				new ProdContract("C", 9, "C", 5, 4, HedgingProContractType.SP), //
				new ProdContract("C", 1, "C", 9, 4, HedgingProContractType.SP), //
				new ProdContract("CS", 5, "CS", 1, 4, HedgingProContractType.SP), //
				new ProdContract("CS", 9, "CS", 5, 4, HedgingProContractType.SP), //
				new ProdContract("CS", 1, "CS", 9, 4, HedgingProContractType.SP), //
				new ProdContract("I", 5, "I", 1, 4, HedgingProContractType.SP), //
				new ProdContract("I", 9, "I", 5, 4, HedgingProContractType.SP), //
				new ProdContract("I", 1, "I", 9, 4, HedgingProContractType.SP), //
				new ProdContract("J", 5, "J", 1, 4, HedgingProContractType.SP), //
				new ProdContract("J", 9, "J", 5, 4, HedgingProContractType.SP), //
				new ProdContract("J", 1, "J", 9, 4, HedgingProContractType.SP), //
				new ProdContract("JD", 5, "JD", 1, 4, HedgingProContractType.SP), //
				new ProdContract("JD", 9, "JD", 5, 4, HedgingProContractType.SP), //
				new ProdContract("JD", 1, "JD", 9, 4, HedgingProContractType.SP), //
				new ProdContract("JM", 5, "JM", 1, 4, HedgingProContractType.SP), //
				new ProdContract("JM", 9, "JM", 5, 4, HedgingProContractType.SP), //
				new ProdContract("JM", 1, "JM", 9, 4, HedgingProContractType.SP), //
				new ProdContract("L", 5, "L", 1, 4, HedgingProContractType.SP), //
				new ProdContract("L", 9, "L", 5, 4, HedgingProContractType.SP), //
				new ProdContract("L", 1, "L", 9, 4, HedgingProContractType.SP), //
				new ProdContract("M", 5, "M", 1, 4, HedgingProContractType.SP), //
				new ProdContract("M", 9, "M", 5, 4, HedgingProContractType.SP), //
				new ProdContract("M", 1, "M", 9, 4, HedgingProContractType.SP), //
				new ProdContract("P", 5, "P", 1, 4, HedgingProContractType.SP), //
				new ProdContract("P", 9, "P", 5, 4, HedgingProContractType.SP), //
				new ProdContract("P", 1, "P", 9, 4, HedgingProContractType.SP), //
				new ProdContract("PP", 5, "PP", 1, 4, HedgingProContractType.SP), //
				new ProdContract("PP", 9, "PP", 5, 4, HedgingProContractType.SP), //
				new ProdContract("PP", 1, "PP", 9, 4, HedgingProContractType.SP), //
				new ProdContract("V", 5, "V", 1, 4, HedgingProContractType.SP), //
				new ProdContract("V", 9, "V", 5, 4, HedgingProContractType.SP), //
				new ProdContract("V", 1, "V", 9, 4, HedgingProContractType.SP), //
				new ProdContract("Y", 5, "Y", 1, 4, HedgingProContractType.SP), //
				new ProdContract("Y", 9, "Y", 5, 4, HedgingProContractType.SP), //
				new ProdContract("Y", 1, "Y", 9, 4, HedgingProContractType.SP), //
				
				new ProdContract("CF", 5, "CF", 1, 4, HedgingProContractType.SP), //
				new ProdContract("CF", 9, "CF", 5, 4, HedgingProContractType.SP), //
				new ProdContract("CF", 1, "CF", 9, 4, HedgingProContractType.SP), //
				new ProdContract("FG", 5, "FG", 1, 4, HedgingProContractType.SP), //
				new ProdContract("FG", 9, "FG", 5, 4, HedgingProContractType.SP), //
				new ProdContract("FG", 1, "FG", 9, 4, HedgingProContractType.SP), //
				new ProdContract("HC", 5, "HC", 1, 3, HedgingProContractType.SP), //
				new ProdContract("HC", 10, "HC", 5, 3, HedgingProContractType.SP), //
				new ProdContract("HC", 1, "HC", 10, 3, HedgingProContractType.SP), //
				new ProdContract("RB", 5, "RB", 1, 3, HedgingProContractType.SP), //
				new ProdContract("RB", 10, "RB", 5, 3, HedgingProContractType.SP), //
				new ProdContract("RB", 1, "RB", 10, 3, HedgingProContractType.SP), //
				new ProdContract("MA", 5, "MA", 1, 4, HedgingProContractType.SP), //
				new ProdContract("MA", 9, "MA", 5, 4, HedgingProContractType.SP), //
				new ProdContract("MA", 1, "MA", 9, 4, HedgingProContractType.SP), //
				new ProdContract("OI", 5, "OI", 1, 4, HedgingProContractType.SP), //
				new ProdContract("OI", 9, "OI", 5, 4, HedgingProContractType.SP), //
				new ProdContract("OI", 1, "OI", 9, 4, HedgingProContractType.SP), //
				new ProdContract("RM", 5, "RM", 1, 4, HedgingProContractType.SP), //
				new ProdContract("RM", 9, "RM", 5, 4, HedgingProContractType.SP), //
				new ProdContract("RM", 1, "RM", 9, 4, HedgingProContractType.SP), //
				new ProdContract("RU", 5, "RU", 1, 3, HedgingProContractType.SP), //
				new ProdContract("RU", 9, "RU", 5, 3, HedgingProContractType.SP), //
				new ProdContract("RU", 1, "RU", 9, 3, HedgingProContractType.SP), //
				new ProdContract("SR", 5, "SR", 1, 4, HedgingProContractType.SP), //
				new ProdContract("SR", 9, "SR", 5, 4, HedgingProContractType.SP), //
				new ProdContract("SR", 1, "SR", 9, 4, HedgingProContractType.SP), //
				new ProdContract("TA", 5, "TA", 1, 4, HedgingProContractType.SP), //
				new ProdContract("TA", 9, "TA", 5, 4, HedgingProContractType.SP), //
				new ProdContract("TA", 1, "TA", 9, 4, HedgingProContractType.SP), //
				new ProdContract("WH", 5, "WH", 1, 4, HedgingProContractType.SP), //
				new ProdContract("WH", 9, "WH", 5, 4, HedgingProContractType.SP), //
				new ProdContract("WH", 1, "WH", 9, 4, HedgingProContractType.SP), //
				
				new ProdContract("CS", 1, "C", 1, 8, HedgingProContractType.SPC), //
				new ProdContract("CS", 5, "C", 5, 8, HedgingProContractType.SPC), //
				new ProdContract("CS", 9, "C", 9, 7, HedgingProContractType.SPC), //
				new ProdContract("A", 1, "M", 1, 5, HedgingProContractType.SPC), //
				new ProdContract("A", 5, "M", 5, 5, HedgingProContractType.SPC), //
				new ProdContract("A", 9, "M", 9, 5, HedgingProContractType.SPC), //
				new ProdContract("L", 1, "V", 1, 5, HedgingProContractType.SPC), //
				new ProdContract("L", 5, "V", 5, 5, HedgingProContractType.SPC), //
				new ProdContract("L", 9, "V", 9, 5, HedgingProContractType.SPC), //
				new ProdContract("Y", 1, "P", 1, 5, HedgingProContractType.SPC), //
				new ProdContract("Y", 5, "P", 5, 5, HedgingProContractType.SPC), //
				new ProdContract("Y", 9, "P", 9, 5, HedgingProContractType.SPC), //
				new ProdContract("M", 1, "RM", 1, 3, HedgingProContractType.SPC), //
				new ProdContract("M", 5, "RM", 5, 3, HedgingProContractType.SPC), //
				new ProdContract("M", 9, "RM", 9, 3, HedgingProContractType.SPC), //
				new ProdContract("Y", 1, "OI", 1, 3, HedgingProContractType.SPC), //
				new ProdContract("Y", 5, "OI", 5, 3, HedgingProContractType.SPC), //
				new ProdContract("Y", 9, "OI", 9, 3, HedgingProContractType.SPC), //
				new ProdContract("JD", 1, "M", 1, 3, HedgingProContractType.SPC), //
				new ProdContract("JD", 5, "M", 5, 3, HedgingProContractType.SPC), //
				new ProdContract("JD", 9, "M", 9, 3, HedgingProContractType.SPC), //
				new ProdContract("L", 1, "PP", 1, 3, HedgingProContractType.SPC), //
				new ProdContract("L", 5, "PP", 5, 3, HedgingProContractType.SPC), //
				new ProdContract("L", 9, "PP", 9, 3, HedgingProContractType.SPC), //
				new ProdContract("HC", 1, "RB", 1, 3, HedgingProContractType.SPC), //
				new ProdContract("HC", 5, "RB", 5, 3, HedgingProContractType.SPC), //
				new ProdContract("HC", 10, "RB", 10, 3, HedgingProContractType.SPC) //
				
				).forEach(prod -> {
					HedgingProdContract hedging = new HedgingProdContract();
					hedging.setCode1(prod.code1);
					hedging.setMonth1(prod.month1);
					hedging.setCode2(prod.code2);
					hedging.setMonth2(prod.month2);
					hedging.setRank(prod.rank);
					hedging.setType(prod.type.getCode());
					hedgingProdContractRepos.save(hedging);
				});
		hedgingProdContractRepos.flush();
		
		hedgingProdContractBuss.refreshHedging();

	}

	public static void main(String[] args) {
		launch(InitHedgingProdContract.class);
	}

	private class ProdContract {
		private String code1;
		private int month1;
		private String code2;
		private int month2;
		private int rank;
		private HedgingProContractType type;

		private ProdContract(String code1, int month1, String code2, int month2, int rank,
				HedgingProContractType type) {
			this.code1 = code1;
			this.month1 = month1;
			this.code2 = code2;
			this.month2 = month2;
			this.rank = rank;
			this.type = type;
		}
	}

}

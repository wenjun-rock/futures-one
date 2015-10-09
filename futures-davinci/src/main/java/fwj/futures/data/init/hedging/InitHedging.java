package fwj.futures.data.init.hedging;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.struct.Formula;
import fwj.futures.resource.entity.hedging.Hedging;
import fwj.futures.resource.repository.hedging.HedgingRepository;

@Component
public class InitHedging extends AbstractBaseLaunch {

	@Autowired
	private HedgingRepository hedgingRepository;

	@Override
	protected void execute() throws Exception {

		Stream.of(//
				new Input("豆油-棕榈油", "-600", ProdEnum.DouYou, "1", ProdEnum.ZongLvYou, "-1", 300),
				new Input("焦炭-焦煤", "100", ProdEnum.JiaoTan, "1", ProdEnum.JiaoMei, "-1.5", 30),
				new Input("大豆-玉米", "0", ProdEnum.DaDou1, "1", ProdEnum.YuMi, "-2", 300)
		//
		).forEach(input -> {
			Hedging hedging = hedgingRepository.findByName(input.name);
			if (hedging == null) {
				hedging = new Hedging();
			}
			hedging.setName(input.name);
			hedging.setExpression(JSON.toJSONString(Formula.create().putConstant(input.constant)
					.putMultinomial(input.prod1, input.coefficient1).putMultinomial(input.prod2, input.coefficient2)));
//			hedging.setStdError(new BigDecimal(input.stdError));
			hedgingRepository.save(hedging);
		});
	}

	public static void main(String[] args) {
		launch(InitHedging.class);
	}

	private class Input {
		private String name;
		private String constant;
		private ProdEnum prod1;
		private String coefficient1;
		private ProdEnum prod2;
		private String coefficient2;
		private int stdError;

		public Input(String name, String constant, ProdEnum prod1, String coefficient1, ProdEnum prod2,
				String coefficient2, int stdError) {
			this.name = name;
			this.constant = constant;
			this.prod1 = prod1;
			this.coefficient1 = coefficient1;
			this.prod2 = prod2;
			this.coefficient2 = coefficient2;
			this.stdError = stdError;
		}
	}
}

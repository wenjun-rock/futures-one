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
				new Input("豆油-棕榈油", ProdEnum.DouYou, "1", ProdEnum.ZongLvYou, "-1", 500, 1500, 1000, 600, 1400),
				new Input("焦炭-焦煤", ProdEnum.JiaoTan, "1", ProdEnum.JiaoMei, "-1.5", -30, 30, 0, -10, 10),
				new Input("大豆-玉米", ProdEnum.DaDou1, "1", ProdEnum.YuMi, "-2", -40, 40, 0, -20, 20)
		//
		).forEach(input -> {
			Hedging hedging = hedgingRepository.findByName(input.name);
			if (hedging == null) {
				hedging = new Hedging();
			}
			hedging.setName(input.name);
			hedging.setExpression(JSON.toJSONString(Formula.create().putConstant(BigDecimal.ZERO)
					.putMultinomial(input.prod1, input.coefficient1).putMultinomial(input.prod2, input.coefficient2)));
			hedging.setDown(input.down);
			hedging.setUp(input.up);
			hedging.setMid(input.mid);
			hedging.setQ1(input.q1);
			hedging.setQ3(input.q3);
			hedgingRepository.save(hedging);
		});
	}

	public static void main(String[] args) {
		launch(InitHedging.class);
	}

	private class Input {
		private String name;
		private ProdEnum prod1;
		private String coefficient1;
		private ProdEnum prod2;
		private String coefficient2;
		private int down;
		private int up;
		private int mid;
		private int q1;
		private int q3;

		public Input(String name, ProdEnum prod1, String coefficient1, ProdEnum prod2, String coefficient2, int down,
				int up, int mid, int q1, int q3) {
			this.name = name;
			this.prod1 = prod1;
			this.coefficient1 = coefficient1;
			this.prod2 = prod2;
			this.coefficient2 = coefficient2;
			this.up = up;
			this.down = down;
			this.mid = mid;
			this.q1 = q1;
			this.q3 = q3;
		}
	}
}

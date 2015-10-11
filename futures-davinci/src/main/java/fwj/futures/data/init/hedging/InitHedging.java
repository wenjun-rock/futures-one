package fwj.futures.data.init.hedging;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.struct.Formula;
import fwj.futures.resource.buss.DailyPriceBuss;
import fwj.futures.resource.buss.HedgingBuss;
import fwj.futures.resource.entity.hedging.Hedging;
import fwj.futures.resource.repository.hedging.HedgingRepository;
import fwj.futures.resource.util.StatisticsHelper;
import fwj.futures.resource.util.StatisticsHelper.Result;
import fwj.futures.resource.vo.KLineGroup;
import fwj.futures.resource.web.vo.Price;

@Component
public class InitHedging extends AbstractBaseLaunch {

	@Autowired
	private HedgingRepository hedgingRepository;

	@Autowired
	private DailyPriceBuss dailyPriceBuss;

	@Autowired
	private HedgingBuss hedgingBuss;

	@Override
	protected void execute() throws Exception {

		List<KLineGroup> kLineGroup = dailyPriceBuss.queryAllGroup();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDt = df.parse("2012-09-01");
		Date endDt = df.parse("2015-09-01");

		Stream.of(//
				new Input("豆油-棕榈油", ProdEnum.DouYou, "1", ProdEnum.ZongLvYou, "-1"),
				new Input("焦炭-焦煤", ProdEnum.JiaoTan, "1", ProdEnum.JiaoMei, "-1.5"),
				new Input("大豆-玉米", ProdEnum.DaDou1, "1", ProdEnum.YuMi, "-2")
		//
		).forEach(input -> {
			Hedging hedging = hedgingRepository.findByName(input.name);
			if (hedging == null) {
				hedging = new Hedging();
			}
			Formula formula = Formula.create().putConstant(BigDecimal.ZERO)
					.putMultinomial(input.prod1, input.coefficient1).putMultinomial(input.prod2, input.coefficient2);
			hedging.setName(input.name);
			hedging.setExpression(JSON.toJSONString(formula));
			List<Price> klinePrices = hedgingBuss.calculateKLine(formula, kLineGroup);
			List<BigDecimal> pList = klinePrices.stream().filter(p -> p.getD().after(startDt) && p.getD().before(endDt))
					.map(Price::getP).collect(Collectors.toList());
			Result result = StatisticsHelper.statsBigDecimal(pList);

			hedging.setDown((int) (result.getAvg() - result.getSd()));
			hedging.setUp((int) (result.getAvg() + result.getSd()));
			hedging.setMid((int) (result.getAvg()));
			hedging.setQ1((int) (result.getQ1()));
			hedging.setQ3((int) (result.getQ3()));
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

		public Input(String name, ProdEnum prod1, String coefficient1, ProdEnum prod2, String coefficient2) {
			this.name = name;
			this.prod1 = prod1;
			this.coefficient1 = coefficient1;
			this.prod2 = prod2;
			this.coefficient2 = coefficient2;
		}
	}
}

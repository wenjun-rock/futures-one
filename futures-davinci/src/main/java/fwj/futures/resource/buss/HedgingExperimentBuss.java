package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.struct.Formula;
import fwj.futures.resource.entity.hedging.HedgingProdExperiment;
import fwj.futures.resource.price.buss.DailyPriceBuss;
import fwj.futures.resource.repository.hedging.HedgingProdExperimentRepository;
import fwj.futures.resource.vo.HedgingExperimentMonitor;
import fwj.futures.resource.vo.HedgingExperimentView;
import fwj.futures.resource.vo.KLineGroup;
import fwj.futures.resource.web.vo.Price;

@Component
public class HedgingExperimentBuss {

	@Autowired
	private DailyPriceBuss kLineBuss;

	@Autowired
	private HedgingBuss hedgingBuss;

	@Autowired
	private HedgingProdExperimentRepository experimentRepo;

	public List<HedgingExperimentView> queryProdExperiments(BigDecimal minRsquared) {
		List<HedgingProdExperiment> experimentList = experimentRepo.findByLimitRsquared(minRsquared);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return experimentList.stream().map(experiment -> {
			Integer id = experiment.getId();
			String name = experiment.getName() + "-" + experiment.getHedgingProdBatch().getName();
			String startDt = df.format(experiment.getHedgingProdBatch().getStartDt());
			String endDt = df.format(experiment.getHedgingProdBatch().getEndDt());
			BigDecimal rsquared = experiment.getRsquared();
			String formula1 = Formula.parse(experiment.getExpression1()).toString();
			String formula2 = Formula.parse(experiment.getExpression2()).toString();
			BigDecimal stdError1 = experiment.getStdError1();
			BigDecimal stdError2 = experiment.getStdError2();
			return new HedgingExperimentView(id, name, startDt, endDt, rsquared, formula1, formula2, stdError1,
					stdError2);
		}).collect(Collectors.toList());
	}

	public HedgingExperimentMonitor monitorProdExperiment(Integer id) {
		HedgingProdExperiment experiment = experimentRepo.findOne(id);
		String name = experiment.getName() + "-" + experiment.getHedgingProdBatch().getName();
		Date startDt = experiment.getHedgingProdBatch().getStartDt();
		Date endDt = experiment.getHedgingProdBatch().getEndDt();
		Formula formula1 = Formula.parse(experiment.getExpression1());
		Formula formula2 = Formula.parse(experiment.getExpression2());
		BigDecimal stdError1 = experiment.getStdError1().setScale(0, RoundingMode.FLOOR);
		BigDecimal stdError2 = experiment.getStdError2().setScale(0, RoundingMode.FLOOR);
		List<KLineGroup> groupList = kLineBuss.queryAllGroup();
		List<Price> price1 = hedgingBuss.calculateKLine(formula1, groupList);
		List<Price> price2 = hedgingBuss.calculateKLine(formula2, groupList);
		return new HedgingExperimentMonitor(id, name, startDt, endDt, formula1.toString(), formula2.toString(),
				stdError1, stdError2, price1, price2);
	}
}

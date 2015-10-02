package fwj.futures.resource.buss;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.struct.Formula;
import fwj.futures.resource.entity.hedging.HedgingProdExperiment;
import fwj.futures.resource.repository.hedging.HedgingProdExperimentRepository;
import fwj.futures.resource.vo.HedgingExperimentView;

@Component
public class HedgingExperimentBuss {

	@Autowired
	private HedgingProdExperimentRepository experimentRepo;

	public List<HedgingExperimentView> queryProdExperiments(BigDecimal minRsquared) {
		List<HedgingProdExperiment> experimentList = experimentRepo.findByLimitRsquared(minRsquared);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return experimentList.stream().map(experiment -> {
			Integer id = experiment.getId();
			String name = experiment.getName() + "-" + experiment.getHedgingProdBatch().getId();
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
}

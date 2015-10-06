package fwj.futures.data.strategy.hedging.model;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.io.Files;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.struct.Formula;
import fwj.futures.resource.entity.hedging.HedgingProdBatch;
import fwj.futures.resource.entity.hedging.HedgingProdExperiment;
import fwj.futures.resource.entity.price.KLine;
import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.repository.hedging.HedgingProdBatchRepository;
import fwj.futures.resource.repository.hedging.HedgingProdExperimentRepository;
import fwj.futures.resource.repository.price.KLineRepository;
import fwj.futures.resource.repository.prod.FuturesRepository;
import fwj.futures.resource.util.CollectorsHelper;
import fwj.futures.resource.util.FuncHelper;

@Component
public class BatchExpriment extends AbstractBaseLaunch {

	private static final String DATA_FILE_NAME = "data.csv";

	private static final String BASE_DIR = "F:/futures";

	private static final String WORKING_DIR = "working";

	private File experimentDir;

	private File workingDir;

	private List<Experiment> chanceList = new ArrayList<>();

	private Experiment current;

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private FuturesRepository futuresRepo;

	@Autowired
	private HedgingProdBatchRepository hedgingProdbatchRepo;

	@Autowired
	private HedgingProdExperimentRepository hedgingProdExperimentRepo;

	@Override
	protected void execute() throws Exception {
		Calendar cal = Calendar.getInstance();
		Date runDt = cal.getTime();
		String dirName = new SimpleDateFormat("yyyyMMddHHmmss").format(runDt);
		experimentDir = new File(BASE_DIR, dirName);
		if (!experimentDir.exists()) {
			experimentDir.mkdirs();
		}

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		cal.setTime(df.parse(df.format(runDt)));
		cal.add(Calendar.MONTH, -6);
		Date endDt = cal.getTime();
		cal.add(Calendar.MONTH, -12);
		Date startDt = cal.getTime();

		this.execute(startDt, endDt);

		List<String> lineList = chanceList.stream().sorted().map(
				chance -> chance.getSquared() + "," + chance.getFormula1().toCsv() + "," + chance.getFormula2().toCsv())
				.collect(Collectors.toList());
		Files.asCharSink(new File(experimentDir, "report.csv"), StandardCharsets.UTF_8).writeLines(lineList);

		HedgingProdBatch batch = new HedgingProdBatch();
		batch.setName("模拟半年");
		batch.setRunDt(runDt);
		batch.setStartDt(startDt);
		batch.setEndDt(endDt);
		final HedgingProdBatch insbatch = hedgingProdbatchRepo.saveAndFlush(batch);

		Map<String, String> codeNameMap = futuresRepo.findAllActive().stream()
				.collect(Collectors.toMap(Futures::getCode, Futures::getName));

		chanceList.stream().forEach(chance -> {
			HedgingProdExperiment experiment = new HedgingProdExperiment();
			experiment.setHedgingProdBatch(insbatch);
			experiment.setName(
					codeNameMap.get(chance.getCodeList().get(0)) + "-" + codeNameMap.get(chance.getCodeList().get(1)));
			experiment.setRsquared(chance.getSquared());
			experiment.setExpression1(JSON.toJSONString(chance.getFormula1()));
			experiment.setExpression2(JSON.toJSONString(chance.getFormula2()));
			experiment.setStdError1(chance.getStdError1());
			experiment.setStdError2(chance.getStdError2());
			hedgingProdExperimentRepo.save(experiment);
		});
		hedgingProdExperimentRepo.flush();

	}

	private void execute(Date startDt, Date endDt) throws Exception {

		List<Futures> futuresList = futuresRepo.findAllActive();
		for (int i = 0; i < futuresList.size(); i++) {
			for (int j = i + 1; j < futuresList.size(); j++) {
				this.execute(startDt, endDt, Arrays.asList(futuresList.get(i), futuresList.get(j)));
			}
		}
		// this.execute(startDt, endDt,
		// Arrays.asList(futuresRepo.findByCode("P"),
		// futuresRepo.findByCode("Y")));
		// this.execute(startDt, endDt,
		// Arrays.asList(futuresRepo.findByCode("J"),
		// futuresRepo.findByCode("JM")));
	}

	private void execute(Date startDt, Date endDt, List<Futures> futuresList) throws Exception {
		init();
		if(outputCsv(startDt, endDt, futuresList)) {
			CalculateInR();
			cleanup();			
		}
	}

	private void cleanup() throws Exception {
		String newDirName = current.getCodeList().stream() //
				.sorted().reduce(FuncHelper.joinString("_")).get();
		newDirName += "_" + current.getSquared();
		workingDir.renameTo(new File(experimentDir, newDirName));
		chanceList.add(current);
		current = null;
		System.out.println(String.format("%4d Done! %s", chanceList.size(), new File(experimentDir, newDirName)));
	}

	private void init() {
		workingDir = new File(experimentDir, WORKING_DIR);
		workingDir.mkdirs();
		current = new Experiment();
	}

	private void CalculateInR() throws Exception {
		Files.copy(new File(this.getClass().getClassLoader().getResource("gauss/lm2Template.R").getFile()),
				new File(workingDir, "script.R"));
		Process process = Runtime.getRuntime().exec("D:/tool/R-3.2.2/bin/R CMD BATCH --no-restore script.R", null,
				workingDir);
		process.waitFor();
		List<String> lines = Files.readLines(new File(workingDir, "my.out"), Charset.forName("GBK"));
		current.setSquared(new BigDecimal(lines.get(1).split(" ")[1]));
		Formula formula1 = Formula.create()
				.putConstant(BigDecimal.ZERO.subtract(new BigDecimal(lines.get(5).split("\\s+")[1])))
				.putMultinomial(current.getCodeList().get(0), "1").putMultinomial(current.getCodeList().get(1),
						BigDecimal.ZERO.subtract(new BigDecimal(lines.get(6).split("\\s+")[1])));
		BigDecimal stdError1 = new BigDecimal(lines.get(5).split("\\s+")[2]);
		Formula formula2 = Formula.create().putConstant(new BigDecimal(lines.get(13).split("\\s+")[1]))
				.putMultinomial(current.getCodeList().get(1), "-1")
				.putMultinomial(current.getCodeList().get(0), new BigDecimal(lines.get(14).split("\\s+")[1]));
		BigDecimal stdError2 = new BigDecimal(lines.get(13).split("\\s+")[2]);
		current.setFormula1(formula1);
		current.setFormula2(formula2);
		current.setStdError1(stdError1);
		current.setStdError2(stdError2);
	}

	private boolean outputCsv(Date startDt, Date endDt, List<Futures> futuresList) throws Exception {
		TreeMap<Date, Map<String, KLine>> kLineMap = futuresList.stream()
				.map(futures -> kLineRepo.findByCodeAndDtBetweenOrderByDtAsc(futures.getCode(), startDt, endDt))
				.flatMap(kLineList -> kLineList.stream()).collect(Collectors.groupingBy(KLine::getDt, TreeMap::new,
						Collectors.toMap(KLine::getCode, Function.identity())));
		kLineMap = kLineMap.entrySet().stream().filter(entry -> entry.getValue().size() == futuresList.size())
				.collect(CollectorsHelper.toTreeMap(Map.Entry::getKey, Map.Entry::getValue));
		
		if(kLineMap.isEmpty()){
			return false;
		}		
		List<String> codeList = kLineMap.lastEntry().getValue().entrySet().stream()
				.sorted((l, r) -> r.getValue().getEndPrice().compareTo(l.getValue().getEndPrice()))
				.map(Map.Entry::getKey).collect(Collectors.toList());
		current.setCodeList(codeList);

		String head = "DATE";
		for (int i = 0; i < codeList.size(); i++) {
			head += ",X" + (i + 1);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String content = kLineMap.entrySet().stream().map(entry -> {
			Date dt = entry.getKey();
			Map<String, KLine> prices = entry.getValue();
			return codeList.stream().map(code -> prices.get(code).getEndPrice().toString())
					.reduce(df.format(dt.getTime()), FuncHelper.joinString(","));
		}).reduce(head, FuncHelper.joinString("\n"));

		System.out.println(workingDir.getAbsolutePath());
		System.out.println(new File(workingDir, DATA_FILE_NAME).getAbsolutePath());
		Files.asCharSink(new File(workingDir, DATA_FILE_NAME), StandardCharsets.UTF_8).write(content);
		return true;
	}

	public static void main(String[] args) {
		launch(BatchExpriment.class);
	}

	public class Experiment implements Comparable<Experiment> {

		private List<String> codeList;
		private BigDecimal squared;
		private Formula formula1;
		private Formula formula2;
		private BigDecimal stdError1;
		private BigDecimal stdError2;

		public List<String> getCodeList() {
			return codeList;
		}

		public BigDecimal getSquared() {
			return squared;
		}

		public Formula getFormula1() {
			return formula1;
		}

		public Formula getFormula2() {
			return formula2;
		}

		public BigDecimal getStdError1() {
			return stdError1;
		}

		public BigDecimal getStdError2() {
			return stdError2;
		}

		public void setCodeList(List<String> codeList) {
			this.codeList = codeList;
		}

		public void setSquared(BigDecimal squared) {
			this.squared = squared;
		}

		public void setFormula1(Formula formula1) {
			this.formula1 = formula1;
		}

		public void setFormula2(Formula formula2) {
			this.formula2 = formula2;
		}

		public void setStdError1(BigDecimal stdError1) {
			this.stdError1 = stdError1;
		}

		public void setStdError2(BigDecimal stdError2) {
			this.stdError2 = stdError2;
		}

		@Override
		public int compareTo(Experiment that) {
			return that.squared.compareTo(this.squared);
		}

		@Override
		public String toString() {
			return squared.toString() + "," + formula1.toString() + "," + formula2.toString();
		}

	}

}

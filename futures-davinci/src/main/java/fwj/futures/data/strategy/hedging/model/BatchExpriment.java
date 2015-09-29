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

import com.google.common.io.Files;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.struct.Formula;
import fwj.futures.resource.entity.price.KLine;
import fwj.futures.resource.entity.prod.Futures;
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

	private List<Chance> chanceList = new ArrayList<>();

	private Chance current;

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private FuturesRepository futuresRepo;

	@Override
	protected void execute() throws Exception {
		String dirName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		experimentDir = new File(BASE_DIR, dirName);
		if (!experimentDir.exists()) {
			experimentDir.mkdirs();
		}

		this.execute(12);

		List<String> lineList = chanceList.stream().sorted()
				.map(chance -> chance.getSquared() + "," + chance.getFormula().toCsv()).collect(Collectors.toList());
		Files.asCharSink(new File(experimentDir, "report.csv"), StandardCharsets.UTF_8).writeLines(lineList);
	}

	private void execute(int months) throws Exception {
		Calendar cal = Calendar.getInstance();
		Date endDt = cal.getTime();
		cal.add(Calendar.MONTH, -months);
		Date startDt = cal.getTime();

		List<Futures> futuresList = futuresRepo.findAllActive();
		for (int i = 0; i < futuresList.size(); i++) {
			for (int j = i + 1; j < futuresList.size(); j++) {
				this.execute(startDt, endDt, Arrays.asList(futuresList.get(i), futuresList.get(j)));
			}
		}

	}

	private void execute(Date startDt, Date endDt, List<Futures> futuresList) throws Exception {
		init();
		outputCsv(startDt, endDt, futuresList);
		CalculateInR();
		cleanup();
	}

	private void cleanup() throws Exception {
		String newDirName = current.getCodeList().stream() //
				.sorted().reduce(FuncHelper.joinString("_")).get();
		newDirName += "_" + current.getSquared();
		Files.move(workingDir, new File(experimentDir, newDirName));
		chanceList.add(current);
		current = null;
		System.out.println(String.format("%4d Done! %s", chanceList.size(), new File(experimentDir, newDirName)));
	}

	private void init() {
		workingDir = new File(experimentDir, WORKING_DIR);
		workingDir.mkdirs();
		current = new Chance();
	}

	private void CalculateInR() throws Exception {
		Files.copy(new File(this.getClass().getClassLoader().getResource("gauss/lm2Template.R").getFile()),
				new File(workingDir, "script.R"));
		Process process = Runtime.getRuntime().exec("D:/tool/R-3.2.2/bin/R CMD BATCH --no-restore script.R", null,
				workingDir);
		process.waitFor();
		List<String> lines = Files.readLines(new File(workingDir, "my.out"), Charset.forName("GBK"));
		current.setSquared(new BigDecimal(lines.get(1).split(" ")[1]));
		Formula formula = Formula.create().putConstant(lines.get(5).split(" ")[1])
				.putMultinomial(current.getCodeList().get(0), "1")
				.putMultinomial(current.getCodeList().get(1), lines.get(6).split("\\s+")[1]);
		current.setFormula(formula);
	}

	private void outputCsv(Date startDt, Date endDt, List<Futures> futuresList) throws Exception {
		TreeMap<Date, Map<String, KLine>> kLineMap = futuresList.stream()
				.map(futures -> kLineRepo.findByCodeAndDtBetweenOrderByDtAsc(futures.getCode(), startDt, endDt))
				.flatMap(kLineList -> kLineList.stream()).collect(Collectors.groupingBy(KLine::getDt, TreeMap::new,
						Collectors.toMap(KLine::getCode, Function.identity())));
		kLineMap = kLineMap.entrySet().stream().filter(entry -> entry.getValue().size() == futuresList.size())
				.collect(CollectorsHelper.toTreeMap(Map.Entry::getKey, Map.Entry::getValue));

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

	}

	public static void main(String[] args) {
		launch(BatchExpriment.class);
	}

	public class Chance implements Comparable<Chance> {

		private List<String> codeList;
		private BigDecimal squared;
		private Formula formula;

		public List<String> getCodeList() {
			return codeList;
		}

		public void setCodeList(List<String> codeList) {
			this.codeList = codeList;
		}

		public BigDecimal getSquared() {
			return squared;
		}

		public void setSquared(BigDecimal squared) {
			this.squared = squared;
		}

		public Formula getFormula() {
			return formula;
		}

		public void setFormula(Formula formula) {
			this.formula = formula;
		}

		@Override
		public int compareTo(Chance that) {
			return that.squared.compareTo(this.squared);
		}

		@Override
		public String toString() {
			return squared.toString() + "," + formula.toString();
		}

	}

}

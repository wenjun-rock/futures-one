package fwj.futures.data.strategy.hedging.model;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataURI;
import fwj.futures.resource.entity.price.KLine;
import fwj.futures.resource.entity.prod.Futures;
import fwj.futures.resource.repository.price.KLineRepository;
import fwj.futures.resource.repository.prod.FuturesRepository;

@Component
public class BatchExpriment extends AbstractBaseLaunch {

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private FuturesRepository futuresRepo;

	@Autowired
	private DataURI dataURI;

	@Override
	protected void execute() throws Exception {
		this.execute(12);
	}

	private void execute(int months) {
		Calendar cal = Calendar.getInstance();
		Date endDt = cal.getTime();
		cal.add(Calendar.MONTH, months);
		Date startDt = cal.getTime();

		List<Futures> futuresList = futuresRepo.findAllActive();
		for (int i = 0; i < futuresList.size(); i++) {
			for (int j = 0; j < futuresList.size(); j++) {
				this.execute(startDt, endDt, Arrays.asList(futuresList.get(i), futuresList.get(j)));
			}
		}

	}

	private void execute(Date startDt, Date endDt, List<Futures> futuresList) {
		Map<Date, Map<String, KLine>> kLineMap = futuresList.stream()
				.map(futures -> kLineRepo.findByCodeAndDtBetweenOrderByDtAsc(futures.getCode(), startDt, endDt))
				.flatMap(kLineList -> kLineList.stream())
				.collect(Collectors.groupingBy(KLine::getDt, Collectors.toMap(KLine::getCode, Function.identity())));
		kLineMap = kLineMap.entrySet().stream().filter(entry -> entry.getValue().size() == futuresList.size())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		
		Date latestDt = kLineMap.keySet().stream().max(Comparator.naturalOrder()).get();
		List<String> codeList = kLineMap.get(latestDt).entrySet().stream()
				.sorted((l, r) -> r.getValue().getEndPrice().compareTo(l.getValue().getEndPrice()))
				.map(Map.Entry::getKey).collect(Collectors.toList());

		String head = "DATE";
		for (int i = 0; i < codeList.size(); i++) {
			head += ",X" + (i + 1);
		}

		// kLineMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(mapper)

	}

	public static void main(String[] args) {
		launch(BatchExpriment.class);
	}

	public void exportEndPrice(String startDt, String endDt, ProdEnum... prods) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		exportEndPrice(df.parse(startDt), df.parse(endDt),
				Stream.of(prods).map(prod -> prod.getCode()).collect(Collectors.toList()));
	}

	public void exportEndPrice(Date startDt, Date endDt, List<String> codeList) throws Exception {
		Collections.sort(codeList);

		String head = codeList.stream().reduce("DATE", (l, r) -> l + "," + r);
		Map<Date, List<KLine>> kLineMap = codeList.stream()
				.map(code -> kLineRepo.findByCodeAndDtBetweenOrderByDtAsc(code, startDt, endDt))
				.flatMap(kLineList -> kLineList.stream())
				.collect(Collectors.groupingBy(KLine::getDt, Collectors.toList()));
		String content = kLineMap.entrySet().stream().filter(entry -> entry.getValue().size() == codeList.size())
				.sorted((l, r) -> l.getKey().compareTo(r.getKey())).map(entry -> {
					String dt = String.valueOf(entry.getKey().getTime());
					return entry.getValue().stream().sorted((l, r) -> l.getCode().compareTo(r.getCode()))
							.map(kLine -> kLine.getEndPrice().toString()).reduce(dt, (l, r) -> l + "," + r);
				}).reduce(head, (l, r) -> l + "\n" + r);

		String fileName = startDt + "_" + endDt + "_" + String.join("_", codeList);
		Files.asCharSink(dataURI.getModelFile(fileName), StandardCharsets.UTF_8).write(content);
	}
}

package fwj.futures.data.strategy.hedging.model;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;

import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.data.process.DataURI;
import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.repository.KLineRepository;

@Component
public class ExportOrigin extends AbstractBaseLaunch {

	@Autowired
	private KLineRepository kLineRepository;
	
	@Autowired
	private DataURI dataURI;

	@Override
	protected void execute() throws Exception {
		this.exportEndPrice("2015-01-01", "2015-09-02", ProdEnum.JiaoTan, ProdEnum.JiaoMei);
	}

	public static void main(String[] args) {
		launch(ExportOrigin.class);
	}

	public void exportEndPrice(String startDt, String endDt, ProdEnum... prods) throws Exception {
		exportEndPrice(startDt, endDt, Stream.of(prods).map(prod -> prod.getCode()).collect(Collectors.toList()));
	}

	public void exportEndPrice(String startDt, String endDt, List<String> codeList) throws Exception {
		Collections.sort(codeList);

		String head = codeList.stream().reduce("DATE", (l, r) -> l + "," + r);
		Map<String, List<KLine>> kLineMap = codeList.stream()
				.map(code -> kLineRepository.findByCodeDateRange(code, startDt, endDt))
				.flatMap(kLineList -> kLineList.stream())
				.collect(Collectors.groupingBy(KLine::getDt, Collectors.toList()));
		String content = kLineMap.entrySet().stream().filter(entry -> entry.getValue().size() == codeList.size())
				.sorted((l, r) -> l.getKey().compareTo(r.getKey())).map(entry -> {
					String dt = entry.getKey();
					return entry.getValue().stream().sorted((l, r) -> l.getCode().compareTo(r.getCode()))
							.map(kLine -> kLine.getEndPrice().toString()).reduce(dt, (l, r) -> l + "," + r);
				}).reduce(head, (l, r) -> l + "\n" + r);
		
		String fileName = startDt + "_" + endDt + "_" + String.join("_", codeList);
		Files.asCharSink(dataURI.getModelFile(fileName), StandardCharsets.UTF_8).write(content);
	}
}

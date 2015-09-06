package cn.fwj.futures.data.process;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Files;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.vo.DailyK;
import cn.fwj.futures.data.vo.DailyKLine;
import cn.fwj.futures.data.vo.Formula;
import cn.fwj.futures.data.vo.XYLine;

@Component
public class DataProcessor {

	@Autowired
	private DataURI dataURI;

	private DailyKLine loadDailyKLine(Product prod) throws IOException {
		DailyKLine kLine = new DailyKLine(prod);
		String jsonStr = Files.asCharSource(dataURI.getLatestDailyKLineFile(prod), StandardCharsets.UTF_8).read();
		JSONArray dailyKs = JSON.parseArray(jsonStr);
		for (int i = 0; i < dailyKs.size(); i++) {
			JSONArray ele = dailyKs.getJSONArray(i);
			DailyK daily = new DailyK();
			daily.setDt(ele.getString(0));
			daily.setOpenPrice(ele.getBigDecimal(1));
			daily.setMaxPrice(ele.getBigDecimal(2));
			daily.setMinPrice(ele.getBigDecimal(3));
			daily.setEndPrice(ele.getBigDecimal(4));
			daily.setTradeVol(ele.getLong(5));
			kLine.setDailyK(daily.getDt(), daily);
		}
		return kLine;
	}

	private List<DailyKLine> loadAllDailyKLines() throws IOException {
		List<DailyKLine> dailyKLines = new ArrayList<>();
		for (Product prod : Product.values()) {
			dailyKLines.add(loadDailyKLine(prod));
		}
		return dailyKLines;
	}

	private List<DailyKLine> loadDailyKLines(Product[] prods) throws IOException {
		List<DailyKLine> dailyKLines = new ArrayList<>();
		for (Product prod : prods) {
			dailyKLines.add(loadDailyKLine(prod));
		}
		return dailyKLines;
	}

	private List<String> tableFormular(String startDt, String endDt, List<XYLine> xyLineList, Formula formula)
			throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse(startDt);
		Date end = df.parse(endDt);

		List<String> lines = new ArrayList<>();
		lines.add("DATE,DIFF");

		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		while (!cal.getTime().after(end)) {
			String dt = df.format(cal.getTime());
			boolean miss = false;
			BigDecimal diff = formula.getConstant();
			for (XYLine xyLine : xyLineList) {
				BigDecimal val = xyLine.getXy().get(dt);
				if (val == null) {
					miss = true;
					break;
				} else {
					diff = diff.add(val.multiply(formula.getMultinomials().get(xyLine.getProd())));
				}
			}
			if (!miss) {
				lines.add(dt + "," + diff);
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return lines;
	}

	private List<String> table(String startDt, String endDt, List<XYLine> xyLineList) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse(startDt);
		Date end = df.parse(endDt);

		List<Map<String, BigDecimal>> xys = new ArrayList<>();
		List<String> lines = new ArrayList<>();
		String head = "DATE";
		for (XYLine trend : xyLineList) {
			head += "," + trend.getProd().getCode();
			xys.add(trend.nomalize(startDt, endDt));
		}
		lines.add(head);

		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		while (!cal.getTime().after(end)) {
			String dt = df.format(cal.getTime());
			String line = dt;
			boolean miss = false;
			for (Map<String, BigDecimal> xy : xys) {
				BigDecimal val = xy.get(dt);
				if (val == null) {
					miss = true;
					break;
				} else {
					line += "," + val;
				}
			}
			if (!miss) {
				lines.add(line);
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return lines;
	}

	private List<XYLine> toEndPriceXYLine(List<DailyKLine> kLineList) {
		return toEndPriceXYLine(kLineList, 0);
	}

	private List<XYLine> toEndPriceXYLine(List<DailyKLine> kLineList, int range) {
		List<XYLine> xyLineList = new ArrayList<>();
		for (DailyKLine kLine : kLineList) {
			xyLineList.add(kLine.createEndPriceXYLine(range));
		}
		return xyLineList;
	}

	public void exportEndPrice(String startDt, String endDt, Product... prods) throws Exception {
		List<DailyKLine> kLineList = prods == null ? this.loadAllDailyKLines() : this.loadDailyKLines(prods);
		List<XYLine> xyLineList = this.toEndPriceXYLine(kLineList);
		List<String> lines = this.table(startDt, endDt, xyLineList);
		String fileName = startDt + "_" + endDt + "_" + kLineList.size();
		for (DailyKLine kLine : kLineList) {
			fileName += "_" + kLine.getProd().getCode();
		}
		Files.asCharSink(dataURI.getModelFile(fileName), StandardCharsets.UTF_8).writeLines(lines);
	}

	public void exportEndPriceNormal(String startDt, String endDt, int range, Product... prods) throws Exception {
		List<DailyKLine> kLineList = prods == null ? this.loadAllDailyKLines() : this.loadDailyKLines(prods);
		List<XYLine> xyLineList = this.toEndPriceXYLine(kLineList, range);
		List<String> lines = this.table(startDt, endDt, xyLineList);
		String fileName = "N_" + startDt + "_" + endDt + "_" + kLineList.size();
		for (DailyKLine kLine : kLineList) {
			fileName += "_" + kLine.getProd().getCode();
		}
		Files.asCharSink(dataURI.getModelFile(fileName), StandardCharsets.UTF_8).writeLines(lines);
	}

	public void testEndPriceFormula(String startDt, String endDt, Formula formula) throws Exception {
		Product[] prods = formula.getMultinomials().keySet().toArray(new Product[] {});
		List<DailyKLine> kLineList = this.loadDailyKLines(prods);
		List<XYLine> xyLineList = this.toEndPriceXYLine(kLineList);
		List<String> lines = this.tableFormular(startDt, endDt, xyLineList, formula);
		String fileName = "T_" + startDt + "_" + endDt + "_" + kLineList.size();
		for (DailyKLine kLine : kLineList) {
			fileName += "_" + kLine.getProd().getCode();
		}
		Files.asCharSink(dataURI.getTestFile(fileName), StandardCharsets.UTF_8).writeLines(lines);
	}

	public void monitorEndPriceFormula(String startDt, Formula formula) throws Exception {
		String endDt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Product[] prods = formula.getMultinomials().keySet().toArray(new Product[] {});
		List<DailyKLine> kLineList = this.loadDailyKLines(prods);
		List<XYLine> xyLineList = this.toEndPriceXYLine(kLineList);
		List<String> lines = this.tableFormular(startDt, endDt, xyLineList, formula);
		String fileName = "M_" + startDt + "_" + endDt + "_" + kLineList.size();
		for (DailyKLine kLine : kLineList) {
			fileName += "_" + kLine.getProd().getCode();
		}
		Files.asCharSink(dataURI.getMonitorFile(fileName), StandardCharsets.UTF_8).writeLines(lines);

	}

}

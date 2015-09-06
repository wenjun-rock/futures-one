package cn.fwj.futures.data.repository;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Files;

import cn.fwj.futures.data.enu.Product;
import cn.fwj.futures.data.struct.DailyK;
import cn.fwj.futures.data.struct.DailyKLine;

@Component
public class KLineRepo {

	public List<DailyKLine> loadAllDailyKLines(boolean includeAll) throws IOException {
		List<DailyKLine> dailyKLines = new ArrayList<>();
		for (Product prod : Product.values()) {
			dailyKLines.add(loadDailyKLine(prod, includeAll));
		}
		return dailyKLines;
	}

	public List<DailyKLine> loadAllDailyKLines() throws IOException {
		return loadAllDailyKLines(true);
	}

	public List<DailyKLine> loadDailyKLines(Product[] prods, boolean includeAll) throws IOException {
		List<DailyKLine> dailyKLines = new ArrayList<>();
		for (Product prod : prods) {
			dailyKLines.add(loadDailyKLine(prod, includeAll));
		}
		return dailyKLines;
	}

	public List<DailyKLine> loadDailyKLines(Product[] prods) throws IOException {
		return loadDailyKLines(prods, true);
	}

	public DailyKLine loadDailyKLine(Product prod, boolean includeAll) throws IOException {
		String jsonStr = Files.asCharSource(this.getLatestDailyKLineFile(prod), StandardCharsets.UTF_8).read();
		JSONArray dailyKs = JSON.parseArray(jsonStr);

		DailyKLine kLine = new DailyKLine(prod);
		for (int i = 0; i < dailyKs.size(); i++) {
			JSONArray ele = dailyKs.getJSONArray(i);
			DailyK daily = new DailyK();
			daily.setDt(ele.getString(0));
			daily.setOpenPrice(ele.getBigDecimal(1));
			daily.setMaxPrice(ele.getBigDecimal(2));
			daily.setMinPrice(ele.getBigDecimal(3));
			daily.setEndPrice(ele.getBigDecimal(4));
			daily.setTradeVol(ele.getLong(5));
			if (includeAll || daily.getTradeVol() > 0) {
				kLine.setDailyK(daily.getDt(), daily);
			}
		}
		return kLine;
	}

	public DailyKLine loadDailyKLine(Product prod) throws IOException {
		return loadDailyKLine(prod, true);
	}

	private File getLatestDailyKLineFile(final Product prod) throws IOException {
		File dir = new File(URIConstants.DATA_RAW_DIR);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				int start = name.lastIndexOf("_") + 1;
				int end = name.indexOf(".");
				if ((start < end) && name.substring(start, end).equals(prod.getCode())) {
					return true;
				} else {
					return false;
				}
			}
		});
		File latestFile = null;
		for (File file : files) {
			if (latestFile == null || file.getName().compareTo(latestFile.getName()) > 0) {
				latestFile = file;
			}
		}
		return latestFile;
	}

}

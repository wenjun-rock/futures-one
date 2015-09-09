package fwj.futures.data.process;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import fwj.futures.data.enu.Product;

@Component
public class DataURI {

	private final static String DATA_URL_SINA = "http://stock2.finance.sina.com.cn/futures/api/json.php";
	private final static String DATA_RAW_DIR = "F:/futures/data/raw";
	private final static String DATA_MODEL_DIR = "F:/futures/data/model";
	private final static String DATA_TEST_DIR = "F:/futures/data/test";
	private final static String DATA_MONITOR_DIR = "F:/futures/data/monitor";

	public URL getDailyKLineUrl(Product prod) throws IOException {
		return new URL(
				String.format("%s/IndexService.getInnerFuturesDailyKLine?symbol=%s0", DATA_URL_SINA, prod.getCode()));
	}

	public File getLatestDailyKLineFile(final Product prod) throws IOException {
		File dir = new File(DATA_RAW_DIR);
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

	public File getDailyKLineFile(Product prod, String dateStr) {
		return new File(String.format("%s/%s_%s.json", DATA_RAW_DIR, dateStr, prod.getCode()));
	}

	public File getDailyKLineFile(Product prod, Date date) {
		return getDailyKLineFile(prod, new SimpleDateFormat("yyyy-MM-dd").format(date));
	}
	
	public File getModelFile(String fileName) {
		return new File(String.format("%s/%s.csv", DATA_MODEL_DIR, fileName));
	}
	
	public File getTestFile(String fileName) {
		return new File(String.format("%s/%s.csv", DATA_TEST_DIR, fileName));
	}
	
	public File getMonitorFile(String fileName) {
		return new File(String.format("%s/%s.csv", DATA_MONITOR_DIR, fileName));
	}

}

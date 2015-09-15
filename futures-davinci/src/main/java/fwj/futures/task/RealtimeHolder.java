package fwj.futures.task;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.io.Resources;

import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.ProductRepository;

@Component
public class RealtimeHolder {

	@Autowired
	private ProductRepository productRepo;

	private Logger log = Logger.getLogger(this.getClass());
	private int index = 0;
	private int rest = 0;
	private boolean first = true;
	private boolean running = false;
	private String[] loopCache = null;

	@Scheduled(cron = "0 */1 * * * ?")
	public void refresh() {

		if (--rest > 0 || running) {
			log.info("skip refresh!");
			return;
		}
		running = true;
		if (first) {
			init();
		} else {
			update();
		}
		running = false;
	}

	private void update() {
		// TODO Auto-generated method stub

	}

	private void init() {
		try {
			List<Product> prodList = productRepo.findAllActive();
			List<String> resultList = new ArrayList<>();
			for (Product prod : prodList) {
				String uri = String.format(
						"http://stock2.finance.sina.com.cn/futures/api/json.php/IndexService.getInnerFuturesMiniKLine5m?symbol=%s0",
						prod.getCode());
				resultList.add(Resources.toString(new URL(uri), StandardCharsets.UTF_8));
			}
		} catch (IOException e) {
			log.error("wrong!", e);
		}

	}
}

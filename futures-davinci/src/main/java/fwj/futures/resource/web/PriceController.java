package fwj.futures.resource.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.ProductRepository;
import fwj.futures.task.RealtimeHolder;

@RestController()
@RequestMapping("/price")
public class PriceController {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private RealtimeHolder rtHolder;

	@RequestMapping("/daily/{codes}")
	public List<Price> findDailyByCodes(@PathVariable("codes") String codes) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return Stream.of(codes.split(",")).map(code -> {
			Product prod = productRepo.findByCode(code);
			if (prod == null) {
				return new Price();
			} else {
				List<KLine> kLineList = kLineRepo.findByCode(code);
				Object[][] data = new Object[kLineList.size()][2];
				for (int i = 0; i < kLineList.size(); i++) {
					KLine kline = kLineList.get(i);
					try {
						data[i][0] = df.parse(kline.getDt()).getTime();
						data[i][1] = kline.getEndPrice();
					} catch (Exception e) {
						log.error("", e);
					}
				}
				return new Price(prod, data);
			}
		}).collect(Collectors.toList());
	}

	@RequestMapping("/realtime/{codes}")
	public List<Price> findRealtimeByCodes(@PathVariable("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> {
			Product prod = productRepo.findByCode(code);
			if (prod == null) {
				return new Price();
			} else {
				return rtHolder.findRealtimeByCode(prod);
			}
		}).collect(Collectors.toList());
	}

	public static class Price {
		private String name;
		private String code;
		private Object[][] data;

		public Price(Product prod, Object[][] data) {
			this.name = prod.getCode() + "(" + prod.getName() + ")";
			this.code = prod.getCode();
			this.data = data;
		}

		public Price() {
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}

		public Object[][] getData() {
			return data;
		}
	}

}

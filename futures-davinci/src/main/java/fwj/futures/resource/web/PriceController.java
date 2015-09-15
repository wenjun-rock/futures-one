package fwj.futures.resource.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fwj.futures.resource.entity.KLine;
import fwj.futures.resource.entity.Product;
import fwj.futures.resource.repository.KLineRepository;
import fwj.futures.resource.repository.ProductRepository;

@RestController()
@RequestMapping("/price")
public class PriceController {

	@Autowired
	private KLineRepository kLineRepo;

	@Autowired
	private ProductRepository productRepo;

	@RequestMapping("/daily/{codes}")
	public List<HistPrice> findDailyByCodes(@PathVariable("codes") String codes) {
		return Stream.of(codes.split(",")).map(code -> {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			Product prod = productRepo.findByCode(code);
			if (prod == null) {
				return new HistPrice();
			} else {
				List<KLine> kLineList = kLineRepo.findByCode(code);
				Object[][] data = new Object[kLineList.size()][2];
				for (int i = 0; i < kLineList.size(); i++) {
					KLine kline = kLineList.get(i);
					try {
						data[i][0] = df.parse(kline.getDt()).getTime();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					data[i][1] = kline.getEndPrice();
				}
				return new HistPrice(prod, data);
			}
		}).collect(Collectors.toList());
	}
	
	@RequestMapping("/realtime/{codes}")
	public List<HistPrice> findRealtimeByCodes(@PathVariable("codes") String codes) {
		


		
		return Stream.of(codes.split(",")).map(code -> {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			Product prod = productRepo.findByCode(code);
			if (prod == null) {
				return new HistPrice();
			} else {
				List<KLine> kLineList = kLineRepo.findByCode(code);
				Object[][] data = new Object[kLineList.size()][2];
				for (int i = 0; i < kLineList.size(); i++) {
					KLine kline = kLineList.get(i);
					try {
						data[i][0] = df.parse(kline.getDt()).getTime();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					data[i][1] = kline.getEndPrice();
				}
				return new HistPrice(prod, data);
			}
		}).collect(Collectors.toList());
	}

	public static class HistPrice {
		private String name;
		private String code;
		private Object[][] data;

		public HistPrice(Product prod, Object[][] data) {
			this.name = prod.getCode() + "(" + prod.getName() + ")";
			this.code = prod.getCode();
			this.data = data;
		}

		public HistPrice() {
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

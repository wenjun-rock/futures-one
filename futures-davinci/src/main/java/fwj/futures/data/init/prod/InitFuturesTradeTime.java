package fwj.futures.data.init.prod;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.data.enu.Exchange;
import fwj.futures.data.enu.ProdEnum;
import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.entity.prod.FuturesTradeTime;
import fwj.futures.resource.repository.prod.FuturesTradeTimeRepository;

@Component
public class InitFuturesTradeTime extends AbstractBaseLaunch {

	@Autowired
	private FuturesTradeTimeRepository tradeTimeRepository;

	@Override
	protected void execute() throws Exception {

		Range _0900_1130 = new Range("0900", "1130", 25);
		Range _1330_1500 = new Range("1330", "1500", 15);
		Range _2100_2300 = new Range("2100", "2300", 20);
		Range _2100_2330 = new Range("2100", "2330", 25);
		Range _2100_0100 = new Range("2100", "0100", 40);
		Range _2100_0230 = new Range("2100", "0230", 55);

		Input[] inputs = new Input[] { //
				new Input(Exchange.SH.name(), _0900_1130, _1330_1500, _2100_0100), //
				new Input(Exchange.DL.name(), _0900_1130, _1330_1500, _2100_2330), //
				new Input(Exchange.ZZ.name(), _0900_1130, _1330_1500, _2100_2330), //

				// SH
				new Input(ProdEnum.Baiyin.getCode(), _0900_1130, _1330_1500, _2100_0230), //
				new Input(ProdEnum.HuangJin.getCode(), _0900_1130, _1330_1500, _2100_0230), //
				new Input(ProdEnum.XiangJiao.getCode(), _0900_1130, _1330_1500, _2100_2300), //
				// DL
				new Input(ProdEnum.YuMi.getCode(), _0900_1130, _1330_1500), //
				new Input(ProdEnum.YuDian.getCode(), _0900_1130, _1330_1500), //
				new Input(ProdEnum.JiDan.getCode(), _0900_1130, _1330_1500), //
				new Input(ProdEnum.SuLiao.getCode(), _0900_1130, _1330_1500), //
				new Input(ProdEnum.JuBinXi.getCode(), _0900_1130, _1330_1500), //
				new Input(ProdEnum.JuLvYiXi.getCode(), _0900_1130, _1330_1500), //
				// ZZ
				new Input(ProdEnum.QiangMai.getCode(), _0900_1130, _1330_1500), //

		};
		Map<String, Input> map = Stream.of(inputs).collect(Collectors.toMap(input -> input.code, input -> input));

		tradeTimeRepository.deleteAllInBatch();
		for (ProdEnum ele : ProdEnum.values()) {
			Range[] ranges = null;
			if (map.containsKey(ele.getCode())) {
				ranges = map.get(ele.getCode()).ranges;
			} else {
				ranges = map.get(ele.getExchange().name()).ranges;
			}
			Stream.of(ranges).forEach(range -> {
				FuturesTradeTime entity = new FuturesTradeTime();
				entity.setCode(ele.getCode());
				entity.setStartTime(range.start);
				entity.setEndTime(range.end);
				entity.setDecHours(range.decHours);
				tradeTimeRepository.save(entity);
			});
			tradeTimeRepository.flush();
		}

		System.out.println("done!");

	}

	public static void main(String[] args) {
		launch(InitFuturesTradeTime.class);
	}

	private class Input {
		private String code;
		private Range[] ranges;

		private Input(String code, Range... ranges) {
			this.code = code;
			this.ranges = ranges;
		}
	}

	private class Range {
		private String start;
		private String end;
		private int decHours;

		private Range(String start, String end, int decHours) {
			this.start = start;
			this.end = end;
			this.decHours = decHours;
		}
	}
}

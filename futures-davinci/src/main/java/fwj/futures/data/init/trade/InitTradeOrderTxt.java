package fwj.futures.data.init.trade;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import fwj.futures.data.launch.AbstractBaseLaunch;
import fwj.futures.resource.trade.entity.TradeOrder;
import fwj.futures.resource.trade.enu.TradeOrderType;
import fwj.futures.resource.trade.repos.TradeOrderRepos;

@Component
public class InitTradeOrderTxt extends AbstractBaseLaunch {

	@Autowired
	private TradeOrderRepos tradeOrderRepos;

	@Override
	protected void execute() throws Exception {
		List<TradeOrder> importList = new ArrayList<>();
		// |成交日期| 交易所 | 品种 | 合约 |买/卖| 投/保 | 成交价 | 手数 | 成交额 | 开平 | 手续费 | 平仓盈亏
		// | 权利金收支 | 成交序号 |

		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		int index = 1;

		BufferedReader br = new BufferedReader(new FileReader("f:/working.txt"));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] eles = line.split("\\|");
			if (eles.length != 15) {
				continue;
			}

			TradeOrder order = new TradeOrder();

			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(eles[1]));
			cal.add(Calendar.MINUTE, index);
			order.setTradeDt(cal.getTime());
			order.setConCode(eles[4].trim().toUpperCase());
			order.setSerialNo(eles[14].trim());
			String type = eles[5].trim() + eles[10].trim().substring(0, 1);
			order.setType(TradeOrderType.getByName(type).getCode());
			order.setPrice(new BigDecimal(eles[7].trim()));
			order.setVol(Integer.parseInt(eles[8].trim()));
			order.setAmount(new BigDecimal(eles[9].trim()));
			order.setFee(new BigDecimal(eles[11].trim()));
			if (eles[10].trim().startsWith("平")) {
				order.setProfit(new BigDecimal(eles[12].trim()));
			}
			importList.add(order);
			System.out.println(JSON.toJSONString(order, false));
			index++;
		}
		br.close();
		int sum = importList.stream().mapToInt(tradeOrder -> {
			if (tradeOrderRepos.findBySerialNo(tradeOrder.getSerialNo()) != null) {
				// 已存在
				return 0;
			} else {
				tradeOrderRepos.save(tradeOrder);
				return 1;
			}
		}).sum();
		System.out.println(sum);

	}

	public static void main(String[] args) {
		launch(InitTradeOrderTxt.class);
	}
}

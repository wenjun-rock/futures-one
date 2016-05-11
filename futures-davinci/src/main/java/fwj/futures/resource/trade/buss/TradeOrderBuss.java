package fwj.futures.resource.trade.buss;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.trade.entity.TradeOrder;
import fwj.futures.resource.trade.enu.TradeOrderType;
import fwj.futures.resource.trade.repos.TradeOrderRepos;

@Component
public class TradeOrderBuss {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TradeOrderRepos tradeOrderRepos;

	public List<TradeOrder> listTradeOrder() {
		return tradeOrderRepos.findAll();

	}

	public int saveTradeOrderInExcel(byte[] bytes) throws Exception {
		List<TradeOrder> importList = this.getImportList(bytes);
		return importList.stream().mapToInt(tradeOrder -> {
			if(tradeOrderRepos.findBySerialNo(tradeOrder.getSerialNo()) != null) {
				// 已存在
				return 0;
			} else {
				tradeOrderRepos.save(tradeOrder);
				return 1;
			}
		}).sum();
	}

	private List<TradeOrder> getImportList(byte[] bytes) throws Exception {
		List<TradeOrder> importList = new ArrayList<>();
		Workbook wb = null;
		try {
			// 交易日期 合约 成交序号 成交时间
			// 买/卖 投机/套保 成交价 手数
			// 成交额 开/平 手续费 平仓盈亏
			// 实际成交日期
			wb = new HSSFWorkbook(new ByteArrayInputStream(bytes));
			Sheet sheet = wb.getSheetAt(0);
			int start = sheet.getFirstRowNum() + 1;
			int end = sheet.getLastRowNum();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = start; i <= end; i++) {
				try {
					Row row = sheet.getRow(i);
					TradeOrder order = new TradeOrder();
					String datetime = row.getCell(0).getStringCellValue() + " " + row.getCell(3).getStringCellValue();
					order.setTradeDt(df.parse(datetime));
					order.setConCode(row.getCell(1).getStringCellValue());
					order.setSerialNo(row.getCell(2).getStringCellValue());
					String type = row.getCell(4).getStringCellValue().trim()
							+ row.getCell(9).getStringCellValue().trim();
					order.setType(TradeOrderType.getByName(type).getCode());
					order.setPrice(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));
					order.setVol((int)row.getCell(7).getNumericCellValue());
					order.setAmount(BigDecimal.valueOf(row.getCell(8).getNumericCellValue()));
					order.setFee(BigDecimal.valueOf(row.getCell(10).getNumericCellValue()));
					if(row.getCell(11).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						order.setProfit(BigDecimal.valueOf(row.getCell(11).getNumericCellValue()));
					}
					importList.add(order);
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
		return importList;
	}

}
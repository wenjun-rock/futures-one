package fwj.futures.resource.trade.buss;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fwj.futures.resource.trade.entity.TradeGroup;
import fwj.futures.resource.trade.entity.TradeGroupOrder;
import fwj.futures.resource.trade.entity.TradeOrder;
import fwj.futures.resource.trade.enu.TradeOrderType;
import fwj.futures.resource.trade.repos.TradeGroupOrderRepos;
import fwj.futures.resource.trade.repos.TradeGroupRepos;
import fwj.futures.resource.trade.repos.TradeOrderRepos;
import fwj.futures.resource.trade.vo.TradeGroupAssignView;
import fwj.futures.resource.trade.vo.TradeGroupView;

@Component
public class TradeOrderBuss {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TradeOrderRepos tradeOrderRepos;
	
	@Autowired
	private TradeGroupRepos tradeGroupRepos;
	
	@Autowired
	private TradeGroupOrderRepos tradeGroupOrderRepos;

	public List<TradeOrder> listTradeOrder() {
		return tradeOrderRepos.findOrderByTradeDtDesc();
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
			// 合约 成交序号 成交时间 买/卖
			// 投机/套保 成交价 手数 成交额
			// 开/平 手续费 平仓盈亏 实际成交日期
			wb = new HSSFWorkbook(new ByteArrayInputStream(bytes));
			Sheet sheet = wb.getSheetAt(0);
			int start = sheet.getFirstRowNum() + 1;
			int end = sheet.getLastRowNum();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = start; i <= end; i++) {
				try {
					Row row = sheet.getRow(i);
					TradeOrder order = new TradeOrder();
					String datetime = row.getCell(11).getStringCellValue() + " " + row.getCell(2).getStringCellValue();
					order.setTradeDt(df.parse(datetime));
					order.setConCode(row.getCell(0).getStringCellValue());
					order.setSerialNo(row.getCell(1).getStringCellValue());
					String type = row.getCell(3).getStringCellValue().trim()
							+ row.getCell(8).getStringCellValue().trim();
					order.setType(TradeOrderType.getByName(type).getCode());
					order.setPrice(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));
					order.setVol((int)row.getCell(6).getNumericCellValue());
					order.setAmount(BigDecimal.valueOf(row.getCell(7).getNumericCellValue()));
					order.setFee(BigDecimal.valueOf(row.getCell(9).getNumericCellValue()));
					if(row.getCell(10).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						order.setProfit(BigDecimal.valueOf(row.getCell(10).getNumericCellValue()));
					}
					if(row.getCell(12) != null) {
						order.setComment(row.getCell(12).getStringCellValue());
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

	public TradeGroupView addTradeGroup(TradeGroupView body) {
		TradeGroup tradeGroup = new TradeGroup();
		tradeGroup.setName(body.getName());
		tradeGroup.setComment(body.getComment());
		TradeGroup tradeNewGroup = tradeGroupRepos.saveAndFlush(tradeGroup);
		body.setId(tradeNewGroup.getId());
		return body;
	}

	public void assignTradeOrder(TradeGroupAssignView body) {
		tradeGroupOrderRepos.deleteByGroupId(body.getGroupId());
		Stream.of(body.getOrderIds()).forEach(orderId -> {
			TradeGroupOrder obj = new TradeGroupOrder();
			obj.setGroupId(body.getGroupId());
			obj.setOrderId(orderId);
			tradeGroupOrderRepos.save(obj);
		});
		tradeGroupOrderRepos.flush();
		return;
	}

	public List<TradeGroupView> listTradeGroup() {
		return tradeGroupRepos.findAll().stream().map(group -> {
			TradeGroupView view = new TradeGroupView();
			view.setId(group.getId());
			view.setName(group.getName());
			view.setComment(group.getComment());
			view.setOrders(tradeOrderRepos.findByGroupId(group.getId()));
			return view;
		}).collect(Collectors.toList());
	}

}

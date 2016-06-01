package fwj.futures.resource.trade.buss;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import fwj.futures.resource.trade.entity.TradeOrder;
import fwj.futures.resource.trade.enu.TradeOrderType;
import fwj.futures.resource.trade.repos.TradeGroupRepos;
import fwj.futures.resource.trade.repos.TradeOrderRepos;
import fwj.futures.resource.trade.vo.TradeGroupAssignReq;
import fwj.futures.resource.trade.vo.TradeGroupView;
import fwj.futures.resource.trade.vo.TradeGroupView.Element;
import fwj.futures.resource.trade.vo.TradeOrderAssignReq;
import fwj.futures.resource.trade.vo.TradeOrderView;

@Component
public class TradeOrderBuss {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private TradeOrderRepos tradeOrderRepos;

	@Autowired
	private TradeGroupRepos tradeGroupRepos;

	public List<TradeOrderView> listTradeOrder() {
		return tradeOrderRepos.findOrderByTradeDtDesc().stream().map(order -> {
			TradeOrderView view = new TradeOrderView();
			view.setId(order.getId());
			view.setConCode(order.getConCode());
			view.setTradeDt(order.getTradeDt());
			view.setType(order.getType());
			view.setPrice(order.getPrice());
			view.setVol(order.getVol());
			view.setAmount(order.getAmount());
			view.setFee(order.getFee());
			view.setProfit(order.getProfit());
			view.setComment(order.getComment());
			view.setGroupId(order.getTradeGroup() != null ? order.getTradeGroup().getId() : null);
			view.setGroupName(order.getTradeGroup() != null ? order.getTradeGroup().getName() : null);
			return view;
		}).collect(Collectors.toList());
	}

	public int saveTradeOrderInExcel(byte[] bytes) throws Exception {
		List<TradeOrder> importList = this.getImportList(bytes);
		return importList.stream().mapToInt(tradeOrder -> {
			if (tradeOrderRepos.findBySerialNo(tradeOrder.getSerialNo()) != null) {
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
					Date tradeDt = df.parse(datetime);
					if (row.getCell(2).getStringCellValue().substring(0, 2).compareTo("20") > 0) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(tradeDt);
						cal.add(Calendar.DATE, -1);
						tradeDt = cal.getTime();
					}
					order.setTradeDt(tradeDt);
					order.setConCode(row.getCell(0).getStringCellValue());
					order.setSerialNo(row.getCell(1).getStringCellValue());
					String type = row.getCell(3).getStringCellValue().trim()
							+ row.getCell(8).getStringCellValue().trim();
					order.setType(TradeOrderType.getByName(type).getCode());
					order.setPrice(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));
					order.setVol((int) row.getCell(6).getNumericCellValue());
					order.setAmount(BigDecimal.valueOf(row.getCell(7).getNumericCellValue()));
					order.setFee(BigDecimal.valueOf(row.getCell(9).getNumericCellValue()));
					if (row.getCell(10).getCellType() == Cell.CELL_TYPE_NUMERIC) {
						order.setProfit(BigDecimal.valueOf(row.getCell(10).getNumericCellValue()));
					}
					if (row.getCell(12) != null) {
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

	public void assignTradeOrder(TradeOrderAssignReq body) {
		TradeOrder tradeOrder = tradeOrderRepos.findOne(body.getOrderId());
		TradeGroup tradeGroup = tradeGroupRepos.findOne(body.getGroupId());
		if (tradeOrder != null && tradeGroup != null) {
			tradeOrder.setTradeGroup(tradeGroup);
			tradeOrderRepos.saveAndFlush(tradeOrder);
		}
		return;
	}

	public void assignTradeGroup(TradeGroupAssignReq body) {
		TradeGroup tradeGroup = tradeGroupRepos.findOne(body.getGroupId());
		if (tradeGroup == null) {
			return;
		}
		tradeOrderRepos.findByTradeGroupOrderByTradeDtDesc(tradeGroup).stream().forEach(order -> {
			order.setTradeGroup(null);
			tradeOrderRepos.save(order);
		});
		Stream.of(body.getOrderIds()).forEach(orderId -> {
			TradeOrder order = tradeOrderRepos.findOne(orderId);
			order.setTradeGroup(tradeGroup);
			tradeOrderRepos.save(order);
		});
		tradeOrderRepos.flush();
		return;
	}

	public List<TradeGroupView> listTradeGroup(boolean orderFlag) {
		return tradeGroupRepos.findAll().stream().map(group -> {
			TradeGroupView view = new TradeGroupView();
			view.setId(group.getId());
			view.setName(group.getName());
			view.setComment(group.getComment());
			
			if(orderFlag == false) {
				return view;
			}
			
			List<TradeOrder> orderList = tradeOrderRepos.findByTradeGroupOrderByTradeDtAsc(group);
			view.setAmount(BigDecimal.ZERO);
			view.setFee(BigDecimal.ZERO);
			view.setProfit(BigDecimal.ZERO);
			view.setVol(0);
			view.setOpenVol(0);
			List<Element> elmts = new ArrayList<>();
			Map<String, Deque<TradeOrder>> map = new HashMap<>();
			orderList.forEach(order -> {
				if (view.getFirstTradeDt() == null || view.getFirstTradeDt().after(order.getTradeDt())) {
					view.setFirstTradeDt(order.getTradeDt());
				}
				if (view.getLastTradeDt() == null || view.getLastTradeDt().before(order.getTradeDt())) {
					view.setLastTradeDt(order.getTradeDt());
				}
				view.setFee(view.getFee().add(order.getFee()));
				view.setAmount(view.getAmount().add(order.getAmount()));
				if (order.getProfit() != null) {
					view.setProfit(view.getProfit().add(order.getProfit()));
				}
				view.setVol(view.getVol() + order.getVol());
				view.setOpenVol((order.getType() == 1 || order.getType() == 2) ? (view.getOpenVol() + order.getVol())
						: (view.getOpenVol() - order.getVol()));

				Deque<TradeOrder> stack = map.get(order.getConCode());
				if (stack == null) {
					stack = new ArrayDeque<>();
					map.put(order.getConCode(), stack);
					for (int i = 0; i < order.getVol(); i++) {
					}
				}
				if (order.getType() == 1 || order.getType() == 2) {
					for (int i = 0; i < order.getVol(); i++) {
						stack.push(order);
					}
				} else {
					for (int i = 0; i < order.getVol(); i++) {
						if(stack .isEmpty()) {
							return;
						}
						TradeOrder openOrder = stack.pop();
						Element ele = new Element();
						ele.setConCode(order.getConCode());
						ele.setOpenDt(openOrder.getTradeDt());
						ele.setOpenPrice(openOrder.getPrice());
						ele.setCloseDt(order.getTradeDt());
						ele.setClosePrice(order.getPrice());
						ele.setType(openOrder.getType());
						ele.setDiffPrice(openOrder.getType() == 1 ? (order.getPrice().subtract(openOrder.getPrice()))
								: (openOrder.getPrice().subtract(order.getPrice())));
						elmts.add(ele);
					}
				}

			});
			for(Map.Entry<String, Deque<TradeOrder>> entry : map.entrySet()) {
				Deque<TradeOrder> stack = entry.getValue();
				while(!stack.isEmpty()) {
					TradeOrder openOrder = stack.pop();
					Element ele = new Element();
					ele.setConCode(openOrder.getConCode());
					ele.setOpenDt(openOrder.getTradeDt());
					ele.setOpenPrice(openOrder.getPrice());
					ele.setType(openOrder.getType());
					elmts.add(ele);
				}
			}
			view.setElmts(elmts);
			return view;
		}).collect(Collectors.toList());
	}

}

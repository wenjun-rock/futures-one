package fwj.futures.resource.trade.enu;

public enum TradeOrderType {
	BuyOpen(1, "买开"), SellOpen(2, "卖开"), SellClose(3, "卖平"), BuyClose(4, "买平");

	private int code;
	private String name;

	TradeOrderType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public static TradeOrderType getByName(String name) {
		if(name == null) {
			return null;
		}
		for (TradeOrderType temp : values()) {
			if(temp.getName().equals(name)) {
				return temp;
			}
		}
		return null;
	}

}

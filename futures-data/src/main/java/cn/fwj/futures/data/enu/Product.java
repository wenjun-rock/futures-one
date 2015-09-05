package cn.fwj.futures.data.enu;

public enum Product {
	
	/* 郑州商品交易所  */
	CaiYou("OI", "菜油", Exchange.ZZ),
	
	/* 大连商品交易所  */
	JiDan("JD", "鸡蛋", Exchange.DL), 
	YuMi("C", "玉米", Exchange.DL), 
	DouPo("M", "豆粕", Exchange.DL), 
	DaDou1("A", "大豆一号",Exchange.DL),	
	DouYou("Y", "豆油",Exchange.DL),
	ZongLvYou("P", "棕榈油",Exchange.DL),
	DaDou2("B", "大豆二号",Exchange.DL);
	


	private String code;
	private String name;
	private Exchange exchange;

	Product(String code, String name, Exchange exchange) {
		this.code = code;
		this.name = name;
		this.exchange = exchange;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Exchange getExchange() {
		return exchange;
	}
	
	public String toString() {
		return code + "(" + name + ")"; 
	}

}

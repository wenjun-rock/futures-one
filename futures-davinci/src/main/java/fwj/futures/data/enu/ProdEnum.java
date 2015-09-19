package fwj.futures.data.enu;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ProdEnum {
	
	/* 上海商品交易所  */
	Baiyin("AG", "白银", Exchange.SH),
	Lv("AL", "沪铝", Exchange.SH),
	HuangJin("AU", "黄金", Exchange.SH),
	LiQing("BU", "沥青", Exchange.SH),
	Tong("CU", "沪铜", Exchange.SH),
//	RanYou("FU", "燃油", Exchange.SH),
	ReJuan("HC", "热卷", Exchange.SH),
	Nie("NI", "沪镍", Exchange.SH),
	Qian("PB", "沪铅", Exchange.SH),
	LuoWenGang("RB", "螺纹", Exchange.SH),
	XiangJiao("RU", "橡胶", Exchange.SH),
	Xi("SN", "沪锡", Exchange.SH),
//	XianCai("WR", "线材", Exchange.SH),
	Xin("ZN", "沪锌", Exchange.SH),
	
	/* 大连商品交易所  */
	DaDou1("A", "大豆",Exchange.DL),	
//	DaDou2("B", "豆二",Exchange.DL),
//	JiaoHeBan("BB", "胶合板", Exchange.DL), 
	YuMi("C", "玉米", Exchange.DL), 
	YuDian("CS", "淀粉", Exchange.DL), 
//	XianWei("FB", "纤维板", Exchange.DL), 
	TieKuangShi("I", "铁矿石", Exchange.DL), 
	JiaoTan("J", "焦炭", Exchange.DL), 
	JiDan("JD", "鸡蛋", Exchange.DL), 
	JiaoMei("JM", "焦煤", Exchange.DL), 
	SuLiao("L", "塑料", Exchange.DL), 
	DouPo("M", "豆粕", Exchange.DL), 
	ZongLvYou("P", "棕榈油", Exchange.DL), 
	JuBinXi("PP", "PP", Exchange.DL), 
	JuLvYiXi("V", "PVC", Exchange.DL), 
	DouYou("Y", "豆油", Exchange.DL), 
	
	/* 郑州商品交易所  */
	MianHua("CF", "棉花", Exchange.ZZ),
	BoLi("FG", "玻璃", Exchange.ZZ),
//	JingDao("JR", "粳稻", Exchange.ZZ),
//	WanXianDao("LR", "晚籼稻", Exchange.ZZ),
	JiaChun("MA", "甲醇", Exchange.ZZ),
	CaiYou("OI", "菜油", Exchange.ZZ),
//	PuMai("PM", "普麦", Exchange.ZZ),
//	ZaoXianDao("RI", "早籼稻", Exchange.ZZ),
	CaiPo("RM", "菜粕", Exchange.ZZ),
	YouCai("RS", "菜籽", Exchange.ZZ),
//	GuiTie("SF", "硅铁", Exchange.ZZ),
//	MengGui("SM", "锰硅", Exchange.ZZ),
	BaiTang("SR", "白糖", Exchange.ZZ),
	PTA("TA", "PTA", Exchange.ZZ),
	DongLiMei("TC", "动力煤", Exchange.ZZ),
//	DongLiMeiZC("ZC", "动力煤ZC", Exchange.ZZ),
	QiangMai("WH", "强麦", Exchange.ZZ);

	private String code;
	private String name;
	private Exchange exchange;

	ProdEnum(String code, String name, Exchange exchange) {
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
	
	public static ProdEnum codeOf(String code) {
		for(ProdEnum ele : ProdEnum.values()){
			if(ele.getCode().equals(code)) {
				return ele;
			}
		}
		return null;
	}
	
	public static ProdEnum[] findByExchange(Exchange exch) {
		return Stream.of(ProdEnum.values()).filter(prodEnum -> prodEnum.getExchange() == exch)
				.collect(Collectors.toList()).toArray(new ProdEnum[0]);
	}
	
	public String toString() {
		return code + "(" + name + ")"; 
	}

}

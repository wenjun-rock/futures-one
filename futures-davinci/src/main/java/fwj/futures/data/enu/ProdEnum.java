package fwj.futures.data.enu;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ProdEnum {

	/* 上海商品交易所 */
	Baiyin("AG", "白银", 15, "千克/手", Exchange.SH), //
	Lv("AL", "沪铝", 5, "吨/手", Exchange.SH), //
	HuangJin("AU", "黄金", 1000, "克/手", Exchange.SH), //
	LiQing("BU", "沥青", 10, "吨/手", Exchange.SH), //
	Tong("CU", "沪铜", 5, "吨/手", Exchange.SH), //
	// RanYou("FU", "燃油", Exchange.SH), //
	ReJuan("HC", "热卷", 10, "吨/手", Exchange.SH), //
	Nie("NI", "沪镍", 1, "吨/手", Exchange.SH), //
	Qian("PB", "沪铅", 5, "吨/手", Exchange.SH), //
	LuoWenGang("RB", "螺纹", 10, "吨/手", Exchange.SH), //
	XiangJiao("RU", "橡胶", 10, "吨/手", Exchange.SH), //
	Xi("SN", "沪锡", 1, "吨/手", Exchange.SH), //
	// XianCai("WR", "线材", Exchange.SH), //
	Xin("ZN", "沪锌", 5, "吨/手", Exchange.SH), //

	/* 大连商品交易所 */
	DaDou1("A", "大豆", 10, "吨/手", Exchange.DL), //
	// DaDou2("B", "豆二",Exchange.DL), //
	// JiaoHeBan("BB", "胶合板", Exchange.DL), //
	YuMi("C", "玉米", 10, "吨/手", Exchange.DL), //
	YuDian("CS", "淀粉", 10, "吨/手", Exchange.DL), //
	// XianWei("FB", "纤维板", Exchange.DL), //
	TieKuangShi("I", "铁矿石", 100, "吨/手", Exchange.DL), //
	JiaoTan("J", "焦炭", 100, "吨/手", Exchange.DL), //
	JiDan("JD", "鸡蛋", 10, "半吨/手", Exchange.DL), //
	JiaoMei("JM", "焦煤", 60, "吨/手", Exchange.DL), //
	SuLiao("L", "塑料", 5, "吨/手", Exchange.DL), //
	DouPo("M", "豆粕", 10, "吨/手", Exchange.DL), //
	ZongLvYou("P", "棕榈油", 10, "吨/手", Exchange.DL), //
	JuBinXi("PP", "PP", 5, "吨/手", Exchange.DL), //
	JuLvYiXi("V", "PVC", 5, "吨/手", Exchange.DL), //
	DouYou("Y", "豆油", 10, "吨/手", Exchange.DL), //

	/* 郑州商品交易所 */
	MianHua("CF", "棉花", 5, "吨/手", Exchange.ZZ), //
	BoLi("FG", "玻璃", 20, "吨/手", Exchange.ZZ), //
	// JingDao("JR", "粳稻", Exchange.ZZ), //
	// WanXianDao("LR", "晚籼稻", Exchange.ZZ), //
	JiaChun("MA", "甲醇", 10, "吨/手", Exchange.ZZ), //
	CaiYou("OI", "菜油", 10, "吨/手", Exchange.ZZ), //
	// PuMai("PM", "普麦", Exchange.ZZ), //
	// ZaoXianDao("RI", "早籼稻", Exchange.ZZ), //
	CaiPo("RM", "菜粕", 10, "吨/手", Exchange.ZZ), //
	YouCai("RS", "菜籽", 10, "吨/手", Exchange.ZZ), //
	// GuiTie("SF", "硅铁", Exchange.ZZ), //
	// MengGui("SM", "锰硅", Exchange.ZZ), //
	BaiTang("SR", "白糖", 10, "吨/手", Exchange.ZZ), //
	PTA("TA", "PTA", 5, "吨/手", Exchange.ZZ), //
	DongLiMei("TC", "动力煤", 200, "吨/手", Exchange.ZZ), //
	// DongLiMeiZC("ZC", "动力煤ZC", 100, "吨/手", Exchange.ZZ), //
	QiangMai("WH", "强麦", 20, "吨/手", Exchange.ZZ); //

	private String code;
	private String name;
	private int unit;
	private String unitDesc;
	private Exchange exchange;

	ProdEnum(String code, String name, int unit, String unitDesc, Exchange exchange) {
		this.code = code;
		this.name = name;
		this.unit = unit;
		this.unitDesc = unitDesc;
		this.exchange = exchange;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getUnit() {
		return unit;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public static ProdEnum codeOf(String code) {
		for (ProdEnum ele : ProdEnum.values()) {
			if (ele.getCode().equals(code)) {
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

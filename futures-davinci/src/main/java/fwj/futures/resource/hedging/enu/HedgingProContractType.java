package fwj.futures.resource.hedging.enu;

public enum HedgingProContractType {
	SP(1, "跨期"), SPC(2, "跨商品");

	private int code;
	private String name;

	HedgingProContractType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}

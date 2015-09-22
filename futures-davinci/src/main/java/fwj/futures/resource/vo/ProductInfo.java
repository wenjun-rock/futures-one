package fwj.futures.resource.vo;

import java.util.List;

import fwj.futures.resource.entity.prod.FuturesTradeTime;

public class ProductInfo {

	private String code;
	private String name;
	private int unit;
	private String unitDesc;
	private List<InnerLabel> labels;
	private List<FuturesTradeTime> tradeTimes;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public String getUnitDesc() {
		return unitDesc;
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public List<InnerLabel> getLabels() {
		return labels;
	}

	public void setLabels(List<InnerLabel> labels) {
		this.labels = labels;
	}

	public List<FuturesTradeTime> getTradeTimes() {
		return tradeTimes;
	}

	public void setTradeTimes(List<FuturesTradeTime> tradeTimes) {
		this.tradeTimes = tradeTimes;
	}

	public static class InnerLabel {
		private Integer labelId;
		private String labelName;

		public Integer getLabelId() {
			return labelId;
		}

		public void setLabelId(Integer labelId) {
			this.labelId = labelId;
		}

		public String getLabelName() {
			return labelName;
		}

		public void setLabelName(String labelName) {
			this.labelName = labelName;
		}
	}

}

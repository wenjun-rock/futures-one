package fwj.futures.resource.vo;

import java.util.List;

public class ProductInfo {

	private String code;
	private String name;
	private int unit;
	private String unitDesc;
	private List<InnerLabel> labels;

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

	public static class InnerLabel {
		private Integer labelId;
		private String name;

		public Integer getLabelId() {
			return labelId;
		}

		public void setLabelId(Integer labelId) {
			this.labelId = labelId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}

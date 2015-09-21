package fwj.futures.resource.web.vo;

public class Series {

	public static Series EMPTY = new Series("", "", new Object[0][2]);

	private String code;
	private String name;
	private Object[][] data;

	public Series(String code, String name, Object[][] data) {
		this.code = code;
		this.name = name;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Object[][] getData() {
		return data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}
}
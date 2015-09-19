package fwj.futures.resource.web.vo;

import java.util.List;

public class ProductLabel {

	private Integer id;
	private String name;
	private List<InnerProduct> products;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<InnerProduct> getProducts() {
		return products;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProducts(List<InnerProduct> products) {
		this.products = products;
	}

	public static class InnerProduct {
		private String code;
		private String name;

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}

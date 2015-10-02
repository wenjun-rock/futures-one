package fwj.futures.data.struct;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import fwj.futures.data.enu.ProdEnum;

public class Formula {

	private BigDecimal constant;
	private List<Multinomial> multinomials = new ArrayList<>();

	public Formula() {
	}

	public static Formula create() {
		return new Formula();
	}

	public static Formula parse(String json) {
		return JSON.parseObject(json, Formula.class);
	}

	public Formula putConstant(BigDecimal constant) {
		this.constant = constant;
		return this;
	}

	public Formula putMultinomial(String code, BigDecimal coefficient) {
		Multinomial multinomial = new Multinomial();
		multinomial.setCode(code);
		multinomial.setCoefficient(coefficient);
		multinomials.add(multinomial);
		return this;
	}

	public Formula putConstant(String constant) {
		this.constant = new BigDecimal(constant);
		return this;
	}

	public Formula putMultinomial(ProdEnum prod, String coefficient) {
		putMultinomial(prod.getCode(), new BigDecimal(coefficient));
		return this;
	}

	public Formula putMultinomial(String prodCode, String coefficient) {
		putMultinomial(prodCode, new BigDecimal(coefficient));
		return this;
	}

	public BigDecimal findCoefficient(String code) {
		for (Multinomial multinomial : multinomials) {
			if (multinomial.getCode().equals(code)) {
				return multinomial.getCoefficient();
			}
		}
		return BigDecimal.ZERO;
	}

	public BigDecimal getConstant() {
		return constant;
	}

	public List<Multinomial> getMultinomials() {
		return multinomials;
	}

	public void setConstant(BigDecimal constant) {
		this.constant = constant;
	}

	public void setMultinomials(List<Multinomial> multinomials) {
		this.multinomials = multinomials;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(constant.setScale(0, RoundingMode.FLOOR).toString());
		for (Multinomial multinomial : multinomials) {
			sb.append(multinomial.getCoefficient().compareTo(BigDecimal.ZERO) > 0 ? " + " : " - ")
					.append(multinomial.getCoefficient().abs()).append("*").append(multinomial.code);
		}
		return sb.toString();
	}

	public String toCsv() {
		StringBuilder sb = new StringBuilder(constant.toString());
		for (Multinomial multinomial : multinomials) {
			sb.append(",").append(multinomial.getCoefficient()).append(",").append(multinomial.code);
		}
		return sb.toString();
	}

	public static class Multinomial {

		private String code;
		private BigDecimal coefficient;

		public String getCode() {
			return code;
		}

		public BigDecimal getCoefficient() {
			return coefficient;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setCoefficient(BigDecimal coefficient) {
			this.coefficient = coefficient;
		}
	}

}
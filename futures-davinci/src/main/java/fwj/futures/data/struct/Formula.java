package fwj.futures.data.struct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import fwj.futures.data.enu.ProdEnum;

public class Formula {

	private BigDecimal constant;
	private List<Multinomial> multinomials = new ArrayList<>();

	public Formula() {
	}

	public static Formula create() {
		return new Formula();
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

	public BigDecimal getCoefficient(String code) {
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

	public class Multinomial {

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
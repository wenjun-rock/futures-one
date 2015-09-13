package fwj.futures.data.struct;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import fwj.futures.data.enu.ProdEnum;

public class Formula {

	private BigDecimal constant;
	private Map<ProdEnum, BigDecimal> multinomials = new HashMap<>();

	private Formula() {

	}

	public static Formula create() {
		return new Formula();
	}

	public Formula putConstant(BigDecimal constant) {
		this.constant = constant;
		return this;
	}

	public Formula putMultinomials(ProdEnum prod, BigDecimal coefficient) {
		multinomials.put(prod, coefficient);
		return this;
	}

	public Formula putConstant(String constant) {
		this.constant = new BigDecimal(constant);
		return this;
	}

	public Formula putMultinomials(ProdEnum prod, String coefficient) {
		multinomials.put(prod, new BigDecimal(coefficient));
		return this;
	}

	public BigDecimal getConstant() {
		return constant;
	}

	public Map<ProdEnum, BigDecimal> getMultinomials() {
		return multinomials;
	}

}
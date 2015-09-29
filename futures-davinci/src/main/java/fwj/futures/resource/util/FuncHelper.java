package fwj.futures.resource.util;

import java.util.function.BinaryOperator;

public class FuncHelper {

	public static BinaryOperator<String> joinString(String delimiter) {
		return (l, r) -> l + delimiter + r;
	}

}

package fwj.futures.resource.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorsHelper {

	public static <T, K, U> Collector<T, ?, TreeMap<K, U>> toTreeMap(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper) {
		return Collectors.toMap(keyMapper, valueMapper, throwingMerger(), TreeMap::new);
	}

	public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper) {
		return Collectors.toMap(keyMapper, valueMapper, throwingMerger(), HashMap::new);
	}

	/**
	 * Returns a merge function, suitable for use in
	 * {@link Map#merge(Object, Object, BiFunction) Map.merge()} or
	 * {@link #toMap(Function, Function, BinaryOperator) toMap()}, which always
	 * throws {@code IllegalStateException}. This can be used to enforce the
	 * assumption that the elements being collected are distinct.
	 *
	 * @param <T>
	 *            the type of input arguments to the merge function
	 * @return a merge function which always throw {@code IllegalStateException}
	 */
	private static <T> BinaryOperator<T> throwingMerger() {
		return (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		};
	}
}

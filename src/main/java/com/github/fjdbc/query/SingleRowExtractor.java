package com.github.fjdbc.query;

/**
 * Extract objects of type {@code T} from a ResultSet where each object maps to a single row.
 */
@FunctionalInterface
public interface SingleRowExtractor<T> extends ResultSetExtractor<T> {
	@Override
	default boolean autoCallNext() {
		return true;
	}

}

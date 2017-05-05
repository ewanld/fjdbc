package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Extract objects of type {@code T} from a ResultSet where each object maps to a single row.
 */
@FunctionalInterface
public interface SingleRowExtractor<T> extends ResultSetExtractor<T> {

	T extract(ResultSet rs) throws SQLException;

	@Override
	default void extractAll(ResultSet rs, Consumer<? super T> callback) throws SQLException {
		while (rs.next()) {
			final T object = extract(rs);
			callback.accept(object);
		}
	}
}

package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Extract a single row from a ResultSet.
 */
@FunctionalInterface
public interface SingleRowExtractor<T> extends ResultSetExtractor<T> {

	public abstract T extract(ResultSet rs) throws SQLException;

	@Override
	public default void extractAll(ResultSet rs, Consumer<? super T> callback) throws SQLException {
		while (rs.next()) {
			final T object = extract(rs);
			callback.accept(object);
		}
	}
}

package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Extract objects of type {@code T} from a ResultSet where each object maps to a single row.
 */
@FunctionalInterface
public interface SingleRowExtractor<T> extends ResultSetExtractor<T> {
	/**
	 * Convert the current ResultSet row to an instance of type {@code T}.
	 * <p>
	 * Implementors should not call the {@link ResultSet#next()} method or any other method that moves the
	 * {@ResultSet}'s cursor. If this is required, implement {@link ResultSetExtractor} instead.
	 */
	T extract(ResultSet rs) throws SQLException;

	@Override
	default void extractAll(ResultSet rs, Consumer<? super T> callback) throws SQLException {
		while (rs.next()) {
			final T object = extract(rs);
			callback.accept(object);
		}
	}
}

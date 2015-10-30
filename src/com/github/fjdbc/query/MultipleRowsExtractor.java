package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Extract an object from a ResultSet, where the object maps to several rows.
 */
public abstract class MultipleRowsExtractor<T> implements ResultSetExtractor<T> {
	/**
	 * {@code rs.next()} must have been called prior to calling this method, and must also be called by implementors
	 * at the end of this method (even if the ResultSet does not have any more rows).
	 */
	protected abstract T extract(ResultSet rs) throws SQLException;

	@Override
	public final void extractAll(ResultSet rs, Consumer<? super T> callback) throws SQLException {
		if (!rs.next()) return;
		while (!rs.isAfterLast()) {
			final T object = extract(rs);
			callback.accept(object);
		}
	}
}

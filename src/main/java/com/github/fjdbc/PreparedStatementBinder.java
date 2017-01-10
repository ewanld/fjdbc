package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import com.github.fjdbc.util.IntSequence;

/**
 * Represent a function that binds values to a prepared statement.
 */
@FunctionalInterface
public interface PreparedStatementBinder {
	void bind(PreparedStatement st, IntSequence paramIndex) throws SQLException;

	public default void bind(PreparedStatement st) throws SQLException {
		bind(st, new IntSequence(1));
	}

	public static PreparedStatementBinder create(Object... params) {
		return (st, index) -> {
			for (final Object param : params) {
				st.setObject(index.next(), param);
			}
		};
	}

	public static PreparedStatementBinder createBatch(Collection<? extends Collection<Object>> params) {
		return (st, index) -> {
			for (final Collection<Object> paramCollection : params) {
				for (final Object param : paramCollection) {
					st.setObject(index.next(), param);
				}
				st.addBatch();
				index.reset();
			}
		};
	}

}

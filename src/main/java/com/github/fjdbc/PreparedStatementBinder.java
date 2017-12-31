package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represent a function that binds parameters to a prepared statement.
 * <p>
 * The general contract is that implementors should only call {@code setXXX} methods from the {@link PreparedStatement}.
 */
@FunctionalInterface
public interface PreparedStatementBinder {
	/**
	 * Bind parameters to the {@code PreparedStatement}.
	 * <p>
	 * Implementors should only call {@code setXXX} methods from the {@link PreparedStatement}.
	 * 
	 * @param index
	 *            Call {@code index.next()} to get the next parameter index, instead of managing the index manually.
	 *            Apart from the convienience, this allows composition of {@code PreparedStatement} instances.
	 */
	void bind(PreparedStatement ps, IntSequence index) throws SQLException;

	public static PreparedStatementBinder create(Object... params) {
		return (st, index) -> {
			for (final Object param : params) {
				st.setObject(index.next(), param);
			}
		};
	}

}

package com.github.fjdbc;

import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * A {@link Consumer} that may throw a {@link SQLException}.
 */
@FunctionalInterface
public interface SQLConsumer<T> {
	void accept(T t) throws SQLException;

	public default Consumer<T> uncheck() {
		return t -> {
			try {
				accept(t);
			} catch (final SQLException e) {
				throw new RuntimeSQLException(e);
			}
		};
	}
}

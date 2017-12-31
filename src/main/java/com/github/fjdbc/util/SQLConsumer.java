package com.github.fjdbc.util;

import java.sql.SQLException;
import java.util.function.Consumer;

import com.github.fjdbc.RuntimeSQLException;

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

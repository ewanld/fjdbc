package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represent a function that binds values to a prepared statement.
 */
@FunctionalInterface
public interface PreparedStatementBinder {
	void bind(PreparedStatement st, Sequence paramIndex) throws SQLException;
	
	public default void bind(PreparedStatement st) throws SQLException {
		bind(st, new Sequence(1));
	}
}

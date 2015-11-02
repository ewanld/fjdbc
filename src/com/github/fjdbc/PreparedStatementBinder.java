package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represent a function that binds values to a prepared statement.
 */
@FunctionalInterface
public interface PreparedStatementBinder {
	void bind(PreparedStatement ps) throws SQLException;
}

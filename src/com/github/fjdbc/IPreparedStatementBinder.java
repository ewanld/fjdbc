package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Represent a function that binds values to a prepared statement.
 */
public interface IPreparedStatementBinder {
	void bind(PreparedStatement ps) throws SQLException;

	/**
	 * @return the number of modified rows.
	 */
	int execute(PreparedStatement ps) throws SQLException;
}

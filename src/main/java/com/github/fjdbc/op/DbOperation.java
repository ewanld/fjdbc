package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represent a database operation that return a row count: insert, update, delete, etc.
 */
public interface DbOperation {
	/**
	 * Execute the operation, leaving the connection open.
	 * 
	 * @return The number of modified rows.
	 */
	int execute(Connection connection) throws SQLException;

	/**
	 * Executes the following steps:
	 * <ul>
	 * <li>execute the operation.
	 * <li>commit.
	 * <li>In case of exception, rollback.
	 * </ul>
	 * The connection is left open at the end.
	 * 
	 * @return The number of modified rows.
	 */
	int executeAndCommit();
}
package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.RuntimeSQLException;

/**
 * Represent a database operation that modifies rows: insert, update, delete, etc.
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
	 * The method {@link ConnectionProvider#giveBack} is called at the end.
	 * 
	 * @return The number of modified rows.
	 * @throws RuntimeSQLException
	 *             if a {@link SQLException} occurs.
	 */
	int executeAndCommit();
}
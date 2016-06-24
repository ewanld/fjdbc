package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for representing statements and prepared statements.
 */
public interface DbOp {
	/**
	 * Execute the operation, leaving the connection open.
	 */
	public int execute(Connection cnx) throws SQLException;

	/**
	 * Executes the following steps:
	 * <ul>
	 * <li>execute the operation.
	 * <li>commit.
	 * <li>In case of exception, rollback.
	 * </ul>
	 */
	public int executeAndCommit(Connection cnx);
}
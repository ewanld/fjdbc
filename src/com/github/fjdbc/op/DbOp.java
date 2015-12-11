package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

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
	 * <li>Acquire a connection from the supplier.
	 * <li>execute the operation.
	 * <li>commit.
	 * <li>close the connection.
	 * </ul>
	 */
	public int executeAndCommit(Supplier<Connection> cnxSupplier);
}
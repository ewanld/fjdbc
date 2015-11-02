package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * A statement that does nothing.
 */
public class NoOp implements Op {

	@Override
	public int execute(Connection cnx) throws SQLException {
		return 0;
	}

	@Override
	public int executeAndCommit(Supplier<Connection> cnxSupplier) {
		return 0;
	}

}

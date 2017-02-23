package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.fjdbc.ConnectionProvider;

/**
 * An operation that does nothing.
 */
public class NoOperation implements DbOperation {

	@Override
	public int execute(Connection connection) throws SQLException {
		return 0;
	}

	@Override
	public int executeAndCommit(ConnectionProvider cnxProvider) {
		return 0;
	}

}

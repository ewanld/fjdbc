package com.github.fjdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.fjdbc.ConnectionProvider;

/**
 * A {@link ConnectionProvider} to a single connection. The connection is not closed when it is returned.
 */
public class SingleConnectionProvider extends ConnectionProvider {
	private final Connection cnx;

	public SingleConnectionProvider(Connection cnx) {
		this.cnx = cnx;
	}

	@Override
	protected Connection doBorrow() throws SQLException {
		return cnx;
	}

	@Override
	protected void doGiveBack(Connection _cnx) {
		// no op
	}

}

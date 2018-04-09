package com.github.fjdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.fjdbc.ConnectionProvider;

/**
 * A {@link ConnectionProvider} to a single connection.
 * <p>
 * The connection is not closed when {@link #giveBack()} is called.
 */
public class SingleConnectionProvider implements ConnectionProvider {
	private final Connection cnx;

	public SingleConnectionProvider(Connection cnx) {
		this.cnx = cnx;
	}

	@Override
	public Connection borrow() throws SQLException {
		return cnx;
	}

	@Override
	public void giveBack(Connection _cnx) {
		// no op
	}

}

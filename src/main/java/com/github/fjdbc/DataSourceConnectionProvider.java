package com.github.fjdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DataSourceConnectionProvider extends ConnectionProvider {

	private final DataSource dataSource;

	public DataSourceConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	protected Connection doBorrow() throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * In a DataSource, calling close() will return the connection back to the pool.
	 */
	@Override
	protected void doGiveBack(Connection cnx) throws SQLException {
		cnx.close();
	}

}

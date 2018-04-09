package com.github.fjdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.github.fjdbc.ConnectionProvider;

/**
 * A {@link ConnectionProvider} that wraps a {@link DataSource}.
 */
public class DataSourceConnectionProvider implements ConnectionProvider {

	private final DataSource dataSource;

	public DataSourceConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection borrow() throws SQLException {
		return dataSource.getConnection();
	}
}

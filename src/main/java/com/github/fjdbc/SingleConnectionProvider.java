package com.github.fjdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.fjdbc.util.FjdbcUtil;

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
		FjdbcUtil.closeConnection(_cnx);
	}

}

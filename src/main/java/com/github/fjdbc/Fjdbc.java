package com.github.fjdbc;

import com.github.fjdbc.op.DbOperation;
import com.github.fjdbc.op.StatementOperation;
import com.github.fjdbc.query.Query;
import com.github.fjdbc.query.ResultSetExtractor;

public class Fjdbc {
	private final ConnectionProvider cnxProvider;

	public Fjdbc(ConnectionProvider cnxProvider) {
		this.cnxProvider = cnxProvider;
	}

	public <T> Query<T> query(String sql, ResultSetExtractor<T> extractor) {
		return new Query<>(cnxProvider, sql, extractor);
	}

	public DbOperation operation(String sql, PreparedStatementBinder binder) {
		return new StatementOperation(sql, binder);
	}

}

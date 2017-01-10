package com.github.fjdbc;

import com.github.fjdbc.op.DbOp;
import com.github.fjdbc.op.StatementOp;
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

	public DbOp operation(String sql, PreparedStatementBinder binder) {
		return new StatementOp(sql, binder);
	}

}

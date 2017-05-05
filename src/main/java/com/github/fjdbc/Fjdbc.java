package com.github.fjdbc;

import java.util.Collection;

import com.github.fjdbc.op.CompositeOperation;
import com.github.fjdbc.op.DbOperation;
import com.github.fjdbc.op.StatementOperation;
import com.github.fjdbc.query.Query;
import com.github.fjdbc.query.ResultSetExtractor;

public class Fjdbc {
	private final ConnectionProvider cnxProvider;

	public Fjdbc(ConnectionProvider cnxProvider) {
		this.cnxProvider = cnxProvider;
	}

	public StatementOperation statement(String sql) {
		return new StatementOperation(cnxProvider, sql);
	}

	public StatementOperation statement(String sql, PreparedStatementBinder binder) {
		return new StatementOperation(cnxProvider, sql, binder);
	}

	public CompositeOperation composite(DbOperation... operations) {
		return new CompositeOperation(cnxProvider, operations);
	}

	public CompositeOperation composite(Collection<? extends DbOperation> operations) {
		return new CompositeOperation(cnxProvider, operations);
	}

	public <T> Query<T> query(String sql, ResultSetExtractor<T> extractor) {
		return new Query<>(cnxProvider, sql, extractor);
	}

	public <T> Query<T> query(String sql, PreparedStatementBinder binder, ResultSetExtractor<T> extractor) {
		return new Query<>(cnxProvider, sql, binder, extractor);
	}

}
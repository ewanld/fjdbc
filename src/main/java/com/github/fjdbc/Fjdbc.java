package com.github.fjdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import com.github.fjdbc.op.CompositeOperation;
import com.github.fjdbc.op.DbOperation;
import com.github.fjdbc.op.StatementOperation;
import com.github.fjdbc.query.Query;
import com.github.fjdbc.query.ResultSetExtractor;

/**
 * Facade to the FJDBC library.
 */
public class Fjdbc {
	private final ConnectionProvider cnxProvider;

	/**
	 * Create a facade to the FJDBC library.
	 * 
	 * @param connectionProvider
	 *            The provider of {@link Connection} instances.
	 */
	public Fjdbc(ConnectionProvider connectionProvider) {
		this.cnxProvider = connectionProvider;
	}

	/**
	 * Create a statement.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * 
	 * @param sql
	 *            The raw SQL string to be executed.
	 */
	public StatementOperation statement(String sql) {
		return new StatementOperation(cnxProvider, sql);
	}

	/**
	 * Merge a sequence of {@link DbOperation} as a single {@link DbOperation}.
	 * <p>
	 * This allows the operations be executed in a single transaction.
	 */
	public CompositeOperation composite(DbOperation... operations) {
		return new CompositeOperation(cnxProvider, operations);
	}

	/**
	 * Merge a sequence of {@link DbOperation} as a single {@link DbOperation}.
	 * <p>
	 * This allows the operations be executed in a single transaction.
	 */
	public CompositeOperation composite(Collection<? extends DbOperation> operations) {
		return new CompositeOperation(cnxProvider, operations);
	}

	/**
	 * Create a new query.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * 
	 * @param sql
	 *            The raw SQL string. It should be a SELECT statement.
	 * @param extractor
	 *            Extracts individual objects from a {@link ResultSet}.
	 */
	public <T> Query<T> query(String sql, ResultSetExtractor<T> extractor) {
		return new Query<>(cnxProvider, sql, extractor);
	}

	public ConnectionProvider getConnectionProvider() {
		return cnxProvider;
	}

}
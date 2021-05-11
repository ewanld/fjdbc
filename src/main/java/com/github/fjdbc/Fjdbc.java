package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import com.github.fjdbc.internal.StatementOperationImpl;
import com.github.fjdbc.op.CompositeOperation;
import com.github.fjdbc.op.DbOperation;
import com.github.fjdbc.op.StatementOperation;
import com.github.fjdbc.query.Query;
import com.github.fjdbc.query.ResultSetExtractor;
import com.github.fjdbc.sql.BatchStatementOperation;
import com.github.fjdbc.sql.SqlBuilder.SqlFragment;

/**
 * Facade to the FJDBC library.
 */
public class Fjdbc {
	private final ConnectionProvider cnxProvider;
	private final List<SQLConsumer<Statement>> beforeExecutionConsumers = new ArrayList<>(2);
	private final List<SQLConsumer<Statement>> afterExecutionConsumers = new ArrayList<>(2);

	/**
	 * Create a facade to the FJDBC library.
	 * @param connectionProvider
	 *        The provider of {@link java.sql.Connection} instances.
	 */
	public Fjdbc(ConnectionProvider connectionProvider) {
		this.cnxProvider = connectionProvider;
	}

	/**
	 * Create a statement.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * @param sql
	 *        The raw SQL string to be executed.
	 */
	public StatementOperation statement(String sql) {
		final StatementOperationImpl res = new StatementOperationImpl(cnxProvider, sql);
		addCallbacks(res);
		return res;
	}

	/**
	 * Create a statement.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * @param sql
	 *        The raw SQL string to be executed.
	 * @param binder
	 *        The binder of {@link java.sql.PreparedStatement} parameters, or {@code null} if this query does not have
	 *        parameters.
	 */
	public StatementOperation statement(String sql, PreparedStatementBinder binder) {
		final StatementOperationImpl res = new StatementOperationImpl(cnxProvider, sql, binder);
		addCallbacks(res);
		return res;
	}

	public <T extends SqlFragment> BatchStatementOperation<T> batchStatement(Stream<T> statements,
			long executeEveryNRow, long commitEveryNRow) {
		final BatchStatementOperation<T> res = new BatchStatementOperation<>(cnxProvider, statements, executeEveryNRow,
				commitEveryNRow);
		addCallbacks(res);
		return res;
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
	 * @param sql
	 *        The raw SQL string. It should be a SELECT statement.
	 * @param extractor
	 *        Extracts individual objects from a {@link java.sql.ResultSet}.
	 */
	public <T> Query<T> query(String sql, ResultSetExtractor<T> extractor) {
		final Query<T> res = new Query<>(cnxProvider, sql, extractor);
		addCallbacks(res);
		return res;
	}

	/**
	 * Create a new query.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * @param sql
	 *        The raw SQL string. It should be a SELECT statement.
	 * @param binder
	 *        The binder of {@link PreparedStatement} parameters, or {@code null} if this query does not have
	 *        parameters.
	 * @param extractor
	 *        Extracts individual objects from a {@link java.sql.ResultSet}.
	 */
	public <T> Query<T> query(String sql, PreparedStatementBinder binder, ResultSetExtractor<T> extractor) {
		final Query<T> res = new Query<>(cnxProvider, sql, binder, extractor);
		addCallbacks(res);
		return res;
	}

	private <T> void addCallbacks(Query<T> query) {
		for (final SQLConsumer<Statement> c : beforeExecutionConsumers) {
			query.doBeforeExecution(c);
		}
		for (final SQLConsumer<Statement> c : afterExecutionConsumers) {
			query.doAfterExecution(c);
		}
	}

	private <T> void addCallbacks(StatementOperation op) {
		for (final SQLConsumer<Statement> c : beforeExecutionConsumers) {
			op.doBeforeExecution(c);
		}
		for (final SQLConsumer<Statement> c : afterExecutionConsumers) {
			op.doAfterExecution(c);
		}
	}

	public ConnectionProvider getConnectionProvider() {
		return cnxProvider;
	}

	public void doBeforeExecution(SQLConsumer<Statement> statementConsumer) {
		beforeExecutionConsumers.add(statementConsumer);
	}

	public void doAfterExecution(SQLConsumer<Statement> statementConsumer) {
		afterExecutionConsumers.add(statementConsumer);
	}

}
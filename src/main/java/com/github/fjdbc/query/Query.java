package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.IntSequence;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.RuntimeSQLException;
import com.github.fjdbc.util.SQLConsumer;

/**
 * Represent an SQL SELECT statement.
 * 
 * @param <T>
 *            The type of objects to be extracted from the {@link ResultSet}}.
 */
public class Query<T> {
	private final String sql;
	private final PreparedStatementBinder binder;
	private final ResultSetExtractor<T> extractor;
	private ConnectionProvider cnxProvider;
	private SQLConsumer<Statement> beforeExecutionConsumer;
	private SQLConsumer<Statement> afterExecutionConsumer;

	/**
	 * @param connectionProvider
	 *            The provider of {@link Connection} instances.
	 * @param sql
	 *            The raw SQL string. It should be a SELECT statement.
	 * @param binder
	 *            The binder of {@link PreparedStatement} parameters, or {@code null} if this query does not have
	 *            parameters.
	 * @param extractor
	 *            Extracts individual objects from a {@link ResultSet}.
	 */
	public Query(ConnectionProvider connectionProvider, String sql, PreparedStatementBinder binder,
			ResultSetExtractor<T> extractor) {
		assert connectionProvider != null;
		assert sql != null;
		assert extractor != null;

		this.cnxProvider = connectionProvider;
		this.sql = sql;
		this.binder = binder;
		this.extractor = extractor;
	}

	/**
	 * @param connectionProvider
	 *            The provider of {@link Connection} instances.
	 * @param sql
	 *            The raw SQL string. It should be a SELECT statement.
	 * @param extractor
	 *            Extracts individual objects from a {@link ResultSet}.
	 */
	public Query(ConnectionProvider connectionProvider, String sql, ResultSetExtractor<T> extractor) {
		this(connectionProvider, sql, null, extractor);
	}

	/**
	 * User-defined code to be executed after the {@link Statement} is created, but before it is executed.
	 * <p>
	 * This can be useful for setting Statement parameters, as {@link Statement#setFetchSize(int)},
	 * {@link Statement#setMaxRows(int)}, etc.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 */
	public void doBeforeExecution(SQLConsumer<Statement> statementConsumer) {
		this.beforeExecutionConsumer = statementConsumer;
	}

	/**
	 * User-defined code to be executed after the {@link Statement} is executed, but before the {@link ResultSet} is
	 * read.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 */
	public void doAfterExecution(SQLConsumer<Statement> statementConsumer) {
		this.afterExecutionConsumer = statementConsumer;
	}

	private boolean isPrepared() {
		return binder != null;
	}

	/**
	 * Execute the query, the calls the specified callback for each object extracted from the {@link ResultSet}.
	 */
	public void forEach(Consumer<? super T> callback) {
		Statement st = null;
		try {
			final Connection cnx = cnxProvider.borrow();
			st = isPrepared() ? cnx.prepareStatement(sql) : cnx.createStatement();
			if (isPrepared()) binder.bind((PreparedStatement) st, new IntSequence(1));
			if (beforeExecutionConsumer != null) beforeExecutionConsumer.accept(st);
			final ResultSet rs = isPrepared() ? ((PreparedStatement) st).executeQuery() : st.executeQuery(sql);
			if (afterExecutionConsumer != null) afterExecutionConsumer.accept(st);
			extractor.extractAll(rs, callback);
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		} finally {
			close(st);
			cnxProvider.giveBack();
		}
	}

	private static void close(Statement st) {
		try {
			if (st != null) st.close();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	/**
	 * Execute the query, then returns a list of objects extracted from the {@link ResultSet}.
	 * <p>
	 * Convenience method; it is equivalent to:
	 * 
	 * <pre>
	 * List<T> list = new ArrayList<>();
	 * forEach(list::add);
	 * </pre>
	 */
	public List<T> toList() {
		final List<T> res = new ArrayList<>();
		forEach(res::add);
		return res;
	}

	/**
	 * Execute the query, then returns the single object extracted from the {@link ResultSet}.
	 * 
	 * @throws IllegalStateException
	 *             if more than one object could be extracted from the {@link ResultSet}.
	 */
	public T toSingleResult() {
		final List<T> res = new ArrayList<>(1);
		forEach(res::add);
		if (res.size() > 1) throw new IllegalStateException(
				"The query returned several objects when only 1 was expected. The query was:\n" + sql);
		return res.size() == 1 ? res.get(0) : null;
	}

	/**
	 * Returns the raw SQL string.
	 */
	public String getSql() {
		return sql;
	}
}

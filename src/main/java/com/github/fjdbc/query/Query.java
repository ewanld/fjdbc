package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.IntSequence;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.RuntimeSQLException;
import com.github.fjdbc.SQLConsumer;

/**
 * Represent an SQL SELECT statement.
 * @param <T>
 *        The type of objects to be extracted from the {@link ResultSet}}.
 */
public class Query<T> {
	private final String sql;
	private PreparedStatementBinder binder;
	private final ResultSetExtractor<T> extractor;
	private ConnectionProvider cnxProvider;
	private SQLConsumer<Statement> beforeExecutionConsumer;
	private SQLConsumer<Statement> afterExecutionConsumer;

	/**
	 * Create a new query.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * @param connectionProvider
	 *        The provider of {@link Connection} instances.
	 * @param sql
	 *        The raw SQL string. It should be a SELECT statement.
	 * @param binder
	 *        The binder of {@link PreparedStatement} parameters, or {@code null} if this query does not have
	 *        parameters.
	 * @param extractor
	 *        Extracts individual objects from a {@link ResultSet}.
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
	 * Create a new query.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * @param connectionProvider
	 *        The provider of {@link Connection} instances.
	 * @param sql
	 *        The raw SQL string. It should be a SELECT statement.
	 * @param extractor
	 *        Extracts individual objects from a {@link ResultSet}.
	 */
	public Query(ConnectionProvider connectionProvider, String sql, ResultSetExtractor<T> extractor) {
		this(connectionProvider, sql, null, extractor);
	}

	/**
	 * User-defined code to be executed after the {@link Statement} is created, but before it is executed.
	 * <p>
	 * This can be useful for setting Statement options, such as {@link Statement#setFetchSize(int)},
	 * {@link Statement#setMaxRows(int)}, etc.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 */
	public Query<T> doBeforeExecution(SQLConsumer<Statement> statementConsumer) {
		this.beforeExecutionConsumer = statementConsumer;
		return this;
	}

	/**
	 * User-defined code to be executed after the {@link Statement} is executed, but before the {@link ResultSet} is
	 * read.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 * @return
	 */
	public Query<T> doAfterExecution(SQLConsumer<Statement> statementConsumer) {
		this.afterExecutionConsumer = statementConsumer;
		return this;
	}

	/**
	 * Bind parameters to the {@code PreparedStatement}.
	 * <p>
	 * Implementors should only call {@code setXXX} methods from the {@link PreparedStatement}.
	 */
	public Query<T> setBinder(PreparedStatementBinder binder) {
		this.binder = binder;
		return this;
	}

	private boolean isPrepared() {
		return binder != null;
	}

	/**
	 * Execute the query, then calls the specified callback for each object extracted from the {@link ResultSet}.
	 */
	public void forEach(Consumer<? super T> callback) {
		Statement st = null;
		Connection cnx = null;
		try {
			cnx = cnxProvider.borrow();
			st = isPrepared() ? cnx.prepareStatement(sql) : cnx.createStatement();
			if (isPrepared()) binder.bind((PreparedStatement) st, new IntSequence(1));
			if (beforeExecutionConsumer != null) beforeExecutionConsumer.accept(st);
			final ResultSet rs = isPrepared() ? ((PreparedStatement) st).executeQuery() : st.executeQuery(sql);
			if (afterExecutionConsumer != null) afterExecutionConsumer.accept(st);
			extractor.iterator(rs).forEachRemaining(callback);
		} catch (final SQLException e) {
			throw new RuntimeSQLException("Error executing query:\n" + sql, e);
		} finally {
			close(st);
			cnxProvider.giveBack(cnx);
		}
	}

	/**
	 * Execute the query, then loop through the ResultSet, discarding the extracted objects.
	 */
	public void process() {
		forEach(c -> {});
	}

	/**
	 * Warning: the returned stream must be closed manually by the caller.
	 */
	public Stream<T> stream() {
		try {
			final Connection cnx = cnxProvider.borrow();
			final Statement st = isPrepared() ? cnx.prepareStatement(sql) : cnx.createStatement();
			if (isPrepared()) binder.bind((PreparedStatement) st, new IntSequence(1));
			if (beforeExecutionConsumer != null) beforeExecutionConsumer.accept(st);
			final ResultSet rs = isPrepared() ? ((PreparedStatement) st).executeQuery() : st.executeQuery(sql);
			if (afterExecutionConsumer != null) afterExecutionConsumer.accept(st);
			final Stream<T> res = StreamSupport
					.stream(Spliterators.spliteratorUnknownSize(extractor.iterator(rs), Spliterator.ORDERED), false);
			res.onClose(() -> {
				closeStatement(rs);
				cnxProvider.giveBack(cnx);
			});
			return res;
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	/**
	 * Execute the query, the collect the objects extracted from the {@link ResultSet} using the specified collector.
	 * @param <A> the mutable accumulation type of the reduction operation (often
	 *        hidden as an implementation detail)
	 * @param <R> the result type of the reduction operation
	 */
	public <A, R> R collect(Collector<T, A, R> collector) {
		final A resultContainer = collector.supplier().get();
		forEach(t -> collector.accumulator().accept(resultContainer, t));
		return collector.finisher().apply(resultContainer);
	}

	static void close(Statement st) {
		try {
			if (st != null) st.close();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	static void close(ResultSet rs) {
		try {
			if (rs != null) rs.close();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	private static void closeStatement(ResultSet rs) {
		try {
			if (rs != null) rs.getStatement().close();
		} catch (final SQLException e) {
			// no op
		}
	}

	/**
	 * Execute the query, then returns a list of objects extracted from the {@link ResultSet}.
	 * <p>
	 * Convenience method; it is equivalent to:
	 * 
	 * <pre>
	 * List&lt;T&gt; list = new ArrayList&lt;&gt;();
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
	 * @throws IllegalStateException
	 *         if more than one object could be extracted from the {@link ResultSet}.
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

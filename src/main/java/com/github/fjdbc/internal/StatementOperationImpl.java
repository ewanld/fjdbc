package com.github.fjdbc.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.IntSequence;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.RuntimeSQLException;
import com.github.fjdbc.SQLConsumer;
import com.github.fjdbc.op.StatementOperation;

/**
 * Represent a database operation that modifies rows: insert, update, delete, etc.
 * <p>
 * Supports prepared statements, and batch statements.
 */
public class StatementOperationImpl implements StatementOperation {
	private final String sql;
	private final PreparedStatementBinder binder;
	private final ConnectionProvider cnxProvider;
	private final List<SQLConsumer<Statement>> beforeExecutionConsumers = new ArrayList<>(2);
	private final List<SQLConsumer<Statement>> afterExecutionConsumers = new ArrayList<>(2);

	/**
	 * Create a statement.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * @param cnxProvider
	 *        The provider of {@link Connection} instances.
	 * @param sql
	 *        The raw SQL string to be executed.
	 */
	public StatementOperationImpl(ConnectionProvider cnxProvider, String sql) {
		this(cnxProvider, sql, null);
	}

	/**
	 * @param cnxProvider
	 *        The provider of {@link Connection} instances.
	 * @param sql
	 *        The raw SQL string to be executed.
	 * @param binder
	 *        The binder of {@link PreparedStatement} parameters, or {@code null} if this query does not have
	 *        parameters.
	 */
	public StatementOperationImpl(ConnectionProvider cnxProvider, String sql, PreparedStatementBinder binder) {
		assert cnxProvider != null;
		assert sql != null;

		this.cnxProvider = cnxProvider;
		this.sql = sql;
		this.binder = binder;
	}

	private boolean isPrepared() {
		return binder != null;
	}

	/**
	 * User-defined code to be executed after the {@link Statement} is created, but before it is executed.
	 * <p>
	 * This can be useful for setting Statement options, such as {@link Statement#setFetchSize(int)} for instance.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 */
	@Override
	public StatementOperation doBeforeExecution(SQLConsumer<Statement> statementConsumer) {
		this.beforeExecutionConsumers.add(statementConsumer);
		return this;
	}

	/**
	 * User-defined code to be executed after the {@link Statement} is executed.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 */
	@Override
	public StatementOperation doAfterExecution(SQLConsumer<Statement> statementConsumer) {
		this.afterExecutionConsumers.add(statementConsumer);
		return this;
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		assert cnx != null;
		return isPrepared() ? execute_preparedStatement(cnx) : execute_regularStatement(cnx);
	}

	private void executeBeforeExecutionConsumers(Statement st) throws SQLException {
		for (final SQLConsumer<Statement> c : beforeExecutionConsumers) {
			c.accept(st);
		}
	}

	private void executeAfterExecutionConsumers(Statement st) throws SQLException {
		for (final SQLConsumer<Statement> c : afterExecutionConsumers) {
			c.accept(st);
		}
	}

	private int execute_regularStatement(Connection cnx) throws SQLException {
		try (Statement st = cnx.createStatement()) {
			executeBeforeExecutionConsumers(st);
			final int modifiedRows = st.executeUpdate(sql);
			executeAfterExecutionConsumers(st);
			return modifiedRows;
		}
	}

	private int execute_preparedStatement(Connection cnx) throws SQLException {
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			final PreparedStatementEx psx = new PreparedStatementEx(ps);
			binder.bind(psx, new IntSequence(1));
			executeBeforeExecutionConsumers(ps);
			final int nRows;
			if (psx.isBatch()) {
				final int[] nRows_array = ps.executeBatch();
				nRows = getNRowsModifiedByBatch(nRows_array);
			} else {
				nRows = ps.executeUpdate();
			}
			executeAfterExecutionConsumers(ps);
			return nRows;
		}
	}

	private int getNRowsModifiedByBatch(int[] modifiedRows) {
		int sum = 0;
		for (final int r : modifiedRows) {
			if (r == Statement.SUCCESS_NO_INFO) {
				return Statement.SUCCESS_NO_INFO;
			} else if (r == Statement.EXECUTE_FAILED) {
				return Statement.EXECUTE_FAILED;
			} else {
				sum += r;
			}
		}
		return sum;
	}

	@Override
	public int executeAndCommit() {
		Connection cnx = null;
		try {
			cnx = cnxProvider.borrow();
			final int modifiedRows = execute(cnx);
			cnxProvider.commit(cnx);
			return modifiedRows;
		} catch (final SQLException e) {
			throw new RuntimeSQLException("Error executing the SQL statement: " + sql, e);
		} finally {
			// if the connection was already committed, roll back should be a no op.
			cnxProvider.rollback(cnx);
			cnxProvider.giveBack(cnx);
		}
	}
}
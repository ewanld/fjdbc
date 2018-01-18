package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.IntSequence;
import com.github.fjdbc.RuntimeSQLException;
import com.github.fjdbc.SQLConsumer;
import com.github.fjdbc.internal.PreparedStatementEx;
import com.github.fjdbc.query.Query;
import com.github.fjdbc.PreparedStatementBinder;

/**
 * Represent a database operation that modifies rows: insert, update, delete, etc.
 * <p>
 * Supports prepared statements, and batch statements.
 */
public class StatementOperation implements DbOperation {
	private final String sql;
	private PreparedStatementBinder binder;
	private final ConnectionProvider cnxProvider;
	private SQLConsumer<Statement> beforeExecutionConsumer;
	private SQLConsumer<Statement> afterExecutionConsumer;

	/**
	 * Create a statement.
	 * <p>
	 * No actual connection is established with the database until the statement is executed.
	 * 
	 * @param cnxProvider
	 *            The provider of {@link Connection} instances.
	 * @param sql
	 *            The raw SQL string to be executed.
	 */
	public StatementOperation(ConnectionProvider cnxProvider, String sql) {
		this(cnxProvider, sql, null);
	}

	/**
	 * @param cnxProvider
	 *            The provider of {@link Connection} instances.
	 * @param sql
	 *            The raw SQL string to be executed.
	 * @param binder
	 *            The binder of {@link PreparedStatement} parameters, or {@code null} if this query does not have
	 *            parameters.
	 */
	public StatementOperation(ConnectionProvider cnxProvider, String sql, PreparedStatementBinder binder) {
		assert cnxProvider != null;
		assert sql != null;

		this.cnxProvider = cnxProvider;
		this.sql = sql;
		this.binder = binder;
	}

	public StatementOperation setBinder(PreparedStatementBinder binder) {
		this.binder = binder;
		return this;
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
	public StatementOperation doBeforeExecution(SQLConsumer<Statement> statementConsumer) {
		this.beforeExecutionConsumer = statementConsumer;
		return this;
	}

	/**
	 * User-defined code to be executed after the {@link Statement} is executed.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 * 
	 * @return
	 */
	public StatementOperation doAfterExecution(SQLConsumer<Statement> statementConsumer) {
		this.afterExecutionConsumer = statementConsumer;
		return this;
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		assert cnx != null;
		return isPrepared() ? execute_preparedStatement(cnx) : execute_regularStatement(cnx);
	}

	private int execute_regularStatement(Connection cnx) throws SQLException {
		try (Statement st = cnx.createStatement()) {
			if (beforeExecutionConsumer != null) beforeExecutionConsumer.accept(st);
			final int modifiedRows = st.executeUpdate(sql);
			if (afterExecutionConsumer != null) afterExecutionConsumer.accept(st);
			return modifiedRows;
		}
	}

	private int execute_preparedStatement(Connection cnx) throws SQLException {
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			final PreparedStatementEx psx = new PreparedStatementEx(ps);
			binder.bind(psx, new IntSequence(1));
			if (beforeExecutionConsumer != null) beforeExecutionConsumer.accept(ps);
			final int nRows;
			if (psx.isBatch()) {
				final int[] nRows_array = ps.executeBatch();
				nRows = getNRowsModifiedByBatch(nRows_array);
			} else {
				nRows = ps.executeUpdate();
			}
			if (afterExecutionConsumer != null) afterExecutionConsumer.accept(ps);
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
		try {
			final int modifiedRows = execute(cnxProvider.borrow());
			cnxProvider.commit();
			return modifiedRows;
		} catch (final SQLException e) {
			throw new RuntimeSQLException("Error executing the SQL statement: " + sql, e);
		} finally {
			// if the connection was already committed, roll back should be a no op.
			cnxProvider.rollback();
			cnxProvider.giveBack();
		}
	}
}
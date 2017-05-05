package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.util.IntSequence;
import com.github.fjdbc.util.PreparedStatementEx;

/**
 * Represent a database operation that return a row count: insert, update, delete, etc.<br>
 * Supports prepared statements, and batch statements.
 */
public class StatementOperation implements DbOperation {
	private final String sql;
	private final PreparedStatementBinder binder;
	private final ConnectionProvider cnxProvider;

	public StatementOperation(ConnectionProvider cnxProvider, String sql) {
		this(cnxProvider, sql, null);
	}

	public StatementOperation(ConnectionProvider cnxProvider, String sql, PreparedStatementBinder binder) {
		assert cnxProvider != null;
		assert sql != null;

		this.cnxProvider = cnxProvider;
		this.sql = sql;
		this.binder = binder;
	}

	private boolean isPrepared() {
		return binder != null;
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		assert cnx != null;
		return isPrepared() ? execute_preparedStatement(cnx) : execute_regularStatement(cnx);
	}

	private int execute_regularStatement(Connection cnx) throws SQLException {
		try (Statement st = cnx.createStatement()) {
			final int modifiedRows = st.executeUpdate(sql);
			return modifiedRows;
		}
	}

	private int execute_preparedStatement(Connection cnx) throws SQLException {
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			final PreparedStatementEx psx = new PreparedStatementEx(ps);
			binder.bind(psx, new IntSequence(1));
			if (psx.isBatch()) {
				final int[] nRows = ps.executeBatch();
				return getNRowsModifiedByBatch(nRows);
			} else {
				final int nRows = ps.executeUpdate();
				return nRows;
			}
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
			throw new FjdbcException(e);
		} finally {
			cnxProvider.giveBack();
		}
	}
}
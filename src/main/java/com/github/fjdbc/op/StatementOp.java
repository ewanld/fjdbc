package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.util.PreparedStatementEx;

/**
 * Represent a database operation that return a row count: insert, update, delete, etc.<br>
 * Supports prepared statements, and batch statements.
 */
public class StatementOp implements DbOp {
	private final String sql;
	private final PreparedStatementBinder binder;

	public StatementOp(String sql) {
		this(sql, null);
	}

	public StatementOp(String sql, PreparedStatementBinder binder) {
		assert sql != null;

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
			binder.bind(psx);
			if (psx.isBatch()) {
				final int[] nRows = ps.executeBatch();
				return getNRowsModifiedByBatch(nRows);
			} else {
				final int nRows = psx.executeUpdate();
				return nRows;
			}
		}
	}

	private int getNRowsModifiedByBatch(int[] modifiedRows) {
		int sum = 0;
		for (final int r : modifiedRows) {
			if (r == PreparedStatement.SUCCESS_NO_INFO) {
				return PreparedStatement.SUCCESS_NO_INFO;
			} else if (r == PreparedStatement.EXECUTE_FAILED) {
				return PreparedStatement.EXECUTE_FAILED;
			} else {
				sum += r;
			}
		}
		return sum;
	}

	@Override
	public int executeAndCommit(ConnectionProvider cnxProvider) {
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
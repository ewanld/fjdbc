package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Wraps a {@link java.sql.PreparedStatement}.
 */
public class PreparedStatementOp implements DbOp {
	private final String sql;
	private final PreparedStatementBinder binder;

	public PreparedStatementOp(String sql, PreparedStatementBinder binder) {
		this.sql = sql;
		this.binder = binder;
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		assert cnx != null;
		PreparedStatement ps = null;
		try {
			ps = cnx.prepareStatement(sql);
			final PreparedStatementDelegate psDelegate = new PreparedStatementDelegate(ps);
			binder.bind(psDelegate);
			if (psDelegate.isBatch()) {
				final int[] nRows = ps.executeBatch();
				return getNRowsModifiedByBatch(nRows);
			} else {
				final int nRows = ps.executeUpdate();
				return nRows;
			}
		} catch(final SQLException e) {
			FjdbcUtil.close(ps);
			throw new FjdbcException(e);
		}
	}

	private int getNRowsModifiedByBatch(int[] modifiedRows) {
		int sum = 0;
		for (final int r : modifiedRows) {
			if (r == PreparedStatement.SUCCESS_NO_INFO) {
				return PreparedStatement.SUCCESS_NO_INFO;
			} else if (r== PreparedStatement.EXECUTE_FAILED) {
				return PreparedStatement.EXECUTE_FAILED;
			} else {
				sum += r;
			}
		}
		return sum;
	}

	@Override
	public int executeAndCommit(Supplier<Connection> cnxSupplier) {
		Connection cnx = null;
		try {
			cnx = cnxSupplier.get();
			final int modifiedRows = execute(cnx);
			return modifiedRows;
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			FjdbcUtil.closeConnection(cnx);
		}
	}
}
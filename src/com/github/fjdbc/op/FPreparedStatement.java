package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.IPreparedStatementBinder;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Wraps a {@link java.sql.PreparedStatement}.
 */
public class FPreparedStatement implements Op {
	private final String sql;
	private final IPreparedStatementBinder binder;

	public FPreparedStatement(String sql, IPreparedStatementBinder binder) {
		this.sql = sql;
		this.binder = binder;
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		assert cnx != null;
		PreparedStatement ps = null;
		try {
			ps = cnx.prepareStatement(sql);
			binder.bind(ps);
			final int modifiedRows = binder.execute(ps);
			return modifiedRows;
		} finally {
			FjdbcUtil.close(ps);
		}
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
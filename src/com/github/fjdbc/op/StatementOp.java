package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Wraps a {@link java.sql.Statement}.
 */
public class StatementOp implements DbOp {
	private final String sql;

	public StatementOp(String sql) {
		assert sql != null;
		this.sql = sql;
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		assert cnx != null;
		Statement st = null;
		try {
			st = cnx.createStatement();
			final int modifiedRows = st.executeUpdate(sql);
			return modifiedRows;
		} catch (final SQLException e) {
			FjdbcUtil.close(st);
			throw new FjdbcException(e);
		}
	}

	@Override
	public int executeAndCommit(Connection cnx) {
		try {
			final int modifiedRows = execute(cnx);
			return modifiedRows;
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			FjdbcUtil.closeConnection(cnx);
		}
	}
}
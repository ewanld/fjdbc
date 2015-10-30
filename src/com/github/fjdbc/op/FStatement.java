package com.github.fjdbc.op;

import static com.github.fjdbc.util.FjdbcUtil.close;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Wraps a {@link java.sql.Statement}.
 */
public class FStatement implements Op {
	private final String sql;

	public FStatement(String sql) {
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
		} finally {
			FjdbcUtil.close(cnx, st);
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
			close(cnx);
		}
	}
}
package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Represents a sequence of statements that must be executed in a single transaction.
 */
public class CompositeOp implements DbOp {
	private final DbOp[] operations;
	public static Logger logger = Logger.getLogger(CompositeOp.class.getName());

	public CompositeOp(DbOp... operations) {
		this.operations = operations;
	}

	public CompositeOp(Collection<DbOp> operations) {
		this(operations.toArray(new DbOp[0]));
	}

	@Override
	public int executeAndCommit(Connection cnx) {
		if (operations.length == 0) return 0;

		try {
			final int modifiedRows = execute(cnx);
			cnx.commit();
			return modifiedRows;

		} catch (final Exception e) {
			FjdbcUtil.rollbackConnection(cnx);
			throw new FjdbcException(e);
		} finally {
			FjdbcUtil.closeConnection(cnx);
		}
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		int modifiedRows = 0;
		for (int i = 0; i < operations.length; i++) {
			final DbOp t = operations[i];
			try {
				modifiedRows += t.execute(cnx);
			} catch (final Exception e) {
				throw new FjdbcException("DB Operation " + (i + 1) + "/" + operations.length + " failed", e);
			}
		}
		return modifiedRows;
	}
}
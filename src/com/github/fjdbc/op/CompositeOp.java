package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Represents a sequence of statements that must be executed in a single transaction.
 */
public class CompositeOp implements Op {
	private final Op[] operations;
	public static Logger logger = Logger.getLogger(CompositeOp.class.getName());

	public CompositeOp(Op... operations) {
		this.operations = operations;
	}

	public CompositeOp(Collection<Op> operations) {
		this.operations = operations.toArray(new Op[0]);
	}

	@Override
	public int executeAndCommit(Supplier<Connection> cnxSupplier) {
		if (operations.length == 0)
			return 0;

		Connection cnx = null;
		try {
			cnx = cnxSupplier.get();
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
			final Op t = operations[i];
			try {
				modifiedRows += t.execute(cnx);
			} catch (final Exception e) {
				throw new FjdbcException("DB Operation " + (i + 1) + "/" + operations.length + " failed", e);
			}

		}
		return modifiedRows;
	}
}
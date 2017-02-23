package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.FjdbcException;

/**
 * Represents a sequence of operations that must be executed in a single transaction.
 */
public class CompositeOperation implements DbOperation {
	private final DbOperation[] operations;

	public CompositeOperation(DbOperation... operations) {
		this.operations = operations;
	}

	public CompositeOperation(Collection<DbOperation> operations) {
		this(operations.toArray(new DbOperation[0]));
	}

	@Override
	public int executeAndCommit(ConnectionProvider cnxProvider) {
		if (operations.length == 0) return 0;

		try {
			final int modifiedRows = execute(cnxProvider.borrow());
			cnxProvider.commit();
			return modifiedRows;
		} catch (final Exception e) {
			cnxProvider.rollback();
			throw new FjdbcException(e);
		} finally {
			cnxProvider.giveBack();
		}
	}

	@Override
	public int execute(Connection cnx) throws SQLException {
		int modifiedRows = 0;
		for (int i = 0; i < operations.length; i++) {
			final DbOperation t = operations[i];
			try {
				modifiedRows += t.execute(cnx);
			} catch (final Exception e) {
				throw new FjdbcException(String.format("DB Operation %s/%s failed!", i + 1, operations.length),
						e);
			}

		}
		return modifiedRows;
	}
}
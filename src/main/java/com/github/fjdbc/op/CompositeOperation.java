package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.RuntimeSQLException;

/**
 * Merge a sequence of {@link DbOperation} as a single {@link DbOperation}.
 * <p>
 * This allows the operations be executed in a single transaction.
 */
public class CompositeOperation implements DbOperation {
	private final DbOperation[] operations;
	private final ConnectionProvider cnxProvider;

	/**
	 * @param connectionProvider
	 *            The provider of {@link Connection} instances.
	 * @param operations
	 *            The sequence of
	 */
	public CompositeOperation(ConnectionProvider connectionProvider, DbOperation... operations) {
		this.cnxProvider = connectionProvider;
		this.operations = operations;
	}

	public CompositeOperation(ConnectionProvider cnxProvider, Collection<? extends DbOperation> operations) {
		this(cnxProvider, operations.toArray(new DbOperation[0]));
	}

	@Override
	public int executeAndCommit() {
		if (operations.length == 0) return 0;

		try {
			final int modifiedRows = execute(cnxProvider.borrow());
			cnxProvider.commit();
			return modifiedRows;
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		} finally {
			// if the connection was already committed, roll back should be a no op.
			cnxProvider.rollback();
			cnxProvider.giveBack();
		}
	}

	@Override
	public int execute(Connection cnx) {
		int modifiedRows = 0;
		for (int i = 0; i < operations.length; i++) {
			final DbOperation t = operations[i];
			try {
				modifiedRows += t.execute(cnx);
			} catch (final SQLException e) {
				throw new RuntimeSQLException(String.format("DB Operation %s/%s failed!", i + 1, operations.length), e);
			}

		}
		return modifiedRows;
	}
}
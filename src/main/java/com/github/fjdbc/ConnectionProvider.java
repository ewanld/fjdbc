package com.github.fjdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * A provider of {@link Connection} instances.
 * <p>
 * It abstracts how Connections are borrowed, returned, committed and and rolled back. The general contract is that, for
 * each call to {@link #borrow()}, there should be a following call to {@link #giveBack()}.
 * <p>
 * This class is not thread safe: each thread should create its own {@code ConnectionProvider} instance.
 */
public abstract class ConnectionProvider {
	private Connection currentCnx;

	/**
	 * Borrow a connection. This call should be followed to call to {@link #giveBack()}.
	 */
	public final Connection borrow() throws SQLException {
		if (currentCnx != null) throw new IllegalStateException(
				"borrow() was called but the previous connection was not returned. Call giveBack() first.");
		currentCnx = doBorrow();
		Objects.requireNonNull(currentCnx);
		return currentCnx;
	}

	protected abstract Connection doBorrow() throws SQLException;

	/**
	 * Return a previsouly aquired connection.
	 */
	public final void giveBack() throws RuntimeSQLException {
		if (currentCnx == null) return;

		try {
			doGiveBack(currentCnx);
			currentCnx = null;
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	protected abstract void doGiveBack(Connection cnx) throws SQLException;

	public void commit() {
		try {
			if (currentCnx.getAutoCommit()) return;
			currentCnx.commit();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	public void rollback() {
		try {
			if (currentCnx.getAutoCommit()) return;
			currentCnx.rollback();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}
}

package com.github.fjdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public abstract class ConnectionProvider {
	private Connection currentCnx;

	public final Connection borrow() throws SQLException {
		if (currentCnx != null) throw new IllegalStateException(
				"borrow() was called but the previous connection was not returned. Call giveBack() first.");
		currentCnx = doBorrow();
		Objects.requireNonNull(currentCnx);
		return currentCnx;
	}

	protected abstract Connection doBorrow() throws SQLException;

	public final void giveBack() throws FjdbcException {
		if (currentCnx == null) return;

		try {
			doGiveBack(currentCnx);
			currentCnx = null;
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}

	protected abstract void doGiveBack(Connection cnx) throws SQLException;

	public void commit() {
		try {
			currentCnx.commit();
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}

	public void rollback() {
		try {
			currentCnx.rollback();
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}
}

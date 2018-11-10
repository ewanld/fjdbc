package com.github.fjdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A provider of {@link Connection} instances.
 * <p>
 * It abstracts how Connections are borrowed, returned, committed and and rolled back. The general contract is that,
 * for each call to {@link #borrow()}, there should be a following call to {@link #giveBack(Connection)}.
 * <p>
 * This class is thread safe.
 */
public interface ConnectionProvider {

	/**
	 * Borrow a connection. This call should be followed to call to {@link #giveBack(Connection)}.
	 */
	Connection borrow() throws SQLException;

	/**
	 * Return a previsouly aquired connection.
	 */
	public default void giveBack(Connection cnx) throws RuntimeSQLException {
		if (cnx == null) return;
		try {
			cnx.close();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	public default void commit(Connection cnx) {
		if (cnx == null) return;
		try {
			if (cnx.getAutoCommit()) return;
			cnx.commit();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	public default void rollback(Connection cnx) {
		if (cnx == null) return;
		try {
			if (cnx.getAutoCommit()) return;
			cnx.rollback();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}
}

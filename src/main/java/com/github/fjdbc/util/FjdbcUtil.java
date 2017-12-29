package com.github.fjdbc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.RuntimeSQLException;

/**
 * Utility methods
 */
public class FjdbcUtil {
	public static void close(Connection cnx, Statement... statements) {
		try {
			for (final Statement st : statements) {
				if (st != null) st.close();
			}
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		} finally {
			closeConnection(cnx);
		}
	}

	public static void closeConnection(Connection cnx) {
		if (cnx == null) return;
		try {
			cnx.close();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}
}

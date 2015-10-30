package com.github.fjdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.FjdbcException;

/**
 * Utility methods
 */
public class FjdbcUtil {
	public static void close(Connection cnx, Statement st, ResultSet rs) throws FjdbcException {
		try {
			if (rs != null) rs.close();
			if (st != null) st.close();
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			closeConnection(cnx);
		}
	}

	public static void close(Statement st) {
		close(null, st);
	}
	
	public static void close(Connection cnx, Statement... statements) {
		try {
			for (final Statement st : statements) {
				if (st != null) st.close();
			}
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			closeConnection(cnx);
		}
	}

	public static void closeConnection(Connection cnx) {
		if (cnx != null) try {
			cnx.close();
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}

	public static void rollbackConnection(Connection cnx) {
		try {
			cnx.rollback();
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}

}

package com.github.fjdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.RuntimeSQLException;

/**
 * Utility methods
 */
public class FjdbcUtil {
	public static void close(ResultSet rs) throws RuntimeSQLException {
		try {
			if (rs != null) rs.close();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	public static PreparedStatement prepareStatement(Connection cnx, String sql) {
		PreparedStatement ps = null;
		try {
			ps = cnx.prepareStatement(sql);
			return ps;
		} catch (final SQLException e) {
			FjdbcUtil.close(ps);
			throw new RuntimeSQLException(e);
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

	public static void rollbackQuitely(Connection cnx) {
		if (cnx == null) return;
		try {
			cnx.rollback();
		} catch (final SQLException e) {
			// do nothing
		}
	}

	public static void rollbackConnection(Connection cnx) {
		if (cnx == null) return;
		try {
			cnx.rollback();
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

}

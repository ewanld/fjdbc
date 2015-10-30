package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The {@link #bind} method should only call the setXXX methods of the prepared statement.
 */
@FunctionalInterface
public interface PreparedStatementBinder extends  IPreparedStatementBinder {
	@Override
	default int execute(PreparedStatement ps) throws SQLException {
		final int modifiedRows = ps.executeUpdate();
		return modifiedRows;
	}

}
package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BatchPreparedStatementBinder implements IPreparedStatementBinder {
	@Override
	public int execute(PreparedStatement ps) throws SQLException {
		ps.executeBatch();
		return -1;
	}

}

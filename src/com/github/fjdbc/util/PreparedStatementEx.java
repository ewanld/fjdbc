package com.github.fjdbc.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Keeps track if the statement is a batch statement or a single statement.
 */
public class PreparedStatementEx extends PreparedStatementDelegate {
	private boolean isBatch = false;
	
	public PreparedStatementEx(PreparedStatement ps) {
		super(ps);
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		super.addBatch(sql);
		isBatch = true;
	}

	@Override
	public void addBatch() throws SQLException {
		super.addBatch();
		isBatch = true;
	}

	@Override
	public void clearBatch() throws SQLException {
		super.clearBatch();
		isBatch = false;
	}

	public boolean isBatch() {
		return isBatch;
	}
	
	public void setInt(int index, Integer value) throws SQLException {
		if (value == null) ps.setNull(index, Types.INTEGER);
		else setInt(index, value.intValue());
	}

}

package com.github.fjdbc;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps track if the statement is a batch statement or a single statement.
 */
public class PreparedStatementEx extends PreparedStatementDelegate {
	//@formatter:off
	public static final Set<Class<?>> jdbcTypes = new HashSet<>(Arrays.asList(
		String.class,
		BigDecimal.class,
		Boolean.class,
		Integer.class,
		Long.class,
		Float.class,
		Double.class,
		byte[].class,
		java.sql.Date.class,
		Time.class,
		Timestamp.class,
		Clob.class,
		Blob.class,
		Array.class,
		Ref.class,
		URL.class
	));
	//@formatter:on
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
}

package com.github.fjdbc.util;

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

	public <T> void setAnyObject(int columnIndex, Object o, Class<T> type) throws SQLException {
		// if (o == null) {
		// ps.setNull(columnIndex, java.sql.Types.OTHER);
		if (type.equals(String.class)) {
			ps.setString(columnIndex, (String) o);
		} else if (type.equals(BigDecimal.class)) {
			ps.setBigDecimal(columnIndex, (BigDecimal) o);
		} else if (type.equals(Boolean.class)) {
			ps.setBoolean(columnIndex, (Boolean) o);
		} else if (type.equals(Integer.class)) {
			ps.setInt(columnIndex, (Integer) o);
		} else if (type.equals(Long.class)) {
			ps.setLong(columnIndex, (Long) o);
		} else if (type.equals(Float.class)) {
			ps.setFloat(columnIndex, (Float) o);
		} else if (type.equals(Double.class)) {
			ps.setDouble(columnIndex, (Double) o);
		} else if (type.equals(byte[].class)) {
			ps.setBytes(columnIndex, (byte[]) o);
		} else if (type.equals(java.sql.Date.class)) {
			ps.setDate(columnIndex, (Date) o);
		} else if (type.equals(Time.class)) {
			ps.setTime(columnIndex, (Time) o);
		} else if (type.equals(Timestamp.class)) {
			ps.setTimestamp(columnIndex, (Timestamp) o);
		} else if (type.equals(Clob.class)) {
			ps.setClob(columnIndex, (Clob) o);
		} else if (type.equals(Blob.class)) {
			ps.setBlob(columnIndex, (Blob) o);
		} else if (type.equals(Array.class)) {
			ps.setArray(columnIndex, (Array) o);
		} else if (type.equals(Ref.class)) {
			ps.setRef(columnIndex, (Ref) o);
		} else if (type.equals(URL.class)) {
			ps.setURL(columnIndex, (URL) o);
		}
	}

}

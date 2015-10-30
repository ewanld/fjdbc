package com.github.fjdbc.query;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Read-only wrapper for a ResultSet. Exposes only the getXXX methods from the wrapped ResultSet.<br>
 * To generate this class with Eclipse, go to Refactor->Generate delegate methods, and select only the getXXX methods.
 */
public class ResultSetView {
	private final ResultSet wrapped;

	public ResultSetView(ResultSet wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getString(int)
	 */
	public String getString(int columnIndex) throws SQLException {
		return wrapped.getString(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int columnIndex) throws SQLException {
		return wrapped.getBoolean(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getByte(int)
	 */
	public byte getByte(int columnIndex) throws SQLException {
		return wrapped.getByte(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getShort(int)
	 */
	public short getShort(int columnIndex) throws SQLException {
		return wrapped.getShort(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getInt(int)
	 */
	public int getInt(int columnIndex) throws SQLException {
		return wrapped.getInt(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getLong(int)
	 */
	public long getLong(int columnIndex) throws SQLException {
		return wrapped.getLong(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	public float getFloat(int columnIndex) throws SQLException {
		return wrapped.getFloat(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	public double getDouble(int columnIndex) throws SQLException {
		return wrapped.getDouble(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	public byte[] getBytes(int columnIndex) throws SQLException {
		return wrapped.getBytes(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(int)
	 */
	public Date getDate(int columnIndex) throws SQLException {
		return wrapped.getDate(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(int)
	 */
	public Time getTime(int columnIndex) throws SQLException {
		return wrapped.getTime(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return wrapped.getTimestamp(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return wrapped.getAsciiStream(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return wrapped.getBinaryStream(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	public String getString(String columnLabel) throws SQLException {
		return wrapped.getString(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String columnLabel) throws SQLException {
		return wrapped.getBoolean(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	public byte getByte(String columnLabel) throws SQLException {
		return wrapped.getByte(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	public short getShort(String columnLabel) throws SQLException {
		return wrapped.getShort(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	public int getInt(String columnLabel) throws SQLException {
		return wrapped.getInt(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	public long getLong(String columnLabel) throws SQLException {
		return wrapped.getLong(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	public float getFloat(String columnLabel) throws SQLException {
		return wrapped.getFloat(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(String columnLabel) throws SQLException {
		return wrapped.getDouble(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	public byte[] getBytes(String columnLabel) throws SQLException {
		return wrapped.getBytes(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	public Date getDate(String columnLabel) throws SQLException {
		return wrapped.getDate(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	public Time getTime(String columnLabel) throws SQLException {
		return wrapped.getTime(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return wrapped.getTimestamp(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		return wrapped.getAsciiStream(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return wrapped.getBinaryStream(columnLabel);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return wrapped.getWarnings();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getCursorName()
	 */
	public String getCursorName() throws SQLException {
		return wrapped.getCursorName();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return wrapped.getMetaData();
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(int)
	 */
	public Object getObject(int columnIndex) throws SQLException {
		return wrapped.getObject(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	public Object getObject(String columnLabel) throws SQLException {
		return wrapped.getObject(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return wrapped.getCharacterStream(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return wrapped.getCharacterStream(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return wrapped.getBigDecimal(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return wrapped.getBigDecimal(columnLabel);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRow()
	 */
	public int getRow() throws SQLException {
		return wrapped.getRow();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return wrapped.getFetchDirection();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return wrapped.getFetchSize();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getType()
	 */
	public int getType() throws SQLException {
		return wrapped.getType();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	public int getConcurrency() throws SQLException {
		return wrapped.getConcurrency();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getStatement()
	 */
	public Statement getStatement() throws SQLException {
		return wrapped.getStatement();
	}

	/**
	 * @param columnIndex
	 * @param map
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		return wrapped.getObject(columnIndex, map);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRef(int)
	 */
	public Ref getRef(int columnIndex) throws SQLException {
		return wrapped.getRef(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	public Blob getBlob(int columnIndex) throws SQLException {
		return wrapped.getBlob(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getClob(int)
	 */
	public Clob getClob(int columnIndex) throws SQLException {
		return wrapped.getClob(columnIndex);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getArray(int)
	 */
	public Array getArray(int columnIndex) throws SQLException {
		return wrapped.getArray(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @param map
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		return wrapped.getObject(columnLabel, map);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	public Ref getRef(String columnLabel) throws SQLException {
		return wrapped.getRef(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	public Blob getBlob(String columnLabel) throws SQLException {
		return wrapped.getBlob(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	public Clob getClob(String columnLabel) throws SQLException {
		return wrapped.getClob(columnLabel);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	public Array getArray(String columnLabel) throws SQLException {
		return wrapped.getArray(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return wrapped.getDate(columnIndex, cal);
	}

	/**
	 * @param columnLabel
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		return wrapped.getDate(columnLabel, cal);
	}

	/**
	 * @param columnIndex
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return wrapped.getTime(columnIndex, cal);
	}

	/**
	 * @param columnLabel
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		return wrapped.getTime(columnLabel, cal);
	}

	/**
	 * @param columnIndex
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return wrapped.getTimestamp(columnIndex, cal);
	}

	/**
	 * @param columnLabel
	 * @param cal
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		return wrapped.getTimestamp(columnLabel, cal);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getURL(int)
	 */
	public URL getURL(int columnIndex) throws SQLException {
		return wrapped.getURL(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	public URL getURL(String columnLabel) throws SQLException {
		return wrapped.getURL(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRowId(int)
	 */
	public RowId getRowId(int columnIndex) throws SQLException {
		return wrapped.getRowId(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getRowId(java.lang.String)
	 */
	public RowId getRowId(String columnLabel) throws SQLException {
		return wrapped.getRowId(columnLabel);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		return wrapped.getHoldability();
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getNClob(int)
	 */
	public NClob getNClob(int columnIndex) throws SQLException {
		return wrapped.getNClob(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getNClob(java.lang.String)
	 */
	public NClob getNClob(String columnLabel) throws SQLException {
		return wrapped.getNClob(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getSQLXML(int)
	 */
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return wrapped.getSQLXML(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getSQLXML(java.lang.String)
	 */
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return wrapped.getSQLXML(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getNString(int)
	 */
	public String getNString(int columnIndex) throws SQLException {
		return wrapped.getNString(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getNString(java.lang.String)
	 */
	public String getNString(String columnLabel) throws SQLException {
		return wrapped.getNString(columnLabel);
	}

	/**
	 * @param columnIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getNCharacterStream(int)
	 */
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return wrapped.getNCharacterStream(columnIndex);
	}

	/**
	 * @param columnLabel
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getNCharacterStream(java.lang.String)
	 */
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return wrapped.getNCharacterStream(columnLabel);
	}

}

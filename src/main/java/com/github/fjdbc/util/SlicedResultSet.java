package com.github.fjdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.github.fjdbc.FjdbcException;

public class SlicedResultSet extends ResultSetDelegate {
	private final String[] columnNames;
	/**
	 * Map<column name, current value> 
	 */
	private final Map<String, Object> currentValues = new HashMap<>();
	private boolean firstRowOfSlice = true;

	public SlicedResultSet(ResultSet rs, String... columnNames) {
		super(rs);
		this.columnNames = columnNames;
	}

	public boolean nextSlice() throws SQLException {
		// go to first row if before first row
		if (rs.isBeforeFirst() && !rs.next()) return false;
		firstRowOfSlice = true;
		return !rs.isAfterLast();
	}
	
	public Object getCurrentValue(String columnName) {
		final Object res = currentValues.get(columnName);
		return res;
	}
	
	@Override
	public boolean next() {
		try {
			if (firstRowOfSlice) {
				for (final String colName : columnNames) {
					currentValues.put(colName, rs.getObject(colName));
				}
				firstRowOfSlice = false;
				return true;
			}
			
			if (!rs.next()) {
				firstRowOfSlice = true;
				return false;
			}
			final boolean eq = isCurrentValueEqual(rs);
			return eq;

		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}
	
	public boolean isCurrentValueEqual(ResultSet rs) throws SQLException {
		for (final String colName : columnNames) {
			final Object val = rs.getObject(colName);
			final Object currentVal = currentValues.get(colName);
			final boolean eq = val.equals(currentVal);
			if (!eq) return false;
		}
		return true;
	}
}

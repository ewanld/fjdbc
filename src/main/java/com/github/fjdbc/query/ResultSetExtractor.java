package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Extract objects from a ResultSet. The object may map to one or many rows from the result set.
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {
	T extract(ResultSet rs) throws SQLException;

	default Iterator<T> iterator(ResultSet rs) {
		return new ResultSetIterator<>(rs, this);
	}

	/**
	 * An iterator backed by a ResultSet. The ResultSet is closed when the last element is read.
	 * 
	 * @param <T>
	 */
	public static class ResultSetIterator<T> implements Iterator<T> {
		private final ResultSet rs;
		private final ResultSetExtractor<T> extractor;
		private T nextValue;

		public ResultSetIterator(ResultSet rs, ResultSetExtractor<T> extractor) {
			this.rs = rs;
			this.extractor = extractor;
		}

		@Override
		public boolean hasNext() {
			maybeReadNext();
			return nextValue != null;
		}

		private void maybeReadNext() {
			if (nextValue != null) return;
			try {
				final boolean next = rs.next();
				if (!next) {
					nextValue = null;
					FjdbcUtil.close(rs);
				} else {
					nextValue = extractor.extract(rs);
				}
			} catch (final SQLException e) {
				FjdbcUtil.close(rs);
				throw new FjdbcException(e);
			}

		}

		@Override
		public T next() {
			maybeReadNext();
			final T res = nextValue;
			nextValue = null;
			return res;
		}

	}

}
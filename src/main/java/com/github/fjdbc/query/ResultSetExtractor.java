package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.github.fjdbc.util.Consumers;
import com.github.fjdbc.util.FjdbcUtil;

/**
 * Extract objects from a ResultSet. The object may map to one or many rows from the result set.
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {
	T extract(ResultSet rs) throws SQLException;

	default void extractAll(ResultSet rs, Consumer<? super T> callback) throws SQLException {
		while (rs.next()) {
			final T object = extract(rs);
			callback.accept(object);
		}
	}

	default List<T> extractAll(ResultSet rs) throws SQLException {
		final List<T> res = new ArrayList<>();
		extractAll(rs, Consumers.toList(res));
		return res;
	}

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
		private boolean _hasNext = true;
		/**
		 * True if resultSet.next() has been called at least once, false otherwise.
		 */
		private boolean nextCalled = false;

		public ResultSetIterator(ResultSet rs, ResultSetExtractor<T> extractor) {
			this.rs = rs;
			this.extractor = extractor;
		}

		@Override
		public boolean hasNext() {
			try {
				// need to call next() at least once otherwise isLast doesn't work (MySql).
				if (!nextCalled) {
					final boolean next = rs.next();
					nextCalled = true;
					if (!next) {
						FjdbcUtil.close(null, null, rs);
						_hasNext = false;
						return false;
					}
				}
				final boolean hasNext = _hasNext && !rs.isLast() && !rs.isAfterLast() && !rs.isClosed();
				if (!hasNext) FjdbcUtil.close(null, null, rs);
				return hasNext;
			} catch (final SQLException e) {
				FjdbcUtil.close(null, null, rs);
				throw new RuntimeException(e);
			}
		}

		@Override
		public T next() {
			try {
				final boolean next = rs.next();
				nextCalled = true;
				if (!next) {
					_hasNext = false;
					return null;
				}
				return extractor.extract(rs);
			} catch (final SQLException e) {
				FjdbcUtil.close(null, null, rs);
				throw new RuntimeException(e);
			}
		}

	}

}
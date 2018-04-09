package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.github.fjdbc.RuntimeSQLException;

/**
 * Extract objects of type {@code T} from a ResultSet.
 * <p>
 * Each object may map to one or many rows from the result set.
 */
public interface ResultSetExtractor<T> {
	/**
	 * Calls the specified callback everytime an object is extracted from the {@link ResultSet}.
	 * @return the extracted object. Implementors should return {@code null} to signal the extraction is over.<br>
	 *         {@code null} may be returned even if the end of the ResultSet has not been reached.
	 */
	T extract(ResultSet rs) throws SQLException;

	/**
	 * If {@code true}, {@link ResultSet#next()} is called once before the first call to {@link #extract(ResultSet)},
	 * and inbetween calls.<br>
	 * Otherwise, {@code next()} is never called and it is the responsibility of implementors to call it.
	 */
	boolean autoCallNext();

	/**
	 * Return an iterator that loops through the objects extracted from the ResultSet.
	 */
	default Iterator<T> iterator(ResultSet rs) {
		return new ResultSetIterator<>(rs, this);
	}

	/**
	 * An iterator backed by a ResultSet. The ResultSet is closed when the last element is read.
	 * @param <T>
	 */
	static class ResultSetIterator<T> implements Iterator<T> {
		private final ResultSet rs;
		private final ResultSetExtractor<T> extractor;
		private T nextValue;
		private boolean endReached = false;

		public ResultSetIterator(ResultSet rs, ResultSetExtractor<T> extractor) {
			this.rs = rs;
			this.extractor = extractor;
		}

		@Override
		public boolean hasNext() {
			maybeReadNext();
			return !endReached;
		}

		private void maybeReadNext() {
			if (endReached || nextValue != null) return;
			try {
				if (rs.isAfterLast()) {
					endReached();
					return;
				}
				if (extractor.autoCallNext()) {
					if (!rs.next()) {
						endReached();
						return;
					}
				}
				nextValue = extractor.extract(rs);
				if (nextValue == null) {
					endReached();
					return;
				}

			} catch (final SQLException e) {
				Query.close(rs);
				throw new RuntimeSQLException(e);
			}
		}

		private void endReached() {
			endReached = true;
			Query.close(rs);
			return;
		}

		@Override
		public T next() {
			if (endReached) throw new NoSuchElementException();
			maybeReadNext();
			final T res = nextValue;
			nextValue = null;
			return res;
		}

	}
}
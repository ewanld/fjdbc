package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.util.FjdbcUtil;

public class Query<T> {
	private final String sql;
	private final PreparedStatementBinder binder;
	private final ResultSetExtractor<T> extractor;
	private final Connection cnx;

	public Query(Connection cnx, String sql, PreparedStatementBinder binder, ResultSetExtractor<T> extractor) {
		assert cnx != null;
		assert sql != null;
		assert extractor != null;

		this.cnx = cnx;
		this.sql = sql;
		this.binder = binder;
		this.extractor = extractor;
	}

	public Query(Connection cnx, String sql, ResultSetExtractor<T> extractor) {
		this(cnx, sql, null, extractor);
	}

	private boolean isPrepared() {
		return binder != null;
	}

	/**
	 * The returned stream must be closed manually by the caller.
	 */
	public Stream<T> stream() {
		try {
			final Statement st = isPrepared() ? cnx.prepareStatement(sql) : cnx.createStatement();
			if (isPrepared()) binder.bind((PreparedStatement) st);
			final ResultSet rs = isPrepared() ? ((PreparedStatement) st).executeQuery() : st.executeQuery(sql);
			final Stream<T> res = StreamSupport
					.stream(Spliterators.spliteratorUnknownSize(extractor.iterator(rs), Spliterator.ORDERED), false);
			res.onClose(() -> FjdbcUtil.close(st));
			return res;
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}

	/**
	 * Convenience method.
	 */
	public <A, R> R collect(Collector<? super T, A, R> collector) {
		try (Stream<T> s = stream()) {
			final R res = s.collect(collector);
			return res;
		}
	}

	/**
	 * Convenience method
	 */
	public List<T> toList() {
		return collect(Collectors.toList());
	}
}

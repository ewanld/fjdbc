package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.util.Consumers;
import com.github.fjdbc.util.FjdbcUtil;

public class PreparedQuery<T> {
	private final String sql;
	private final PreparedStatementBinder binder;
	private final ResultSetExtractor<T> extractor;
	private final Connection cnx;

	public PreparedQuery(Connection cnx, String sql, PreparedStatementBinder binder, ResultSetExtractor<T> extractor) {
		assert cnx != null;
		assert sql != null;
		assert extractor != null;

		this.cnx = cnx;
		this.sql = sql;
		this.binder = binder;
		this.extractor = extractor;
	}

	public PreparedQuery(Connection cnx, String sql, ResultSetExtractor<T> extractor) {
		this(cnx, sql, null, extractor);
	}

	private boolean isPrepared() {
		return binder != null;
	}

	public void forEach(Consumer<? super T> callback) {
		Statement st = null;
		try {
			st = isPrepared() ? cnx.prepareStatement(sql) : cnx.createStatement();
			if (isPrepared()) binder.bind((PreparedStatement) st);
			final ResultSet resultSet = isPrepared() ? ((PreparedStatement) st).executeQuery() : st.executeQuery(sql);
			extractor.extractAll(resultSet, callback);
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			FjdbcUtil.close(st);
		}
	}

	public Stream<T> stream() {
		ResultSet rs = null;
		try (PreparedStatement ps = cnx.prepareStatement(sql)) {
			binder.bind(ps);
			rs = ps.executeQuery();
			final Stream<T> res = StreamSupport
					.stream(Spliterators.spliteratorUnknownSize(extractor.iterator(rs), Spliterator.ORDERED), false);
			res.onClose(() -> FjdbcUtil.close(ps));
			return res;
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		}
	}

	/**
	 * Convenience method
	 */
	public List<T> toList() {
		final List<T> res = new ArrayList<T>();
		forEach(Consumers.toList(res));
		return res;
	}
}

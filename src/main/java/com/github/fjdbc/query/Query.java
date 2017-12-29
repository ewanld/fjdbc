package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.RuntimeSQLException;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.util.FjdbcUtil;
import com.github.fjdbc.util.IntSequence;

public class Query<T> {
	private final String sql;
	private final PreparedStatementBinder binder;
	private final ResultSetExtractor<T> extractor;
	private ConnectionProvider cnxProvider;
	private Integer fetchSize;

	public Query(ConnectionProvider provider, String sql, PreparedStatementBinder binder,
			ResultSetExtractor<T> extractor) {
		assert provider != null;
		assert sql != null;
		assert extractor != null;

		this.cnxProvider = provider;
		this.sql = sql;
		this.binder = binder;
		this.extractor = extractor;
	}

	public Query(ConnectionProvider connectionProvider, String sql, ResultSetExtractor<T> extractor) {
		this(connectionProvider, sql, null, extractor);
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	private boolean isPrepared() {
		return binder != null;
	}

	public void forEach(Consumer<? super T> callback) {
		Statement st = null;
		try {
			final Connection cnx = cnxProvider.borrow();
			st = isPrepared() ? cnx.prepareStatement(sql) : cnx.createStatement();
			if (isPrepared()) binder.bind((PreparedStatement) st, new IntSequence(1));
			if (fetchSize != null) st.setFetchSize(fetchSize);
			final ResultSet rs = isPrepared() ? ((PreparedStatement) st).executeQuery() : st.executeQuery(sql);
			extractor.extractAll(rs, callback);
		} catch (final SQLException e) {
			throw new RuntimeSQLException(e);
		} finally {
			FjdbcUtil.close(st);
			cnxProvider.giveBack();
		}
	}

	/**
	 * Convenience method
	 */
	public List<T> toList() {
		final List<T> res = new ArrayList<>();
		forEach(res::add);
		return res;
	}

	public T toSingleResult() {
		final List<T> res = new ArrayList<>(1);
		forEach(res::add);
		assert res.size() <= 1;
		return res.size() == 1 ? res.get(0) : null;
	}

	public String getSql() {
		return sql;
	}
}

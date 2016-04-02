package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.util.Consumers;
import com.github.fjdbc.util.FjdbcUtil;

public class Query<T> {
	private final String sql;
	private final ResultSetExtractor<T> extractor;
	private final Connection cnx;

	public Query(Connection cnx, String sql, ResultSetExtractor<T> extractor) {
		assert cnx != null;
		assert sql != null;
		assert extractor != null;
		
		this.cnx = cnx;
		this.sql = sql;
		this.extractor = extractor;
	}

	public void forEach(Consumer<T> callback) {
		ResultSet rs = null;
		Statement st = null;
		try {
			st = cnx.createStatement();
			rs = st.executeQuery(sql);
			extractor.extractAll(rs, callback);
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			FjdbcUtil.close(st);
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

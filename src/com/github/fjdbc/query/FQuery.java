package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.util.Consumers;
import com.github.fjdbc.util.FjdbcUtil;

public class FQuery<T> {
	private final String sql;
	private final ResultSetExtractor<T> extractor;
	private final Supplier<Connection> cnxSupplier;

	public FQuery(Supplier<Connection> cnxSupplier, String sql, ResultSetExtractor<T> extractor) {
		assert cnxSupplier != null;
		assert sql != null;
		assert extractor != null;
		
		this.cnxSupplier = cnxSupplier;
		this.sql = sql;
		this.extractor = extractor;
	}

	public void forEach(Consumer<? super T> callback) {
		ResultSet rs = null;
		Statement st = null;
		Connection cnx = null;
		try {
			cnx = cnxSupplier.get();
			st = cnx.createStatement();
			rs = st.executeQuery(sql);
			extractor.extractAll(rs, callback);
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			FjdbcUtil.close(cnx, st, rs);
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

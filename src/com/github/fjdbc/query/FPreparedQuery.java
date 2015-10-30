package com.github.fjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.github.fjdbc.FjdbcException;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.util.Consumers;
import com.github.fjdbc.util.FjdbcUtil;

public class FPreparedQuery<T> {
	private final String sql;
	private final PreparedStatementBinder binder;
	private final ResultSetExtractor<T> extractor;
	private final Supplier<Connection> cnxSupplier;

	public FPreparedQuery(Supplier<Connection> cnxSupplier, String sql, PreparedStatementBinder binder, ResultSetExtractor<T> extractor) {
		assert cnxSupplier != null;
		assert sql != null;
		assert binder != null;
		assert extractor != null;
		
		this.cnxSupplier = cnxSupplier;
		this.sql = sql;
		this.binder = binder;
		this.extractor = extractor;
	}

	public void forEach(Consumer<? super T> callback) {
		PreparedStatement ps = null;
		Connection cnx = null;
		try {
			cnx = cnxSupplier.get();
			ps = cnx.prepareStatement(sql);
			binder.bind(ps);
			final ResultSet resultSet = ps.executeQuery();
			extractor.extractAll(resultSet, callback);
		} catch (final SQLException e) {
			throw new FjdbcException(e);
		} finally {
			FjdbcUtil.close(cnx, ps);
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

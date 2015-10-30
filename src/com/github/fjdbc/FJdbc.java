package com.github.fjdbc;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import com.github.fjdbc.op.CompositeOp;
import com.github.fjdbc.op.FPreparedStatement;
import com.github.fjdbc.op.FStatement;
import com.github.fjdbc.op.Op;
import com.github.fjdbc.query.FPreparedQuery;
import com.github.fjdbc.query.FQuery;
import com.github.fjdbc.query.ResultSetExtractor;

public class FJdbc {
	private final Supplier<Connection> cnxSupplier;

	public FJdbc(Supplier<Connection> cnxSupplier) {
		this.cnxSupplier = cnxSupplier;
	}
	
	public <T> FQuery<T> query(String sql, ResultSetExtractor<T> extractor) {
		return new FQuery<T>(cnxSupplier, sql, extractor);
	}
	
	public <T> FPreparedQuery<T> query(String sql, PreparedStatementBinder binder, ResultSetExtractor<T> extractor) {
		return new FPreparedQuery<T>(cnxSupplier, sql, binder, extractor);
	}
	
	public int exec(String sql) {
		return new FStatement(sql).executeAndCommit(cnxSupplier);
	}
	
	public int exec(String sql, IPreparedStatementBinder binder) {
		return new FPreparedStatement(sql, binder).executeAndCommit(cnxSupplier);
	}
	
	public int exec(Collection<Op> statements) {
		return new CompositeOp(statements).executeAndCommit(cnxSupplier);
	}
	
	public int exec(Op... statement) {
		return exec(Arrays.asList(statement));
	}
}

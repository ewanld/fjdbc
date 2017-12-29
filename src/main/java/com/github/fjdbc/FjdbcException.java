package com.github.fjdbc;

import java.sql.SQLException;

/**
 * Root class of all runtime exceptions thrown by fjdbc.
 */
public class FjdbcException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final SQLException exception;

	public FjdbcException(String message, SQLException exception) {
		super(message, exception);
		this.exception = exception;
	}

	public FjdbcException(SQLException exception) {
		super(exception);
		this.exception = exception;
	}

	public SQLException get() {
		return exception;
	}

}

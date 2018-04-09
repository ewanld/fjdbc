package com.github.fjdbc;

import java.sql.SQLException;

/**
 * Unchecked {@link SQLException}.
 * <p>
 * The wrapped exception can be accessed using the {@link #get()} method.
 */
public class RuntimeSQLException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final SQLException exception;

	public RuntimeSQLException(String message, SQLException exception) {
		super(message, exception);
		this.exception = exception;
	}

	public RuntimeSQLException(SQLException exception) {
		super(exception);
		this.exception = exception;
	}

	/**
	 * Get the wrapped {@link SQLException}.
	 */
	public SQLException get() {
		return exception;
	}
}

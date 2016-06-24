package com.github.fjdbc;

public class FjdbcException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FjdbcException(String message) {
		super(message);
	}

	public FjdbcException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FjdbcException(Throwable throwable) {
		super(throwable);
	}
}

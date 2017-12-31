package com.github.fjdbc.op;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * An operation that does nothing.
 * <p>
 * This can be useful as a return value for methods that must return a {@link DbOperation}, but when no processing is
 * required.
 */
public class NoOperation implements DbOperation {

	@Override
	public int execute(Connection connection) throws SQLException {
		return 0;
	}

	@Override
	public int executeAndCommit() {
		return 0;
	}

}

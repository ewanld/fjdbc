package com.github.fjdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Merge a sequence of {@link PreparedStatementBinder} instances as a single {@link PreparedStatementBinder}.
 */
public class CompositePreparedStatementBinder implements PreparedStatementBinder {
	private final Collection<? extends PreparedStatementBinder> children;

	public CompositePreparedStatementBinder(Collection<? extends PreparedStatementBinder> children) {
		this.children = children;
	}

	@Override
	public void bind(PreparedStatement st, IntSequence paramIndex) throws SQLException {
		for (final PreparedStatementBinder child : children) {
			child.bind(st, paramIndex);
		}
	}
}

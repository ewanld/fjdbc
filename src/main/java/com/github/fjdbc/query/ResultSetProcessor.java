package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A {@code ResultSet} processor. Use this interface when the {@link ResultSetExtractor} abstraction does not fit, i.e
 * when no object is extracted from the {@code ResultSet} but a custom processing is done instead.
 */
@FunctionalInterface
public interface ResultSetProcessor extends ResultSetExtractor<Void> {

	void process(ResultSet rs) throws SQLException;

	@Override
	default Void extract(ResultSet rs) throws SQLException {
		process(rs);
		// null means "stop processing". 
		return null;
	}

	@Override
	default boolean autoCallNext() {
		return false;
	}

}

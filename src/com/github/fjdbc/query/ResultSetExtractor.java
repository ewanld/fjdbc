package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Extract objects from a ResultSet. The object may map to one or many rows from the result set.
 */
public interface ResultSetExtractor<T> {
	void extractAll(ResultSet rs, Consumer<? super T> callback) throws SQLException;
}
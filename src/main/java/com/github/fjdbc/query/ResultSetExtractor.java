package com.github.fjdbc.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Extract objects of type {@code T} from a ResultSet.
 * <p>
 * Each object may map to one or many rows from the result set.
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {
	/**
	 * Calls the specified callback everytime an object is extracted from the {@link ResultSet}.
	 * <p>
	 * It is the responsibility of implementors to call {@link ResultSet#next()} to loop through the ResultSet.
	 */
	void extractAll(ResultSet rs, Consumer<? super T> callback) throws SQLException;
}
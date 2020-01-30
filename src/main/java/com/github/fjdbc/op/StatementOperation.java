package com.github.fjdbc.op;

import java.sql.SQLException;
import java.sql.Statement;

import com.github.fjdbc.RuntimeSQLException;
import com.github.fjdbc.SQLConsumer;

public interface StatementOperation extends DbOperation {

	/**
	 * User-defined code to be executed after the {@link Statement} is created, but before it is executed.
	 * <p>
	 * This can be useful for setting Statement options, such as {@link Statement#setFetchSize(int)} for instance.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 */
	StatementOperation doBeforeExecution(SQLConsumer<Statement> statementConsumer);

	/**
	 * User-defined code to be executed after the {@link Statement} is executed.
	 * <p>
	 * If the specified consumer throws a {@link SQLException}, it will be wrapped in an unchecked
	 * {@link RuntimeSQLException}.
	 */
	StatementOperation doAfterExecution(SQLConsumer<Statement> statementConsumer);

}
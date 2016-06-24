# fjdbc
Functional wrapper for the JDBC API.

Requires Java >= 8.

## Examples
### Query the database
```java
final String sql = "select name from user";
final ResultSetExtractor<String> extractor = (rs) -> rs.getString(1);
final List<String> names = new PreparedQuery<>(connection, sql, extractor).toList();
System.out.println(names);
```

### Query the database using a prepared statement
```java
final String sql = "select name from user where role = ?";
final PreparedStatementBinder binder = (ps) -> ps.setString(1, "grunt");
final SingleRowExtractor<String> extractor = rs -> rs.getString(1);
final List<String> names = PreparedQuery<>(connection, sql, binder, extractor).toList();
System.out.println(names);
```

### Execute a statement (update, delete, insert, etc)
```java
final String sql = "update user set name='jack' where name='henri'";
final int nRows = new StatementOp(sql).executeAndCommit(connection);
System.out.println(nRows + " rows changed");
```

### Execute a prepared statement
```java
final String sql = "update user set name=? where name=?";
final PreparedStatementBinder binder = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "jack");
	ps.setString(paramIndex.next(), "henri");
};
final int nRows = new PreparedStatementOp(sql, binder).executeAndCommit(connection);
System.out.println(nRows + " rows changed");
```

### Execute a sequence of statements (in a single transaction)
```java
final PreparedStatementBinder binder = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "jack");
	ps.setString(paramIndex.next(), "henri");
};
final Op updateName = new PreparedStatementOp("update user set name=? where name=?", binder);

final PreparedStatementBinder binder2 = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "manager");
};
final Op deleteManagers = new PreparedStatementOp("delete from user where role=?", binder2);

final int nRows = new CompositeOp(updateName, deleteManagers).executeAndCommit(connection);
System.out.println(nRows + " rows changed");
```

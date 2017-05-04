# fjdbc
Functional wrapper for the JDBC API.

Requires Java >= 8.

## Examples
### Query the database
```java
final String sql = "select name from user";
final ResultSetExtractor<String> extractor = (rs) -> rs.getString(1);
final List<String> names = new Query<>(connection, sql, extractor).toList();
System.out.println(names);
```

### Query the database using a prepared statement
```java
final String sql = "select name from user where role = ?";
final PreparedStatementBinder binder = (ps) -> ps.setString(1, "grunt");
final SingleRowExtractor<String> extractor = rs -> rs.getString(1);
final List<String> names = Query<>(connection, sql, binder, extractor).toList();
System.out.println(names);
```

### Execute a statement (update, delete, insert, etc)
```java
final String sql = "update user set name='jack' where name='henri'";
final int nRows = new StatementOperation(sql).executeAndCommit(connection);
System.out.println(nRows + " rows changed");
```

### Execute a prepared statement
```java
final String sql = "update user set name=? where name=?";
final PreparedStatementBinder binder = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "jack");
	ps.setString(paramIndex.next(), "henri");
};
final int nRows = new StatementOperation(sql, binder).executeAndCommit(connection);
System.out.println(nRows + " rows changed");
```

### Execute a sequence of statements (in a single transaction)
```java
final PreparedStatementBinder binder = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "jack");
	ps.setString(paramIndex.next(), "henri");
};
final DbOperation updateName = new StatementOperation("update user set name=? where name=?", binder);

final PreparedStatementBinder binder2 = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "manager");
};
final DbOperation deleteManagers = new StatementOperation("delete from user where role=?", binder2);

final int nRows = new CompositeOperation(updateName, deleteManagers).executeAndCommit(connection);
System.out.println(nRows + " rows changed");
```

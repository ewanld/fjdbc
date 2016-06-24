# fjdbc
Functional wrapper for the JDBC API.

Requires Java >= 8.

## Examples
### Query the database
```java
final ResultSetExtractor<String> extractor = (rs) -> rs.getString(1);
final List<String> names = new PreparedQuery<>("select name from user", extractor).toList();
System.out.println(names);
```

### Query the database using a prepared statement
```java
final String sql = "select name from user where role = ?";
final PreparedStatementBinder binder = (ps) -> ps.setString(1, "grunt");
final SingleRowExtractor<String> extractor = rs -> rs.getString(1);
final List<String> names = fjdbc.query(sql, binder, extractor).toList();
System.out.println(names);
```

### Execute a statement (update, delete, insert, etc)
```java
final int nRows = fjdbc.exec("update user set name='jack' where name='henri'");
System.out.println(nRows + " rows changed");
```

### Execute a prepared statement
```java
final PreparedStatementBinder binder = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "jack");
	ps.setString(paramIndex.next(), "henri");
};
final int nRows = fjdbc.exec("update user set name=? where name=?", binder);
System.out.println(nRows + " rows changed");
```

### Execute a sequence of statements (in a single transaction)
```java
final PreparedStatementBinder binder = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "jack");
	ps.setString(paramIndex.next(), "henri");
};
final Op updateName = new FPreparedStatement("update user set name=? where name=?", binder);

final PreparedStatementBinder binder2 = (ps, paramIndex) -> {
	ps.setString(paramIndex.next(), "manager");
};
final Op deleteManagers = new FPreparedStatement("delete from user where role=?", binder2);

final int nRows = fjdbc.exec(updateName, deleteManagers);
System.out.println(nRows + " rows changed");
```

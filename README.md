# fjdbc
Functional wrapper for JPDBC API.

Requires Java >= 8.

## Examples
### Initialize the FJdbc facade
```java
final Supplier<Connection> cnxSupplier = () -> {
	try {
		return DriverManager.getConnection("jdbc-url", "user", "password");
	} catch (final Exception e) {
		throw new RuntimeException(e);
	}
};
final FJdbc fjdbc = new FJdbc(cnxSupplier);
```

### Query the database
```java
final SingleRowExtractor<String> extractor = (rs) -> rs.getString(1);
final List<String> names = fjdbc.query("select name from user", extractor).toList();
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
System.out.println(nRows);
```

### Execute a prepared statement
```java
final PreparedStatementBinder binder = (ps) -> {
	ps.setString(1, "jack");
	ps.setString(2, "henri");
};
final int nRows = fjdbc.exec("update user set name=? where name=?", binder);
System.out.println(nRows);
```

### Execute a sequence of statements (in a single transaction)
```java
final PreparedStatementBinder binder = (ps) -> {
	ps.setString(1, "jack");
	ps.setString(2, "henri");
};
final Op updateName = new FPreparedStatement("update user set name=? where name=?", binder);

final PreparedStatementBinder binder2 = (ps) -> {
ps.setString(1, "manager");
};
final Op deleteManagers = new FPreparedStatement("delete from user where role=?", binder2);

final int nRows = fjdbc.exec(updateName, deleteManagers);
```

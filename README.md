# fjdbc
Functional wrapper for the JDBC API.

Requires Java >= 8.

## Examples
### Setup
```java
final Connection connection = DriverManager.getConnection("jdbc/url/to/database");
final SingleConnectionProvider cnxProvider = new SingleConnectionProvider(connection);
final Fjdbc fjdbc = new Fjdbc(cnxProvider);
```

### Query the database
```java
final String sql = "select name from user";
final SingleRowExtractor<String> extractor = (rs) -> rs.getString("name");
final List<String> names = fjdbc.query(sql, extractor).toList();
```

### Query the database using a prepared statement
```java
final String sql = "select name from user where role = ?";
final PreparedStatementBinder binder = (ps, seq) -> ps.setString(seq.next(), "grunt");
final SingleRowExtractor<String> extractor = rs -> rs.getString("name");
final List<String> names = fjdbc.query(sql, binder, extractor).toList();
```

### Execute a statement (update, delete, insert, etc)
```java
final String sql = "update user set name='jack' where name='henri'";
final int nRows = fjdbc.statement(sql).executeAndCommit();
```

### Execute a prepared statement
```java
final String sql = "update user set name=? where name=?";
final PreparedStatementBinder binder = (ps, seq) -> {
	ps.setString(seq.next(), "jack");
	ps.setString(seq.next(), "henri");
};
final int nRows = fjdbc.statement(sql, binder).executeAndCommit();
```

### Execute a sequence of statements (in a single transaction)
```java
final PreparedStatementBinder binder = (ps, seq) -> {
	ps.setString(seq.next(), "jack");
	ps.setString(seq.next(), "henri");
};
final DbOperation updateName = fjdbc.statement("update user set name=? where name=?", binder);

final PreparedStatementBinder binder2 = (ps, seq) -> {
	ps.setString(seq.next(), "manager");
};
final DbOperation deleteManagers = fjdbc.statement("delete from user where role=?", binder2);

final int nRows = fjdbc.composite(updateName, deleteManagers).executeAndCommit();
```

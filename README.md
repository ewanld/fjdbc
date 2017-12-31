# fjdbc: functional wrapper for the JDBC API

fjdbc is a lightweight library to write JDBC code in a more convenient and functional style.

fjdbc is built around a small number of abstractions:
* ```ConnectionProvider``` is responsible for getting ```java.sql.Connection``` instances.
* ```DbOperation``` represents either a single ```java.sql.Statement```, or a sequence of Statements to be executed in a single transaction.
* ```ResultSetExtractor``` is a functional interface to extract objects from a ```java.sql.ResultSet```.
* ```PreparedStatementBinder ``` is a functional interface to bind the parameters of a ```java.sql.PreparedStatement```.
* ```Query``` represents a SQL SELECT statement.
* ```Fjdbc``` is the library façade.
* ```SQLException``` instances are wrapped in an unchecked ```RuntimeSQLException```.

Requires Java >= 8.
Latest version: 0.1.0.

## Examples
### Setup
```java
final Connection connection = DriverManager.getConnection("jdbc/url/to/database");
final ConnectionProvider cnxProvider = new SingleConnectionProvider(connection);
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
final SingleRowExtractor<String> extractor = rs -> rs.getString("name");
final List<String> names = fjdbc
	.query(sql, extractor)
	.setBinder((ps, seq) -> ps.setString(seq.next(), "grunt"))
	.toList();
```

### Execute a statement (update, delete, insert, etc)
```java
final String sql = "update user set name='jack' where name='henri'";
final int nRows = fjdbc.statement(sql).executeAndCommit();
```

### Execute a prepared statement
```java
final String sql = "update user set name=? where name=?";
final int nRows = fjdbc
	.statement(sql)
	.setBinder((ps, seq) -> {
		ps.setString(seq.next(), "jack");
		ps.setString(seq.next(), "henri");
	})
	.executeAndCommit();
```

### Execute a sequence of statements (in a single transaction)
```java
final StatementOperation updateName = fjdbc
	.statement("update user set name=? where name=?")
	.setBinder((ps, seq) -> {
		ps.setString(seq.next(), "jack");
		ps.setString(seq.next(), "henri");
	});

final StatementOperation deleteManagers = fjdbc
	.statement("delete from user where role=?")
	.setBinder((ps, seq) -> {
		ps.setString(seq.next(), "manager");
	});

final int nRows = fjdbc.composite(updateName, deleteManagers).executeAndCommit();
```

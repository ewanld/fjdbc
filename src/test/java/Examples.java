import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.Fjdbc;
import com.github.fjdbc.connection.SingleConnectionProvider;
import com.github.fjdbc.op.StatementOperation;
import com.github.fjdbc.query.SingleRowExtractor;

public class Examples {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws SQLException {
		// Setup
		final Connection connection = DriverManager.getConnection("jdbc/url/to/database");
		final ConnectionProvider cnxProvider = new SingleConnectionProvider(connection);
		final Fjdbc fjdbc = new Fjdbc(cnxProvider);

		// -------------------------------------------------------------------------------------------------------------
		// Query the database
		{
			final String sql = "select name from user";
			final SingleRowExtractor<String> extractor = (rs) -> rs.getString("name");
			final List<String> names = fjdbc.query(sql, extractor).toList();
		}

		// -------------------------------------------------------------------------------------------------------------
		// Query the database using a prepared statement
		{
			// @formatter:off
			final String sql = "select name from user where role = ?";
			final SingleRowExtractor<String> extractor = rs -> rs.getString("name");
			final List<String> names = fjdbc
				.query(sql, extractor)
				.setBinder((ps, seq) -> ps.setString(seq.next(), "grunt"))
				.toList();
			// @formatter:on
		}

		// -------------------------------------------------------------------------------------------------------------
		// Execute a statement (update, delete, insert, etc)
		{
			final String sql = "update user set name='jack' where name='henri'";
			final int nRows = fjdbc.statement(sql).executeAndCommit();
		}

		// -------------------------------------------------------------------------------------------------------------
		// Execute a prepared statement
		{
			// @formatter:off
			final String sql = "update user set name=? where name=?";
			final int nRows = fjdbc
				.statement(sql)
				.setBinder((ps, seq) -> {
					ps.setString(seq.next(), "jack");
					ps.setString(seq.next(), "henri");
				})
				.executeAndCommit();
			// @formatter:on
		}

		// -------------------------------------------------------------------------------------------------------------
		// Execute a sequence of statements (in a single transaction)
		{
			// @formatter:off
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
			// @formatter:on
		}
	}
}

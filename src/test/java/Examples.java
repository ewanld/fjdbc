import java.util.List;

import com.github.fjdbc.ConnectionProvider;
import com.github.fjdbc.Fjdbc;
import com.github.fjdbc.PreparedStatementBinder;
import com.github.fjdbc.op.DbOperation;
import com.github.fjdbc.query.ResultSetExtractor;

public class Examples {
	public static void main(String[] args) {
		final ConnectionProvider cnxProvider = null;
		final Fjdbc fjdbc = new Fjdbc(cnxProvider);

		// -------------------------------------------------------------------------------------------------------------
		// Query the database
		{
			final String sql = "select name from user";
			final ResultSetExtractor<String> extractor = (rs) -> rs.getString("name");
			final List<String> names = fjdbc.query(sql, extractor).toList();
			System.out.println(names);
		}

		// -------------------------------------------------------------------------------------------------------------
		// Query the database using a prepared statement
		{
			final String sql = "select name from user where role = ?";
			final PreparedStatementBinder binder = (ps, seq) -> ps.setString(seq.next(), "grunt");
			final ResultSetExtractor<String> extractor = rs -> rs.getString("name");
			final List<String> names = fjdbc.query(sql, binder, extractor).toList();
			System.out.println(names);
		}

		// -------------------------------------------------------------------------------------------------------------
		// Execute a statement (update, delete, insert, etc)
		{
			final String sql = "update user set name='jack' where name='henri'";
			final int nRows = fjdbc.statement(sql).executeAndCommit(cnxProvider);
			System.out.println(nRows + " rows changed");
		}

		// -------------------------------------------------------------------------------------------------------------
		// Execute a prepared statement
		{
			final String sql = "update user set name=? where name=?";
			final PreparedStatementBinder binder = (ps, paramIndex) -> {
				ps.setString(paramIndex.next(), "jack");
				ps.setString(paramIndex.next(), "henri");
			};
			final int nRows = fjdbc.statement(sql, binder).executeAndCommit(cnxProvider);
			System.out.println(nRows + " rows changed");
		}

		// -------------------------------------------------------------------------------------------------------------
		// Execute a sequence of statements (in a single transaction)
		{
			final PreparedStatementBinder binder = (ps, paramIndex) -> {
				ps.setString(paramIndex.next(), "jack");
				ps.setString(paramIndex.next(), "henri");
			};
			final DbOperation updateName = fjdbc.statement("update user set name=? where name=?", binder);

			final PreparedStatementBinder binder2 = (ps, paramIndex) -> {
				ps.setString(paramIndex.next(), "manager");
			};
			final DbOperation deleteManagers = fjdbc.statement("delete from user where role=?", binder2);

			final int nRows = fjdbc.composite(updateName, deleteManagers).executeAndCommit(cnxProvider);
			System.out.println(nRows + " rows changed");
		}
	}
}

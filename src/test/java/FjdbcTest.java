import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.io.FileUtils;

import com.github.fjdbc.Fjdbc;
import com.github.fjdbc.RuntimeSQLException;
import com.github.fjdbc.connection.SingleConnectionProvider;
import com.github.fjdbc.op.NoOperation;
import com.github.fjdbc.op.StatementOperation;
import com.github.fjdbc.query.Query;
import com.github.fjdbc.query.SingleRowExtractor;
import com.github.fjdbc.util.CheckedUtil;

/**
 * This class conforms to the POJO convention of maven surefire.
 * <p>
 * It writes test results in the file FjdbcTest-last.txt, then compares the content with a reference file
 * (FjdbcTest-ref.txt).
 */
public class FjdbcTest {
	private static final File last = new File(
			FjdbcTest.class.getClassLoader().getResource("FjdbcTest-last.txt").getFile());
	private static final File ref = new File(
			FjdbcTest.class.getClassLoader().getResource("FjdbcTest-ref.txt").getFile());
	// the sqlite database fjdbc-test.db has the following schema:
	// create table user(id number(6), name varchar2(100));
	private static final File db = new File(FjdbcTest.class.getClassLoader().getResource("fjdbc-test.db").getFile());
	private final Writer writer;
	private final Fjdbc fjdbc;
	private final Connection conn;

	public FjdbcTest() throws Exception {
		writer = new BufferedWriter(new FileWriter(last));
		final String url = "jdbc:sqlite:" + db.getAbsolutePath();
		conn = DriverManager.getConnection(url);
		final SingleConnectionProvider connProvider = new SingleConnectionProvider(conn);
		fjdbc = new Fjdbc(connProvider);
	}

	/**
	 * delete table 'user', insert data, then query the table to check if the insertion was performed.
	 */
	public void testAll() throws IOException {
		delete();
		insert();
		query();
		queryWithLimit();
		writer.flush();
		assert FileUtils.contentEquals(last, ref);
		delete();
	}

	private void delete() throws IOException {
		fjdbc.statement("delete from user").executeAndCommit();
	}

	private void insert() throws IOException {
		final StatementOperation statement1 = fjdbc.statement("insert into user values(1, 'name1')");
		final StatementOperation statement2 = fjdbc.statement("insert into user values(2, 'name2')");
		final StatementOperation statement3 = fjdbc.statement("insert into user values(?, ?)");
		statement3.setBinder((ps, seq) -> {
			ps.setInt(seq.next(), 3);
			ps.setString(seq.next(), "name3");
		});
		statement1.executeAndCommit();
		fjdbc.composite(statement2, statement3).executeAndCommit();
	}

	private void query() throws IOException {
		final SingleRowExtractor<Integer> extractor = rs -> rs.getInt("id");
		writeQuery(fjdbc.query("select id from user", extractor));
	}

	private void queryWithLimit() throws IOException {
		final SingleRowExtractor<Integer> extractor = rs -> rs.getInt("id");

		final Query<Integer> query = fjdbc.query("select id from user", extractor);
		query.doBeforeExecution(st -> st.setMaxRows(1));
		writeQuery(query);
	}

	public void testInvalidStatement() {
		boolean exception = false;
		try {
			fjdbc.statement("invalid statement *^").executeAndCommit();
		} catch (final RuntimeSQLException e) {
			exception = true;
		}
		assert exception;
	}

	/**
	 * Test the NoOperation class
	 */
	public void testNoOp() {
		new NoOperation().executeAndCommit();
	}

	public <T> void writeQuery(Query<T> query) throws IOException {
		writeln(query.getSql());
		query.forEach(this::writeln);
	}

	public void writeln(Object o) {
		write(o);
		write("\n");
	}

	public void write(Object o) {
		CheckedUtil.quietly(() -> writer.write(o == null ? "null" : o.toString()));
	}

	public void tearDown() throws Exception {
		if (writer != null) writer.close();
		if (conn != null) conn.close();
	}
}

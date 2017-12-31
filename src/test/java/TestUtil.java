
public class TestUtil {
	/**
	 * Run an expression block, wrapping all exceptions into a RuntimeException.
	 */
	public static void quietly(CheckedRunnable checkedRunnable) {
		try {
			checkedRunnable.run();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}

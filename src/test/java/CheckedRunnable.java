

@FunctionalInterface
public interface CheckedRunnable {
	void run() throws Exception;

	public static Runnable uncheck(CheckedRunnable checkedRunnable) {
		return () -> {
			try {
				checkedRunnable.run();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}

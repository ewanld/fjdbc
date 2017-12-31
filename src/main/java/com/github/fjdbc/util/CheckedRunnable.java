package com.github.fjdbc.util;

@FunctionalInterface
public interface CheckedRunnable {
	void run() throws Exception;

	public default Runnable uncheck() {
		return () -> {
			try {
				run();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}

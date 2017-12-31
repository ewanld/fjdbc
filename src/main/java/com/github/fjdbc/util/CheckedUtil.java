package com.github.fjdbc.util;

public class CheckedUtil {
	/**
	 * Run an expression block, wrapping all exceptions into a RuntimeException.
	 */
	public static void quietly(CheckedRunnable checkedRunnable) {
		checkedRunnable.uncheck().run();
	}
}

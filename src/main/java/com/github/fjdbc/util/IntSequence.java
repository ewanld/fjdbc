package com.github.fjdbc.util;

public class IntSequence {
	private int counter;
	private final int startValue;

	public IntSequence(int startValue) {
		this.startValue = startValue;
		counter = startValue;
	}

	public int next() {
		return counter++;
	}

	public void reset() {
		counter = startValue;
	}

	public int get() {
		return counter;
	}
}
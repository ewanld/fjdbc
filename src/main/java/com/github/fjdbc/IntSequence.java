package com.github.fjdbc;

public class IntSequence {
	private int counter;
	private final int startValue;

	public IntSequence(int startValue) {
		this.startValue = startValue;
		reset();
	}

	public int next() {
		return ++counter;
	}

	public void reset() {
		counter = startValue - 1;
	}

	public int get() {
		return counter;
	}
}
package com.github.fjdbc.util;

public class IntSequence {
	private int counter;

	public IntSequence(int startValue) {
		counter = startValue;
	}

	public int next() {
		return counter++;
	}
}
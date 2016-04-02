package com.github.fjdbc;

public class Sequence {
	private int counter;

	public Sequence(int startValue) {
		counter = startValue;
	}

	public int nextValue() {
		return counter++;
	}
}
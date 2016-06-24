package com.github.fjdbc.util;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Collection of static consumers.
 */
public class Consumers {
	private Consumers() {
	}

	private static class ToList<T> implements Consumer<T> {
		private final Collection<T> collection;

		public ToList(Collection<T> list) {
			this.collection = list;
			list.clear();
		}

		@Override
		public void accept(T t) {
			collection.add(t);
		}
	}
	
	public static <T> ToList<T> toList(Collection<T> list) {
		return new ToList<T>(list);
	}

}

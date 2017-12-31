

import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedSupplier<T> {
	T get() throws Exception;

	public static <U> Supplier<U> uncheck(CheckedSupplier<U> checkedSupplier) {
		return () -> {
			try {
				return checkedSupplier.get();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		};
	}
}

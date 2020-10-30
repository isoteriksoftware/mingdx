package com.isoterik.mgdx.ai.fsm;

/**
 * An instance of {@link ICondition} is used to test if a condition is satisfied (evaluates to boolean <em>true</em>).
 * An implementation of this interface is expected to do the necessary calculations to determine if the condition is satisfied or not.
 *
 * @author isoteriksoftware
 * @see Transition
 */
public interface ICondition {
	/**
	 * Checks the status of the condition.
	 * @return {@code true} if the condition is met, {@code false} otherwise
	 */
	boolean test();

	/**
	 * A data source is used by conditions to reference the data they work with. This allows easier modification at runtime.
	 * @param <T> the data type
	 */
	class DataSource<T> {
		public T data;

		public DataSource(T data) {
			this.data = data;
		}
	}
}

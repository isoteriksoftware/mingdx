package com.isoterik.mgdx.ai.fsm.conditions;

public class NotEqualCondition<T> extends EqualsCondition<T> {
	/**
	 * Creates a new instance given the data sources.
	 * @param firstDataSource the first value source
	 * @param secondDataSource the second value source
	 * @param identity if {@code true} == will be used for comparison else {@link Object#equals(Object)} will be used
	 */
	public NotEqualCondition(DataSource<T> firstDataSource, DataSource<T> secondDataSource,
						   boolean identity) {
		super(firstDataSource, secondDataSource, identity);
	}

	/**
	 * Creates a new instance given the data sources. Will use == for comparison by default.
	 * @param firstDataSource the first value source
	 * @param secondDataSource the second value source
	 */
	public NotEqualCondition(DataSource<T> firstDataSource, DataSource<T> secondDataSource) {
		super(firstDataSource, secondDataSource);
	}

	/**
	 * Creates a new instance given the two values to compare.
	 * @param first the first value
	 * @param second the second value
	 * @param identity if {@code true} != will be used for comparison else {@link Object#equals(Object)} will be used
	 */
	public NotEqualCondition(T first, T second, boolean identity) {
		super(first, second, identity);
	}

	/**
	 * Creates a new instance given the two values to compare. Will use != for comparison by default
	 * @param first the first value
	 * @param second the second value
	 */
	public NotEqualCondition(T first, T second) {
		super(first, second);
	}

	/**
	 * Sets the first value.
	 * @param first the value
	 * @return this instance for chaining
	 */
	public NotEqualCondition<T> setFirst(T first) {
		super.setFirst(first);
		return this;
	}

	/**
	 * Sets the second value
	 * @param second the value
	 * @return this instance for chaining
	 */
	public NotEqualCondition<T> setSecond(T second) {
		super.setSecond(second);
		return this;
	}

	/**
	 * Determines how the comparison should me made
	 * @param identity if {@code true} == will be used for comparison else {@link Object#equals(Object)} will be used
	 * @return this instance for chaining
	 */
	public NotEqualCondition<T> setIdentity(boolean identity) {
		super.setIdentity(identity);
		return this;
	}

	/**
	 * Sets the data source for the first value.
	 * @param firstDataSource the data source for the first value.
	 * @return this instance for chaining.
	 */
	public NotEqualCondition<T> setFirstDataSource(DataSource<T> firstDataSource) {
		this.firstDataSource = firstDataSource;
		return this;
	}

	/**
	 * Sets the data source for the second value.
	 * @param secondDataSource the data source for the second value.
	 * @return this instance for chaining.
	 */
	public NotEqualCondition<T> setSecondDataSource(DataSource<T> secondDataSource) {
		this.secondDataSource = secondDataSource;
		return this;
	}

	/**
	 *
	 * @return {@code true} if both values are not equal, {@code false} otherwise
	 */
	@Override
	public boolean test() {
		return !super.test();
	}
}

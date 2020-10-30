package com.isoterik.mgdx.ai.fsm.conditions;

/**
 * A condition that compares two given value and evaluates to {@code true} if the first value is less than the second.
 * @param <T> the type of number to compare. Currently supports precision to as high as {@link Float} precision.
 *
 * @author isoteriksoftware
 */
public class LessThanCondition<T extends Number> extends GreaterThanCondition<T> {
	/**
	 * Creates a new instance given the data sources.
	 * @param firstDataSource the first value source
	 * @param secondDataSource the second value source
	 */
	public LessThanCondition(DataSource<T> firstDataSource, DataSource<T> secondDataSource) {
		super(firstDataSource, secondDataSource);
	}

	/**
	 * Creates an instance given the two values to compare
	 * @param first the first value
	 * @param second the second value
	 */
	public LessThanCondition(T first, T second) {
		super(first, second);
	}

	/**
	 * Sets the first value.
	 * @param first the value
	 * @return this instance for chaining
	 */
	public LessThanCondition<T> setFirst(T first) {
		super.setFirst(first);
		return this;
	}

	/**
	 * Sets the second value
	 * @param second the value
	 * @return this instance for chaining
	 */
	public LessThanCondition<T> setSecond(T second) {
		super.setSecond(second);
		return this;
	}

	/**
	 * Sets the data source for the first value.
	 * @param firstDataSource the data source for the first value.
	 * @return this instance for chaining.
	 */
	public LessThanCondition<T> setFirstDataSource(DataSource<T> firstDataSource) {
		this.firstDataSource = firstDataSource;
		return this;
	}

	/**
	 * Sets the data source for the second value.
	 * @param secondDataSource the data source for the second value.
	 * @return this instance for chaining.
	 */
	public LessThanCondition<T> setSecondDataSource(DataSource<T> secondDataSource) {
		this.secondDataSource = secondDataSource;
		return this;
	}

	/**
	 *
	 * @return {@code true} if the first value is greater than the second value, {@code false} otherwise
	 */
	@Override
	public boolean test() {
		if (firstDataSource.data instanceof Integer) {
			return firstDataSource.data.intValue() < secondDataSource.data.intValue();
		}
		else {
			return firstDataSource.data.floatValue() < secondDataSource.data.floatValue();
		}
	}
}

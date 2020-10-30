package com.isoterik.mgdx.ai.fsm.conditions;

import com.isoterik.mgdx.ai.fsm.ICondition;

/**
 * A condition that compares two given value and evaluates to {@code true} if the first value is greater than the second.
 * @param <T> the type of number to compare. Currently supports precision to as high as {@link Float} precision.
 *
 * @author isoteriksoftware
 */
public class GreaterThanCondition<T extends Number> implements ICondition {
	protected DataSource<T> firstDataSource;
	protected DataSource<T> secondDataSource;

	/**
	 * Creates a new instance given the data sources.
	 * @param firstDataSource the first value source
	 * @param secondDataSource the second value source
	 */
	public GreaterThanCondition(DataSource<T> firstDataSource, DataSource<T> secondDataSource) {
		this.firstDataSource = firstDataSource;
		this.secondDataSource = secondDataSource;
	}

	/**
	 * Creates an instance given the two values to compare
	 * @param first the first value
	 * @param second the second value
	 */
	public GreaterThanCondition(T first, T second) {
		this(new DataSource<>(first), new DataSource<>(second));
	}

	/**
	 * Sets the first value.
	 * @param first the value
	 * @return this instance for chaining
	 */
	public GreaterThanCondition<T> setFirst(T first) {
		firstDataSource.data = first;
		return this;
	}

	/**
	 *
	 * @return the first value
	 */
	public T getFirst() {
		return firstDataSource.data;
	}

	/**
	 * Sets the second value
	 * @param second the value
	 * @return this instance for chaining
	 */
	public GreaterThanCondition<T> setSecond(T second) {
		secondDataSource.data = second;
		return this;
	}

	/**
	 *
	 * @return the second value
	 */
	public T getSecond() {
		return secondDataSource.data;
	}

	/**
	 * Returns the data source for the first value.
	 * @return the data source for the first value.
	 */
	public DataSource<T> getFirstDataSource() {
		return firstDataSource;
	}

	/**
	 * Returns the data source for the second value.
	 * @return the data source for the second value.
	 */
	public DataSource<T> getSecondDataSource() {
		return secondDataSource;
	}

	/**
	 * Sets the data source for the first value.
	 * @param firstDataSource the data source for the first value.
	 * @return this instance for chaining.
	 */
	public GreaterThanCondition<T> setFirstDataSource(DataSource<T> firstDataSource) {
		this.firstDataSource = firstDataSource;
		return this;
	}

	/**
	 * Sets the data source for the second value.
	 * @param secondDataSource the data source for the second value.
	 * @return this instance for chaining.
	 */
	public GreaterThanCondition<T> setSecondDataSource(DataSource<T> secondDataSource) {
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
			return firstDataSource.data.intValue() > secondDataSource.data.intValue();
		}
		else {
			return firstDataSource.data.floatValue() > secondDataSource.data.floatValue();
		}
	}
}

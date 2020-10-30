package com.isoterik.mgdx.ai.fsm.conditions;

import com.isoterik.mgdx.ai.fsm.ICondition;

/**
 * A condition that compares two given values using equality.
 * @param <T> the type of data that will be compared.
 *
 * @author isoteriksoftware
 */
public class EqualsCondition<T> implements ICondition {
	protected DataSource<T> firstDataSource;
	protected DataSource<T> secondDataSource;

	protected boolean identity;

	/**
	 * Creates a new instance given the two values to compare.
	 * @param first the first value
	 * @param second the second value
	 * @param identity if {@code true} == will be used for comparison else {@link Object#equals(Object)} will be used
	 */
	public EqualsCondition(T first, T second, boolean identity) {
		this(new DataSource<>(first), new DataSource<>(second), identity);
	}

	/**
	 * Creates a new instance given the two values to compare. Will use == for comparison by default.
	 * @param first the first value
	 * @param second the second value
	 */
	public EqualsCondition(T first, T second) {
		this(first, second, true);
	}

	/**
	 * Creates a new instance given the data sources.
	 * @param firstDataSource the first value source
	 * @param secondDataSource the second value source
	 * @param identity if {@code true} == will be used for comparison else {@link Object#equals(Object)} will be used
	 */
	public EqualsCondition(DataSource<T> firstDataSource, DataSource<T> secondDataSource,
						   boolean identity) {
		this.firstDataSource = firstDataSource;
		this.secondDataSource = secondDataSource;
		this.identity = identity;
	}

	/**
	 * Creates a new instance given the data sources. Will use == for comparison by default.
	 * @param firstDataSource the first value source
	 * @param secondDataSource the second value source
	 */
	public EqualsCondition(DataSource<T> firstDataSource, DataSource<T> secondDataSource) {
		this(firstDataSource, secondDataSource, true);
	}

	/**
	 * Sets the first value.
	 * @param first the value
	 * @return this instance for chaining
	 */
	public EqualsCondition<T> setFirst(T first) {
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
	public EqualsCondition<T> setSecond(T second) {
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
	public EqualsCondition<T> setFirstDataSource(DataSource<T> firstDataSource) {
		this.firstDataSource = firstDataSource;
		return this;
	}

	/**
	 * Sets the data source for the second value.
	 * @param secondDataSource the data source for the second value.
	 * @return this instance for chaining.
	 */
	public EqualsCondition<T> setSecondDataSource(DataSource<T> secondDataSource) {
		this.secondDataSource = secondDataSource;
		return this;
	}

	/**
	 * Determines how the comparison should me made
	 * @param identity if {@code true} == will be used for comparison else {@link Object#equals(Object)} will be used
	 * @return this instance for chaining
	 */
	public EqualsCondition<T> setIdentity(boolean identity) {
		this.identity = identity;
		return this;
	}

	/**
	 *
	 * @return {@code true} if == is used for comparison, {@code false} otherwise
	 */
	public boolean isIdentity() {
		return identity;
	}

	/**
	 *
	 * @return {@code true} if both values are equal, {@code false} otherwise
	 */
	@Override
	public boolean test() {
		if (identity)
			return firstDataSource.data == secondDataSource.data;

		return firstDataSource.data.equals(secondDataSource.data);
	}
}

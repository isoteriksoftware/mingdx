package com.isoterik.mgdx.ai.fsm.conditions;

import com.isoterik.mgdx.ai.fsm.ICondition;


/**
 * A condition that delegates to other conditions to create a complex condition. This condition is capable of comparing {@link Number}s directly.
 * This condition initially delegates to {@link EqualsCondition} on construction, subsequent conditions are compounded to yield a final condition.
 * @param <T> the type of number to compare. Currently supports precision to as high as {@link Float} precision.
 *
 * @author isoteriksoftware
 * @see com.isoterik.mgdx.ai.fsm.conditions.CompoundCondition
 */
public class NumericCompoundCondition<T extends Number> extends CompoundCondition {
	protected DataSource<T> dataSource;

	/**
	 * Creates a new instance given an initial value
	 * @param value the initial value
	 */
	public NumericCompoundCondition(T value) {
		super(new EqualsCondition<T>(value, value));
		dataSource = ((EqualsCondition<T>)condition).firstDataSource;
	}

	/**
	 * Sets the current value
	 * @param value the value
	 * @return this instance for chaining
	 */
	public NumericCompoundCondition<T> setValue(T value) {
		dataSource.data = value;
		return this;
	}

	/**
	 *
	 * @return the current value
	 */
	public T getValue() {
		return dataSource.data;
	}

	/**
	 * Delegates to {@link AndCondition} using the current condition and one or more conditions
	 * @param newCondition the new condition
	 * @param newConditions other conditions
	 * @return this instance for chaining
	 * @see AndCondition
	 */
	@Override
	public NumericCompoundCondition<T> and(ICondition newCondition, ICondition... newConditions) {
		super.and( newCondition, newConditions);
		return this;
	}

	/**
	 * Delegates to {@link OrCondition} using the current condition and one or more conditions
	 * @param newCondition the new condition
	 * @param newConditions other conditions
	 * @return this instance for chaining
	 * @see OrCondition
	 */
	@Override
	public NumericCompoundCondition<T> or(ICondition newCondition, ICondition... newConditions) {
		super.or(newCondition, newConditions);
		return this;
	}

	/**
	 * Compares the current value of the condition against a given value using {@link EqualsCondition}
	 * @param value the value to test against
	 * @return this instance for chaining
	 * @see EqualsCondition
	 */
	@Override
	public NumericCompoundCondition<T> is(boolean value) {
		super.is(value);
		return this;
	}

	/**
	 * Delegates to {@link NotCondition} given a value to test against the current value
	 * @param testValue the value to test
	 * @return this instance for chaining
	 * @see NotCondition
	 */
	public NumericCompoundCondition<T> not(T testValue) {
		condition = new NotCondition(new EqualsCondition<T>(
			dataSource, new DataSource<>(testValue)));
		return this;
	}

	/**
	 * Delegates to {@link NotCondition} using the current condition
	 * @return this instance for chaining
	 * @see NotCondition
	 */
	@Override
	public NumericCompoundCondition<T> not() {
		super.not();
		return this;
	}

	/**
	 * Delegates to {@link EqualsCondition} given a value to compare against the current value
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 * @see EqualsCondition
	 */
	public NumericCompoundCondition<T> is(T testValue) {
		condition = new EqualsCondition<T>(dataSource, new DataSource<>(testValue),
				false);
		return this;
	}

	/**
	 * Delegates to {@link NotEqualCondition} given a value to compare against the current value
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 * @see NotEqualCondition
	 */
	public NumericCompoundCondition<T> isNot(T testValue) {
		condition = new NotEqualCondition<T>(dataSource, new DataSource<>(testValue),
				false);
		return this;
	}

	/**
	 * Compares the current value of the condition against a given value using {@link NotEqualCondition}
	 * @param value the value to test against
	 * @return this instance for chaining
	 * @see NotEqualCondition
	 */
	@Override
	public NumericCompoundCondition<T> isNot(boolean value) {
		super.isNot(value);
		return this;
	}

	/**
	 * Delegates to {@link GreaterThanCondition} given a value to compare against the current value
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 * @see GreaterThanCondition
	 */
	public NumericCompoundCondition<T> greaterThan(T testValue) {
		condition = new GreaterThanCondition<T>(dataSource, new DataSource<>(testValue));
		return this;
	}

	/**
	 * Delegates to {@link LessThanCondition} given a value to compare against the current value
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 * @see LessThanCondition
	 */
	public NumericCompoundCondition<T> lessThan(T testValue) {
		condition = new LessThanCondition<T>(dataSource, new DataSource<>(testValue));
		return this;
	}

	/**
	 * Delegates to {@link OrCondition} and {@link LessThanCondition} to compare a given value.
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 */
	public NumericCompoundCondition<T> orLessThan(T testValue) {
		return this.or(new LessThanCondition<>(dataSource, new DataSource<>(testValue)));
	}

	/**
	 * Delegates to {@link OrCondition} and {@link GreaterThanCondition} to compare a given value.
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 */
	public NumericCompoundCondition<T> orGreaterThan(T testValue) {
		return this.or(new GreaterThanCondition<>(dataSource, new DataSource<>(testValue)));
	}

	/**
	 * Delegates to {@link AndCondition} and {@link LessThanCondition} to compare a given value.
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 */
	public NumericCompoundCondition<T> andLessThan(T testValue) {
		return this.and(new LessThanCondition<T>(dataSource, new DataSource<>(testValue)));
	}

	/**
	 * Delegates to {@link AndCondition} and {@link GreaterThanCondition} to compare a given value.
	 * @param testValue the value to compare
	 * @return this instance for chaining
	 */
	public NumericCompoundCondition<T> andGreaterThan(T testValue) {
		return this.and(new GreaterThanCondition<>(dataSource, new DataSource<>(testValue)));
	}
}

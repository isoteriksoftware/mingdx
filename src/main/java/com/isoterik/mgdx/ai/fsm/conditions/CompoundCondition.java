package com.isoterik.mgdx.ai.fsm.conditions;

import com.isoterik.mgdx.ai.fsm.ICondition;

/**
 * A condition that delegates to other conditions to create a complex condition.
 * This condition always evaluates to the result obtained from delegated conditions.
 * <p>
 * A {@link CompoundCondition} can delegate to another {@link CompoundCondition} and so on.
 *
 * @author isoteriksoftware
 */
public class CompoundCondition implements ICondition {
	protected ICondition condition;

	/**
	 * Creates an instance with an initial condition to delegate to.
	 * @param initialCondition the initial condition to delegate to
	 */
	public CompoundCondition(ICondition initialCondition) {
		this.condition = initialCondition;
	}

	/**
	 * Delegates to {@link AndCondition} using the current condition and one or more conditions
	 * @param newCondition the new condition
	 * @param newConditions other conditions
	 * @return this instance for chaining
	 * @see AndCondition
	 */
	public CompoundCondition and(ICondition newCondition, ICondition... newConditions) {
		ICondition old = condition;
		condition = new AndCondition(newCondition, newConditions);
		((AndCondition)condition).addCondition(old);
		
		return this;
	}

	/**
	 * Delegates to {@link OrCondition} using the current condition and one or more conditions
	 * @param newCondition the new condition
	 * @param newConditions other conditions
	 * @return this instance for chaining
	 * @see OrCondition
	 */
	public CompoundCondition or(ICondition newCondition, ICondition... newConditions) {
		ICondition old = condition;
		condition = new OrCondition(newCondition, newConditions);
		((OrCondition)condition).addCondition(old);

		return this;
	}

	/**
	 * Delegates to {@link NotCondition} using the current condition
	 * @return this instance for chaining
	 * @see NotCondition
	 */
	public CompoundCondition not() {
		condition = new NotCondition(new BooleanCondition(
			condition.test()));
		return this;
	}

	/**
	 * Compares the current value of the condition against a given value using {@link EqualsCondition}
	 * @param value the value to test against
	 * @return this instance for chaining
	 * @see EqualsCondition
	 */
	public CompoundCondition is(boolean value) {
		condition = new EqualsCondition<>(condition
	    	.test(), value);
		return this;
	}

	/**
	 * Compares the current value of the condition against a given value using {@link NotEqualCondition}
	 * @param value the value to test against
	 * @return this instance for chaining
	 * @see NotEqualCondition
	 */
	public CompoundCondition isNot(boolean value) {
		condition = new NotEqualCondition<>(condition.test(), value);
		return this;
	}

	/**
	 * @return the compounded evaluation of the delegated conditions
	 */
	@Override
	public boolean test() {
		if (condition == null)
			return false;
			
		return condition.test();
	}
}

package com.isoterik.mgdx.ai.fsm.conditions;

import com.badlogic.gdx.utils.Array;
import com.isoterik.mgdx.ai.fsm.ICondition;

/**
 * A condition that performs logical <em>AND</em> operation on all given conditions.
 * The condition is satisfied if and only if all the given conditions are satisfied.
 *
 * @author isoteriksoftware
 * @see GroupCondition
 */
public class AndCondition extends GroupCondition {
	/**
	 * Creates a new instance given an array of conditions.
	 * @param conditions the array of conditions
	 */
	public AndCondition(Array<ICondition> conditions) {
		super(conditions);
	}

	/**
	 * Creates a new instance given one or more conditions.
	 * @param condition first condition
	 * @param conditions more conditions
	 */
	public AndCondition(ICondition condition, ICondition... conditions) {
		super(condition, conditions);
	}

	/**
	 * @return {@code true} if all the conditions are satisfied, {@code false} otherwise
	 */
	@Override
	public boolean test() {
		if (conditions.isEmpty())
			return false;

		for (ICondition condition : conditions) {
			if (!condition.test())
				return false;
		}
		
		return true;
	}
}

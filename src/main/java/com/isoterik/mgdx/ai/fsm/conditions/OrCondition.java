package com.isoterik.mgdx.ai.fsm.conditions;

import com.badlogic.gdx.utils.Array;
import com.isoterik.mgdx.ai.fsm.ICondition;

/**
 * A condition that performs logical <em>OR</em> operation on all given conditions.
 * The condition is satisfied if any of the given conditions is satisfied.
 *
 * @author isoteriksoftware
 * @see GroupCondition
 */
public class OrCondition extends GroupCondition {
	/**
	 * Creates a new instance given an array of conditions.
	 * @param conditions the array of conditions
	 */
	public OrCondition(Array<ICondition> conditions) {
		super(conditions);
	}

	/**
	 * Creates a new instance given one or more conditions.
	 * @param condition first condition
	 * @param conditions more conditions
	 */
	public OrCondition(ICondition condition, ICondition... conditions) {
		super(condition, conditions);
	}

	/**
	 * @return {@code true} if any of the conditions are satisfied, {@code false} otherwise
	 */
	@Override
	public boolean test() {
		for (ICondition condition : conditions) {
			if (condition.test())
				return true;
		}

		return false;
	}
}

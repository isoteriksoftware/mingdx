package com.isoterik.mgdx.ai.fsm.conditions;

import com.badlogic.gdx.utils.Array;
import com.isoterik.mgdx.ai.fsm.ICondition;

/**
 * A condition that evaluates other conditions.
 * Concrete subclasses provide group specific methods of evaluating the sub-conditions.
 *
 * @author isoteriksoftware
 */
public abstract class GroupCondition implements ICondition {
	protected Array<ICondition> conditions;

	/**
	 * Creates a new instance given an array of conditions to evaluate
	 * @param conditions the conditions
	 */
	public GroupCondition(Array<ICondition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * Creates a new instance given one or more conditions.
	 * @param condition a condition to evaluate
	 * @param conditions more conditions
	 */
	public GroupCondition(ICondition condition, ICondition... conditions) {
		this.conditions = new Array<>();
		this.conditions.add(condition);
		this.conditions.addAll(conditions);
	}

	/**
	 * Sets the conditions
	 * @param conditions an array of conditions
	 */
	public void setConditions(Array<ICondition> conditions) {
		this.conditions = conditions;
	}

	/**
	 *
	 * @return the conditions
	 */
	public Array<ICondition> getConditions() {
		return conditions;
	}

	/**
	 * Adds a condition
	 * @param condition the condition to add
	 */
	public void addCondition(ICondition condition) {
		conditions.add(condition);
	}

	/**
	 * Removes a given condition
	 * @param condition the condition to remove
	 */
	public void removeCondition(ICondition condition) {
		conditions.removeValue(condition, true);
	}

	/**
	 * Checks if a given condition exists
	 * @param condition the condition to check
	 * @return {@code true} if given condition exists, {@code false} otherwise
	 */
	public boolean hasCondition(ICondition condition) {
		return conditions.contains(condition, true);
	}
}

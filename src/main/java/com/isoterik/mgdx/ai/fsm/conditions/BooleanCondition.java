package com.isoterik.mgdx.ai.fsm.conditions;

import com.isoterik.mgdx.ai.fsm.ICondition;

/**
 * A condition that wraps a {@code boolean} value. The condition simply returns the current value (boolean), no evaluation is done.
 *
 * @author isoteriksoftware
 */
public class BooleanCondition implements ICondition {
	protected DataSource<Boolean> dataSource;

	/**
	 * Creates a new instance given a data source.
	 * @param dataSource the data source
	 */
	public BooleanCondition(DataSource<Boolean> dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Creates a new instance with a given value.
	 * @param value the current value.
	 */
	public BooleanCondition(boolean value) {
		this(new DataSource<>(value));
	}

	/**
	 * Sets the value for this condition
	 * @param value the value
	 */
	public void setValue(boolean value) {
		dataSource.data = value;
	}

	/**
	 * @return the current value
	 */
	public boolean getValue() {
		return dataSource.data;
	}

	/**
	 * Returns the data source used by this condition.
	 * @return the data source used by this condition.
	 */
	public DataSource<Boolean> getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the data source used by this condition.
	 * @param dataSource the data source.
	 */
	public void setDataSource(DataSource<Boolean> dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the current value, no evaluation is done
	 */
	@Override
	public boolean test() {
		return dataSource.data;
	}
}
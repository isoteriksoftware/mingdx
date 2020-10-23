package com.isoterik.mgdx.m2d.components.physics;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.isoterik.mgdx.Component;

/**
 * A collider represents a {@link Shape} used for collision detection.
 * <strong>Note:</strong> collision {@link Shape}s are disposed automatically after use.
 *
 * @author isoteriksoftware
 */
public abstract class Collider extends Component
{
	protected Fixture fixture;
	protected boolean isSensor;
	
	protected Shape shape;

	protected short groupIndex = 0;
	protected short categoryBits = 0x0001;
	protected short maskBits = -1;

	/**
	 * Determines if this collider is a sensor. A sensor collider collects contact information but never generates a collision response!
	 * @param isSensor whether this collider is a sensor.
	 */
	public void setIsSensor(boolean isSensor)
	{ this.isSensor = isSensor; }

	/**
	 *
	 * @return whether this collider is a sensor
	 */
	public boolean isSensor()
	{ return isSensor; }

	/**
	 *
	 * @return the collision group index
	 */
	public short getGroupIndex()
	{ return groupIndex; }

	/**
	 * Sets the collision group index for this collider.
	 * Collision groups allow a certain group of objects to never collide (negative) or always collide (positive). Zero means no
	 * collision group. Non-zero group filtering always wins against the mask bits.
	 * @param groupIndex the collision group index
	 */
	public void setGroupIndex(short groupIndex)
	{ this.groupIndex = groupIndex; }

	/**
	 *
	 * @return the collision category bits
	 */
	public short getCategoryBits()
	{ return categoryBits; }

	/**
	 * Sets the collision category bits. Normally you would just set one bit.
	 * @param categoryBits the collision category bits
	 */
	public void setCategoryBits(short categoryBits)
	{ this.categoryBits = categoryBits; }

	/**
	 *
	 * @return the collision mask bits.
	 */
	public short getMaskBits()
	{ return maskBits; }

	/**
	 * Sets the collision mask bits. This states the categories that this shape would accept for collision.
	 * @param maskBits the collision mask bits.
	 */
	public void setMaskBits(short maskBits)
	{ this.maskBits = maskBits; }

	/**
	 * Sets the {@link Fixture} generated for this collider.
	 * This is called internally by the system and should never be called
	 * @param fixture the fixture
	 */
	public void __setFixture(Fixture fixture)
	{ this.fixture = fixture; }

	/**
	 *
	 * @return the {@link Fixture} that this collider generates
	 */
	public Fixture getFixture()
	{ return fixture; }

	/**
	 * Disposes the {@link Shape} used by this collider.
	 * This is called internally by the system and should never be called
	 */
	public void __disposeShape()
	{
		if (shape != null)
			shape.dispose();
	}

	/**
	 * Creates a {@link FixtureDef} for this collider and return it.
	 * Concrete subclasses must implement this method
	 * @return a {@link FixtureDef} for this collider
	 */
	public abstract FixtureDef getFixtureDef();
}

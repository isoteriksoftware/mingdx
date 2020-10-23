package com.isoterik.mgdx.m2d.components.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.isoterik.mgdx.GameCamera;

/**
 * A debug draw that renders a cross (X) at the position of the host game object
 *
 * @author isoteriksoftware
 */
public class XDebugDraw extends DebugDrawBase
{
	protected float size = .1f;

	/**
	 * Creates a new instance given a color
	 * @param color the color
	 */
	public XDebugDraw(Color color)
	{ super(LINE_SHAPE, color); }

	/**
	 * Creates a new instance
	 */
	public XDebugDraw()
	{ this(Color.RED); }

	/**
	 * Sets the size of the cross drawn
	 * @param size the size in world units
	 */
	public void setSize(float size)
	{ this.size = size; }

	/**
	 *
	 * @return the size of the cross drawn
	 */
	public float getSize()
	{ return size; }
	
	@Override
	public XDebugDraw setColor(Color color)
	{
		super.setColor(color);
		return this;
	}

	/**
	 * The shape defaults to ShapeType.Line and cannot be changed!
	 * @throws UnsupportedOperationException if called
	 */
	@Override
	public XDebugDraw setShapeType(ShapeType shapeType) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("A PointDebugDraw can only use ShapeRenderer.ShapeType.Line shape " +
			" and cannot be changed!");
	}

	@Override
	public void draw(ShapeRenderer shapeRenderer, GameCamera gameCamera)
	{
		Vector3 pos = gameObject.transform.position;
		
		shapeRenderer.x(pos.x, pos.y, size);
	}
}

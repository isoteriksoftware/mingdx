package com.isoterik.mgdx.m2d.components.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.isoterik.mgdx.GameCamera;

/**
 * A debug draw that renders a circle shape around the host game object
 *
 * @author isoteriksoftware
 */
public class CircleDebugDraw extends DebugDrawBase
{
	protected int segments = 30;

	/**
	 * Creates a new instance given a shape type and a color
	 * @param shapeType the shape type
	 * @param color the color
	 */
	public CircleDebugDraw(ShapeType shapeType, Color color)
	{ super(shapeType, color); }

	/**
	 * Creates a new instance given a shape type
	 * @param shapeType the shape type
	 */
	public CircleDebugDraw(ShapeType shapeType)
	{ super(shapeType); }

	/**
	 * Creates a new instance
	 */
	public CircleDebugDraw()
	{ super(); }

	/**
	 * Sets the number of segments used to render the circle
	 * @param segments the number of segments
	 * @return this instance for chaining
	 */
	public CircleDebugDraw setSegments(int segments)
	{
		this.segments = segments;
		return this;
	}

	/**
	 *
	 * @return the number of segments used to render the circle
	 */
	public int getSegments()
	{ return segments; }

	@Override
	public CircleDebugDraw setColor(Color color)
	{
		super.setColor(color);
		return this;
	}

	@Override
	public CircleDebugDraw setShapeType(ShapeType shapeType)
	{
		super.setShapeType(shapeType);
		return this;
	}

	@Override
	public void draw(ShapeRenderer shapeRenderer, GameCamera gameCamera)
	{
		Vector3 pos = gameObject.transform.position;
		Vector3 size = gameObject.transform.size;

		shapeRenderer.circle(pos.x + size.x *  .5f, pos.y + size.y * .5f, 
			(size.x / size.y) * 4f, segments);
	}
}

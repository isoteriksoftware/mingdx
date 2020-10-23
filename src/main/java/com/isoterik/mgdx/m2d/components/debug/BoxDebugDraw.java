package com.isoterik.mgdx.m2d.components.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.isoterik.mgdx.GameCamera;

/**
 * A debug draw that renders a box shape around the host game object
 *
 * @author isoteriksoftware
 */
public class BoxDebugDraw extends DebugDrawBase
{
	/**
	 * Creates a new instance given a shape type and a color
	 * @param shapeType the shape type
	 * @param color the color
	 */
	public BoxDebugDraw(ShapeType shapeType, Color color)
	{ super(shapeType, color); }

	/**
	 * Creates a new instance given a shape type
	 * @param shapeType the shape type
	 */
	public BoxDebugDraw(ShapeType shapeType)
	{ super(shapeType); }

	/**
	 * Creates a new instance
	 */
	public BoxDebugDraw()
	{ super(); }
	
	@Override
	public BoxDebugDraw setColor(Color color)
	{
		super.setColor(color);
		return this;
	}
	
	@Override
	public BoxDebugDraw setShapeType(ShapeType shapeType)
	{
		super.setShapeType(shapeType);
		return this;
	}
	
	@Override
	public void draw(ShapeRenderer shapeRenderer, GameCamera gameCamera)
	{
		Vector3 pos = gameObject.transform.position;
		Vector3 size = gameObject.transform.size;
		Vector3 origin = gameObject.transform.origin;
		Vector3 scale = gameObject.transform.scale;
		float rotation = gameObject.transform.getRotation();
		
		shapeRenderer.rect(pos.x, pos.y, origin.x, origin.y, 
			size.x, size.y, scale.x, scale.y, rotation);
	}
}

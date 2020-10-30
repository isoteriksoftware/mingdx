package com.isoterik.mgdx.ai;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isoterik.mgdx.Component;
import com.isoterik.mgdx.GameObject;
import com.isoterik.mgdx.utils.ArithmeticUtils;

/**
 * An instance of {@link GameObjectLocation2d} conveys and manipulate the 2D world information of a {@link GameObject}.
 * This is used extensively by {@link com.badlogic.gdx.ai.steer.SteeringBehavior}s to know the current location of a {@link GameObject}.
 * It is a component, so it can be attached to any {@link GameObject}.
 *
 * @author isoteriksoftware
 * @see Location
 * @see Component
 */
public class GameObjectLocation2d extends Component implements Location<Vector2> {
    /**
     * The position of the {@link GameObject} in 2D coordinates
     */
    protected Vector2 position;

    /**
     * Creates a new instance with no {@link GameObject}.
     * This will make the instance less usable until explicitly attached to a {@link GameObject}
     */
    public GameObjectLocation2d() {
        this.position = new Vector2();
    }

    /**
     * Creates a new instance and automatically attaches itself to the given {@link GameObject}
     * @param gameObject the {@link GameObject} that should be associated with the instance
     */
    public GameObjectLocation2d(GameObject gameObject) {
        this();
        gameObject.addComponent(this);
    }

    /**
     * Gets the 2D position of the host {@link GameObject}
     * @return the position of the host {@link GameObject} or the default position if this instance is not attached to any {@link GameObject}
     */
    @Override
    public Vector2 getPosition() {
        if (gameObject != null)
            position.set(gameObject.transform.position.x, gameObject.transform.position.y);

        return position;
    }

    /**
     * Gets the current orientation of the host {@link GameObject}
     * @return the current orientation of the host {@link GameObject} or always 0 if this instance is not attached to any {@link GameObject}
     */
    @Override
    public float getOrientation() {
        if (gameObject == null)
            return 0;

        return gameObject.transform.getRotation()
                * MathUtils.degreesToRadians;
    }

    /**
     * Sets the orientation of the host {@link GameObject} if any.
     * @param orientation the orientation
     */
    @Override
    public void setOrientation(float orientation) {
        if (gameObject != null)
        gameObject.transform.setRotation(
                orientation * MathUtils.radiansToDegrees);
    }

    /**
     * Creates a new instance.
     * @return a new instance.
     */
    @Override
    public Location<Vector2> newLocation() {
        return new GameObjectLocation2d();
    }

    /**
     * Converts a {@link Vector2} to an angle in radians
     * @param vector the {@link Vector2}
     * @return the converted angle in radians
     */
    @Override
    public float vectorToAngle(Vector2 vector) {
        return ArithmeticUtils.vectorToAngle2d(vector);
    }

    /**
     * Converts an angle in radians to a {@link Vector2}
     * @param outVector an existing instance of {@link Vector2} to set the result on
     * @param angle the angle in radians
     * @return the output {@link Vector2}
     */
    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return ArithmeticUtils.angleToVector2d(angle, outVector);
    }
}

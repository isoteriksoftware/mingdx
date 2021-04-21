package com.isoterik.mgdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * The Transform component determines the position, rotation, scale, size and origin of a {@link GameObject}.
 *
 * @author isoteriksoftware
 */
public class Transform extends Component {
    /** The position of the host game object */
    public final Vector3 position;

    /** The scale of the host game object */
    public final Vector3 scale;

    /** The size of the host game object */
    public final Vector3 size;

    /** The origin of the host game object */
    public final Vector3 origin;

    /** The orientation of the host game object */
    public final Vector3 rotation;

    protected final Vector3 temp = new Vector3();

    /**
     * Creates a new instance.
     */
    public Transform() {
        position = new Vector3(0, 0, 0);
        scale    = new Vector3(1, 1, 1);
        size     = new Vector3(0, 0, 0);
        origin   = new Vector3(0, 0,  0);
        rotation = new Vector3(0, 0, 0);
    }

    /**
     * Sets the origin of the host game object.
     * @param originX origin on the x-axis
     * @param originY origin on the y-axis
     * @param originZ origin on the z-axis
     */
    public void setOrigin(float originX, float originY, float originZ)
    { this.origin.set(originX, originY, originZ); }

    /**
     * Sets the origin of the host game object.
     * @param originX origin on the x-axis
     * @param originY origin on the y-axis
     */
    public void setOrigin(float originX, float originY)
    { setOrigin(originX, originY, origin.z); }

    /**
     * Sets the size of the host game object.
     * @param width the width
     * @param height the height
     * @param depth the depth
     */
    public void setSize(float width, float height, float depth)
    { this.size.set(width, height, depth); }

    /**
     * Sets the size of the host game object.
     * @param width the width
     * @param height the height
     */
    public void setSize(float width, float height)
    { setSize(width, height, size.z); }

    /**
     * Sets the position of the host game object.
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param z position on the z-axis
     */
    public void setPosition(float x, float y, float z)
    { this.position.set(x, y, z); }

    /**
     * Sets the position of the host game object.
     * @param x position on the x-axis
     * @param y position on the y-axis
     */
    public void setPosition(float x, float y)
    { setPosition(x, y, position.z); }

    /**
     * Sets the scale of the host game object.
     * @param scaleX scale on the x-axis
     * @param scaleY scale on the y-axis
     * @param scaleZ scale on the z-axis
     */
    public void setScale(float scaleX, float scaleY, float scaleZ)
    { this.scale.set(scaleX, scaleY, scaleZ); }

    /**
     * Sets the scale of the host game object.
     * @param scaleX scale on the x-axis
     * @param scaleY scale on the y-axis
     */
    public void setScale(float scaleX, float scaleY)
    { setScale(scaleX, scaleY, scale.z); }

    /**
     * Sets the rotation of the host game object.
     * @param rotationX rotation around the x-axis
     * @param rotationY rotation around the y-axis
     * @param rotationZ rotation around the z-axis
     */
    public void setRotation(float rotationX, float rotationY, float rotationZ)
    { this.rotation.set(rotationX, rotationY, rotationZ); }

    /**
     * Sets the rotation around the y-axis. Useful for 2D game objects.
     * @param rotation rotation around the z-axis
     */
    public void setRotation(float rotation)
    { setRotation(0, 0, rotation); }

    /**
     *
     * @return the rotation vector
     */
    public Vector3 getRotationVector()
    { return this.rotation; }

    /**
     *
     * @return rotation around the z-axis
     */
    public float getRotation()
    { return rotation.z; }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public float getScaleZ() {
        return scale.z;
    }

    public float getWidth() {
        return size.x;
    }

    public float getHeight() {
        return size.y;
    }

    public float getDepth() {
        return size.z;
    }

    public float getOriginX() {
        return origin.x;
    }

    public float getOriginY() {
        return origin.y;
    }

    public float getOriginZ() {
        return origin.x;
    }

    public float getRotationX() {
        return rotation.x;
    }

    public float getRotationY() {
        return rotation.y;
    }

    public float getRotationZ() {
        return rotation.z;
    }

    /**
     * Translates the host game object.
     * @param x change on the x-axis
     * @param y change on the y-axis
     * @param z change on the z-axis
     */
    public void translate(float x, float y, float z)
    { this.position.add(x, y, z); }

    /**
     * Translates the host game object.
     * @param x change on the x-axis
     * @param y change on the y-axis
     */
    public void translate(float x, float y)
    { translate(x, y, 0);}

    /**
     * Rotates the host game object around the z-axis
     * @param degAngle change in angle (in degrees)
     */
    public void rotate(float degAngle)
    { this.rotation.z += degAngle; }

    /**
     *
     * @return the diagonal of the host game object
     */
    public float calcDiagonal() {
        float w = size.x;
        float h = size.y;
        float d = size.z;
        float scaleX = scale.x;
        float scaleY = scale.y;
        float scaleZ = scale.z;

        return (float)Math.sqrt(Math.pow(w * scaleX, 2f) +
                Math.pow(h * scaleY, 2f) + Math.pow(d * scaleZ, 2f));
    }

    /**
     * Determines whether the transform is currently within a visible area of a given camera.
     * @param camera the camera
     * @return true if the game can be seen by the camera. false otherwise
     */
    public boolean isInCameraFrustum(Camera camera) {
        float x = position.x;
        float y = position.y;
        float z = position.z;
        float w = size.x;
        float h = size.y;
        float d = size.z;

        temp.set(x + w * .5f, y + h *.5f, z + d * .5f);
        return camera.frustum.sphereInFrustum(temp,
                calcDiagonal() * .5f);
    }

    @Override
    public void attach() {
        if (hasComponent(Transform.class))
            throw new UnsupportedOperationException("There can only be one Transform component for a GameObject!");
    }

    @Override
    public void detach() {
        throw new UnsupportedOperationException("You cannot detach the Transform component!");
    }

    @Override
    public void addComponent(Component component) {
        if (component instanceof Transform && hasComponent(Transform.class))
            throw new UnsupportedOperationException("There can only be one Transform component for a GameObject!");
    }
}
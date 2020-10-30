package com.isoterik.mgdx.m2d.components.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.isoterik.mgdx.Component;
import com.isoterik.mgdx.Scene;
import com.isoterik.mgdx.m2d.physics.PhysicsMaterial2d;

/**
 * {@link com.isoterik.mgdx.GameObject}s that need to simulate physics in 2D space should attach this component. This component is a wrapper for
 * a box2D {@link Body} instance. It relies on one or more {@link Collider}s to generate collision shapes for the host game object.
 *
 * <strong>Note:</strong> this component uses the host game object as the user data for the {@link Body} for internal uses. Do not override the user data!
 *
 * @author isoteriksoftware
 */
public class Physics2d extends Component {
    /** A dynamic body type. Dynamic bodies have positive mass, non-zero velocity determined by forces and  moved by solver */
    public static final BodyDef.BodyType DynamicBody = BodyDef.BodyType.DynamicBody;

    /** A static body type. Static bodies have zero mass, zero velocity and may be manually moved */
    public static final BodyDef.BodyType StaticBody = BodyDef.BodyType.StaticBody;

    /** A kinematic body type. Kinematic bodies have zero mass and non-zero velocity set by user */
    public static final BodyDef.BodyType KinematicBody = BodyDef.BodyType.KinematicBody;

    private Body body;
    private BodyDef.BodyType bodyType;
    private final PhysicsMaterial2d material;

    private Array<Collider> colliders;

    /**
     * Creates a new instance given a body type and a physics material.
     * @param bodyType the body type
     * @param material the physics material
     */
    public Physics2d(BodyDef.BodyType bodyType, PhysicsMaterial2d material) {
        this.bodyType = bodyType;
        this.material = material;

        colliders = new Array<>();
    }

    /**
     * Creates a new instance given a body type and no physics material. A default {@link PhysicsMaterial2d#PhysicsMaterial2d()} is used
     * @param bodyType the body type
     */
    public Physics2d(BodyDef.BodyType bodyType)
    { this(bodyType, new PhysicsMaterial2d()); }

    /**
     * Gets the box2D {@link Body} that this instance uses.
     * @return the box2D {@link Body} that this instance uses or null if the body is not created yet
     */
    public Body getBody()
    { return body; }

    /**
     * This creates the physics body if it is not created yet and returns it.
     * <strong>Note:</strong> null will be returned if this component is not attached to a game object yet. If there is an existing body,
     * that body will be returned
     * @param hostScene a scene where the host game object will be added to
     * @return the created physics body or null if a body cannot be created
     */
    public Body getBody(Scene hostScene) {
        if (body != null)
            return body;

        if (gameObject == null)
            return null;

        createBody(hostScene.getPhysicsWorld2d());
        return body;
    }

    private void createAndAttachCollider(Collider collider) {
        FixtureDef fdef = collider.getFixtureDef();
        fdef.friction = material.friction;
        fdef.restitution = material.bounciness;
        fdef.density = material.density;
        fdef.isSensor = collider.isSensor();
        fdef.filter.categoryBits = collider.getCategoryBits();
        fdef.filter.groupIndex = collider.getGroupIndex();
        fdef.filter.maskBits = collider.getMaskBits();

        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData(collider.getUserData());
        collider.__setFixture(fixture);
        collider.__disposeShape();
    }

    /*
    Creates the physics body for the host game object
     */
    private void createBody(World physicsWorld) {
        // Offset the current game object position by half its dimension
        Vector2 pos = new Vector2(gameObject.transform.position.x,
                gameObject.transform.position.y);
        pos.add(gameObject.transform.size.x * .5f,
                gameObject.transform.size.y * .5f);

        // The origin of the game object must be at the center for simulation to work
        gameObject.transform.origin.set(gameObject.transform.size.x * .5f,
                gameObject.transform.size.y * .5f, 0);

        BodyDef bdef = new BodyDef();
        bdef.type = bodyType;
        bdef.angle = gameObject.transform.getRotation() * MathUtils.degreesToRadians;
        bdef.position.set(pos);

        // Create the body
        body = physicsWorld.createBody(bdef);
        body.setUserData(gameObject);

        // Create the collision shapes using available colliders
        for (Collider collider : colliders) {
            createAndAttachCollider(collider);
        }
    }

    /**
     * Interpolates the physic body to avoid temporal aliasing.
     * This method is called internally by the system and should never be called directly.
     * @param alpha the ratio of the time spent by the renderer to a fixed time steps
     */
    public void __interpolate(float alpha) {
        // We bail out if the body is either null or inactive
        if (body == null || !body.isActive())
            return;

        // Get the transform data from the physics body
        com.badlogic.gdx.physics.box2d.Transform transform =
                body.getTransform();
        Vector2 bodyPosition = transform.getPosition();

        // Offset the current body position by half the dimension of the game object
        // This effectively move the position from the center of the physics body to its lower left
        bodyPosition.sub(gameObject.transform.size.x * .5f,
                gameObject.transform.size.y * .5f);

        // Get the position of the game object
        Vector3 position = gameObject.transform.position;

        // Get the rotation
        float angle = gameObject.transform.getRotation();

        // Convert the physics body angle from radians to degrees
        float bodyAngle = transform.getRotation() * MathUtils.radiansToDegrees;

        // Interpolate the position
        position.x = bodyPosition.x * alpha + position.x * (1.0f - alpha);
        position.y = bodyPosition.y * alpha + position.y * (1.0f - alpha);

        // Interpolate the rotation
        gameObject.transform.setRotation(bodyAngle * alpha + angle * (1.0f - alpha));
    }

    private void __disposeBody() {
        if (scene == null || body == null)
            return;

        scene.destroyPhysicsBody2d(body);
    }

    @Override
    public void __setHostScene(Scene scene) {
        super.__setHostScene(scene);

        // Create a physics body once a valid scene is available
        if (scene != null && body == null) {
            createBody(scene.getPhysicsWorld2d());
        }
    }

    @Override
    public void attach() {
        // This body cannot have more than one instance of this Component
        if (hasComponent(Physics2d.class))
            throw new UnsupportedOperationException("A GameObject can have only one instance of Physics2d attached!");

        // This signals that the component was just added
        body = null;
        colliders.clear();

        // Grab available colliders
        Array<Collider> colls = getComponents(Collider.class);
        if (!colls.isEmpty()) {
            for (Collider coll : colls) {
                colliders.add(coll);
            }
        }
    }

    @Override
    public void detach() {
        // Destroy the physics body associated with this RigidBody
        this.__disposeBody();
    }

    @Override
    public void start() {
        // Create the physics body using available colliders
        if (body == null)
            createBody(scene.getPhysicsWorld2d());
    }

    @Override
    public void componentAdded(Component component) {
        // If the component added is a Collider then we have add it to our list of colliders

        if (component instanceof Collider) {
            Collider collider = (Collider)component;
            if (!colliders.contains(collider,true)) {
                colliders.add(collider);

                // If we have a non-null physics body already then we have to attach this collider immediately
                if (body != null) {
                    createAndAttachCollider(collider);
                }
            }
        }
    }

    @Override
    public void componentRemoved(Component component) {
        // If the component removed is a Collider then we have to remove it from our list of colliders.
        // We also need to detach it from the body

        if (component instanceof Collider) {
            Collider collider = (Collider)component;
            if (!colliders.contains(collider,true))
                return;

            colliders.removeValue(collider, true);

            if (body != null) {
                body.destroyFixture(collider.getFixture());
            }
        }
    }

    @Override
    public void fixedUpdate(float deltaTime) {
        // Attempt to create a body if none exists.
        // This should never happen but just in case
        if (body == null)
            createBody(scene.getPhysicsWorld2d());

        // Apply updates only when we have a valid body
        if (body != null)
        {
            Transform transform = body.getTransform();
            Vector2 pos = transform.getPosition();
            float rotation = transform.getRotation();

            // Offset the position from the origin (center)
            pos.sub(gameObject.transform.size.x * 0.5f, gameObject.transform.size.y * 0.5f);

            // Convert the angle to degrees
            rotation *= MathUtils.radiansToDegrees;

            // Update the transform
            gameObject.transform.position.set(pos, 0);
            gameObject.transform.setRotation(rotation);
        }
    }
}

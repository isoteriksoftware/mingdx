package com.isoterik.mgdx.m2d.components.ai.steer;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.isoterik.mgdx.Component;
import com.isoterik.mgdx.GameObject;
import com.isoterik.mgdx.utils.ArithmeticUtils;
import com.isoterik.mgdx.ai.GameObjectLocation2d;
import com.isoterik.mgdx.m2d.components.physics.Physics2d;

/**
 * Attach this component to your {@link GameObject}s to make them steerable in 2D space.
 * For this component to be useful, the host {@link GameObject} must have {@link Physics2d} attached!
 *
 * @see com.badlogic.gdx.ai.steer.Steerable
 * @see SteeringBehavior
 * @see Component
 *
 * @author isoteriksoftware
 */
public class SteerableGameObject2d extends Component implements Steerable<Vector2> {
	protected Body body;
	
	protected float boundingRadius;
	protected float zeroLinearSpeedThreshold;
	protected boolean tagged;
	
	protected float maxLinearSpeed;
	protected float maxLinearAcceleration;
	protected float maxAngularSpeed;
	protected float maxAngularAcceleration;
	
	protected boolean independentFacing;
	
	protected SteeringBehavior<Vector2> steeringBehavior;
	
	protected static final SteeringAcceleration<Vector2> 
		steeringOutput = new SteeringAcceleration<>(
			new Vector2());

	/**
	 * Creates a new instance given an owner. This component will be attached to the owner if not null.
	 * @param owner the game object that owns this component
	 * @param boundingRadius the bounding radius of the owner
	 * @param independentFacing if true, the steerable will rely on angular acceleration to get a resultant orientation. Otherwise the steerable will make the host
	 * game object to always face the direction where it is going.
	 *
	 */
	public SteerableGameObject2d(GameObject owner, float boundingRadius, boolean independentFacing) {
		if (owner != null)
			owner.addComponent(this);
			
		this.boundingRadius = boundingRadius;
		this.independentFacing = independentFacing;
		this.tagged = false;

		// We set defaults. This makes sure the steerable produces motion in cases where the user forgets to set these.
		// These can always be changed later
		this.zeroLinearSpeedThreshold = 0.001f;
		this.maxAngularAcceleration = .5f;
		this.maxAngularSpeed = 3f;
		this.maxLinearAcceleration = 10f;
		this.maxLinearSpeed = 3f;
	}

	/**
	 * Creates a new instance given an owner. This component will be attached to the owner if not null.
	 * @param owner the game object that owns this component
	 * @param boundingRadius the bounding radius of the owner
	 */
	public SteerableGameObject2d(GameObject owner, float boundingRadius)
	{ this(owner, boundingRadius, false); }

	/**
	 * Creates a new instance given an owner. This component will be attached to the owner if not null.
	 * @param owner the game object that owns this component
	 */
	public SteerableGameObject2d(GameObject owner) {
		this(owner, (owner.transform.size.x / owner.transform.size.y)
			* 4f);
	}

	/**
	 * Creates a new instance with no owner.
	 * @param boundingRadius the bounding radius of the owner
	 * @param independentFacing if true, the steerable will rely on angular acceleration to get a resultant orientation. Otherwise the steerable will make the host
	 * game object to always face the direction where it is going.
	 */
	public SteerableGameObject2d(float boundingRadius, boolean independentFacing)
	{ this(null, boundingRadius, independentFacing); }

	/**
	 * Creates a new instance with no owner.
	 * @param boundingRadius the bounding radius of the owner
	 */
	public SteerableGameObject2d(float boundingRadius)
	{ this(null, boundingRadius); }

	/**
	 * Creates a new instance with no owner.
	 */
	public SteerableGameObject2d()
	{ this(4f); }

	/**
	 * Sets the steering behavior for this steerable to use.
	 * @param steeringBehavior the steering behavior
	 */
	public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior)
	{ this.steeringBehavior = steeringBehavior; }

	/**
	 *
	 * @return the current steering behavior
	 */
	public SteeringBehavior<Vector2> getSteeringBehavior()
	{ return steeringBehavior; }

	/**
	 * Determines when the linear speed can be considered a zero length speed.
	 * @param zeroLinearSpeedThreshold the threshold
	 */
	public void setZeroLinearSpeedThreshold(float zeroLinearSpeedThreshold)
	{ this.zeroLinearSpeedThreshold = zeroLinearSpeedThreshold; }

	public float getZeroLinearSpeedThreshold()
	{ return zeroLinearSpeedThreshold; }

	/**
	 * Tries to get the physics body ({@link Body}) attached to the host game object.
	 */
	protected void resolvePhysicsBody() {
		if (body == null) {
			Physics2d physics2d = getComponent(Physics2d.class);
			if (physics2d == null)
				return;
				
			body = physics2d.getBody();
		}
	}

	/**
	 * Sets the bounding radius
	 * @param boundingRadius the radius
	 */
	public void setBoundingRadius(float boundingRadius)
	{ this.boundingRadius = boundingRadius; }
	
	@Override
	public float getBoundingRadius()
	{ return boundingRadius; }

	public void setTagged(boolean tagged)
	{ this.tagged = tagged; }
	
	@Override
	public boolean isTagged()
	{ return tagged; }

	/**
	 * Sets the maximum linear speed
	 * @param maxLinearSpeed maximum linear speed
	 */
	public void setMaxLinearSpeed(float maxLinearSpeed)
	{ this.maxLinearSpeed = maxLinearSpeed; }

	@Override
	public float getMaxLinearSpeed()
	{ return maxLinearSpeed; }

	/**
	 * Sets the maximum linear acceleration
	 * @param maxLinearAcceleration maximum linear acceleration
	 */
	public void setMaxLinearAcceleration(float maxLinearAcceleration)
	{ this.maxLinearAcceleration = maxLinearAcceleration; }

	@Override
	public float getMaxLinearAcceleration()
	{ return maxLinearAcceleration; }

	/**
	 * Sets the maximum angular speed
	 * @param maxAngularSpeed maximum angular speed
	 */
	public void setMaxAngularSpeed(float maxAngularSpeed)
	{ this.maxAngularSpeed = maxAngularSpeed; }

	@Override
	public float getMaxAngularSpeed()
	{ return maxAngularSpeed; }

	/**
	 * Sets the maximum angular acceleration
	 * @param maxAngularAcceleration maximum angular acceleration
	 */
	public void setMaxAngularAcceleration(float maxAngularAcceleration)
	{ this.maxAngularAcceleration = maxAngularAcceleration; }
	
	@Override
	public float getMaxAngularAcceleration()
	{ return maxAngularAcceleration; }

	/**
	 * Determines if this steerable should apply angular acceleration or not
	 * @param independentFacing if true, the steerable will rely on angular acceleration to get a resultant orientation. Otherwise the steerable will make the host
	 * game object to always face the direction where it is going.
	 */
	public void setIndependentFacing(boolean independentFacing)
	{ this.independentFacing = independentFacing; }

	/**
	 *
	 * @return whether angular acceleration is applied
	 */
	public boolean isIndependentFacing()
	{ return independentFacing; }

	@Override
	public void setOrientation(float orientation) {
		resolvePhysicsBody();
		if (body != null)
			body.setTransform(body.getPosition(), orientation);
	}

	@Override
	public float getAngularVelocity() {
		resolvePhysicsBody();
		if (body == null)
			return 0;
			
		return body.getAngularVelocity();
	}

	@Override
	public Vector2 getLinearVelocity() {
		resolvePhysicsBody();
		if (body == null)
			return Vector2.Zero;
		
		return body.getLinearVelocity();
	}

	@Override
	public float getOrientation() {
		resolvePhysicsBody();
		if (body == null)
			return 0;
		
		return body.getAngle();
	}

	@Override
	public Vector2 getPosition() {
		resolvePhysicsBody();
		if (body == null)
			return Vector2.Zero;
		
		return body.getPosition();
	}

	@Override
	public float vectorToAngle(Vector2 vector)
	{ return ArithmeticUtils.vectorToAngle2d(vector); }

	@Override
	public Vector2 angleToVector(Vector2 vector, float angle)
	{ return ArithmeticUtils.angleToVector2d(angle, vector); }

	@Override
	public Location<Vector2> newLocation()
	{ return new GameObjectLocation2d(gameObject); }

	@Override
	public void start() {
		/*
		On start, we try to resolve the physics body for the host game object. At this point it is possible that no Physics2d component is attached
		so the physics body may still be null. However making this check now will help speed things up because if a physics body exists, we wont have to
		make this query again
		 */
		resolvePhysicsBody();
	}

	@Override
	public void componentAdded(Component component) {
		/*
		We check to see if the component added is a Physics2d instance. If it is then we grab the physics body
		 */
		if (component instanceof Physics2d)
			resolvePhysicsBody();
	}

	@Override
	public void componentRemoved(Component component) {
		/*
		Once the Physics2d component is removed, the physics body will be destroyed. So we need to make sure we are not pointing to a non-existing instance
		 */
		if (component instanceof Physics2d)
			body = null;
	}

	@Override
	public void fixedUpdate(float deltaTime) {
		// We do nothing if there is no body yet
		resolvePhysicsBody();
		if (body == null)
			return;
			
		if (steeringBehavior != null) {
			// Calculate steering acceleration
			steeringBehavior.calculateSteering(steeringOutput);

			// Apply steering acceleration
			applySteering(deltaTime);
		}
	}
	
	protected void applySteering(float deltaTime) {
		// Was there any acceleration applied?
		boolean anyAcceleration = false;
		
		// Update position and linear velocity
		if (!SteerableGameObject2d.steeringOutput.linear.isZero()) {
			body.applyForceToCenter(SteerableGameObject2d.steeringOutput.linear, true);
			anyAcceleration = true;
		}
		
		// Update orientation and angular acceleration
		// IF independentFacing is 'true', we apply any angular acceleration provided else we make the game object face where it is going
		if (independentFacing) {
			if (SteerableGameObject2d.steeringOutput.angular != 0) {
				body.applyTorque(SteerableGameObject2d.steeringOutput.angular, true);
				anyAcceleration = true;
			}
		}
		else {
			// Face movement direction if there is any velocity
			Vector2 vel = body.getLinearVelocity();
			if (!vel.isZero(zeroLinearSpeedThreshold)) {
				float newOrientation = vectorToAngle(vel);
				body.setAngularVelocity((newOrientation - getAngularVelocity())
					* deltaTime);
				body.setTransform(body.getPosition(), newOrientation);
			}
		}

		// If there was any angular acceleration applied then we have the limit the speeds
		if (anyAcceleration) {
			Vector2 vel = body.getLinearVelocity();
			float currSpeedSqr = vel.len2();
			if (currSpeedSqr > maxLinearSpeed * maxLinearSpeed)
				body.setLinearVelocity(vel.scl(maxLinearSpeed / 
					(float)Math.sqrt(currSpeedSqr)));
					
			// Cap the angular speed
			if (body.getAngularVelocity() > maxAngularSpeed)
				body.setAngularVelocity(maxAngularSpeed);
		}
	}
}

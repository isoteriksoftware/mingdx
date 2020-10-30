package com.isoterik.mgdx.m2d.physics;

/**
 * Defines the physical properties of a physics body. PhysicsMaterials are immutable!
 *
 * @author isoteriksoftware
 */
public class PhysicsMaterial2d {
    /**
     * The friction coefficient, usually in the range [0,1].
     */
    public final float friction;

    /**
     * The restitution (elasticity) usually in the range [0,1]. The higher the value, the more energy conserved by the body.
     */
    public final float bounciness;

    /**
     * The density, usually in kg/m^2.
     */
    public final float density;

    /**
     * Creates a new instance given the friction, bounciness and density
     * @param friction The friction coefficient, usually in the range [0,1].
     * @param bounciness The restitution (elasticity) usually in the range [0,1]. The higher the value, the more energy conserved by the body.
     * @param density The density, usually in kg/m^2.
     */
    public PhysicsMaterial2d(float friction, float bounciness,
                           float density) {
        this.friction   = friction;
        this.bounciness = bounciness;
        this.density    = density;
    }

    /**
     * Creates an instance with default values. friction defaults to 0.4f, bounciness defaults to 0.1f and density defaults to 1f
     */
    public PhysicsMaterial2d() {
        this(0.4f, 0.1f, 1f);
    }
}

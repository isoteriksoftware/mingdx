package com.isoterik.mgdx.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.isoterik.mgdx.MinGdx;
import com.isoterik.mgdx.Scene;
import com.isoterik.mgdx.m2d.scenes.transition.TransitionDirection;
import com.isoterik.mgdx.utils.WorldUnits;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;

/**
 * Provides convenient methods for animating {@link Actor}s.
 *
 * @author isoteriksoftware
 */
public final class ActorAnimation {
    private static ActorAnimation instance;

    private float screenWidth, screenHeight;

    public static final int UP = TransitionDirection.UP;
    public static final int DOWN = TransitionDirection.DOWN;
    public static final int LEFT = TransitionDirection.LEFT;
    public static final int RIGHT = TransitionDirection.RIGHT;

    private ActorAnimation(){}

    /**
     * Returns a singleton instance. This will assume the dimensions of the current scene if it has a valid main camera
     * @return singleton instance.
     */
    public static ActorAnimation instance() {
        if(instance == null)
			instance = new ActorAnimation();

        Scene currentScene = MinGdx.instance().sceneManager.getCurrentScene();
        if (currentScene != null && currentScene.getMainCamera() != null) {
            WorldUnits worldUnits = currentScene.getMainCamera().getWorldUnits();
            instance.setup(worldUnits.getScreenWidth(), worldUnits.getScreenHeight());
        }

        return(instance);
    }

    /**
     * Sets the screen dimensions to use for animations.
     * @param screenWidth the width in pixels
     * @param screenHeight the height in pixels
     */
    public void setup(float screenWidth, float screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Scales an actor from 0 to 1 for a given duration.
     * @param actor the actor to animate
     * @param duration the duration in seconds
     * @param easing an interpolation to use
     */
    public void grow(Actor actor, float duration,
						   Interpolation easing) {
		actor.setScale(0, 0);
		Action grow = scaleTo(1, 1, duration, easing);
		actor.addAction(grow);
    }

    /**
     * Resize an actor given a fraction to resize by, for a given duration.
     * @param actor the actor to animate
     * @param fraction a fraction to resize by
     * @param duration the duration in seconds
     * @param easing an interpolation to use
     */
    public void resize(Actor actor, float duration,
						 float fraction, Interpolation easing) {
		float w = actor.getWidth() * fraction;
		float h = actor.getHeight() * fraction;
		Action grow = sizeTo(w, h, duration, easing);
		actor.addAction(grow);
    }

    /**
     * Shrinks an actor then remove it from the scene once the animation is completed.
     * @param actor the actor to animate
     * @param duration the duration in seconds
     * @param easing an interpolation to use
     */
    public void shrinkThenRemove(Actor actor, float duration,
							 Interpolation easing) {
		Action shrink = scaleTo(0, 0, duration, easing);
		actor.addAction(sequence(shrink, removeActor()));
    }

    /**
     * Shrinks an actor for a given duration.
     * @param actor the actor to animate
     * @param duration the duration in seconds
     * @param easing an interpolation to use
     */
    public void shrink(Actor actor, float duration,
					   Interpolation easing) {
		Action shrink = scaleTo(0, 0, duration, easing);
		actor.addAction(shrink);
    }

    /**
     * Slides in an actor for a given duration
     * @param actor the actor to animate
     * @param direction the direction of animation. Must be one of {@link #LEFT}, {@link #RIGHT}, {@link #UP}, {@link #DOWN}
     * @param duration the duration in seconds
     * @param easing an interpolation to use
     */
    public void slideIn(Actor actor, int direction,
                        float duration, Interpolation easing) {
        float w = actor.getWidth();
        float h = actor.getHeight();
        float maxW = screenWidth;
        float maxH = screenHeight;
        float x = actor.getX();
        float y = actor.getY();
        float toX = x;
        float toY = y;

        switch(direction) {
            case DOWN:
				y = maxH;
				break;
            case UP:
				y = -h;
				break;
            case LEFT:
				x = -w;
				break;
            case RIGHT:
				x = maxW;
				break;
        }

        actor.setPosition(x, y);
        Action slide = moveTo(toX, toY, duration, 
							  easing);
        actor.addAction(slide);
    }

    /**
     * Slides in an actor for a given duration. The easing defaults to {@link Interpolation#swing}
     * @param actor the actor to animate
     * @param direction the direction of animation. Must be one of {@link #LEFT}, {@link #RIGHT}, {@link #UP}, {@link #DOWN}
     * @param duration the duration in seconds
     */
    public void slideIn(Actor actor, int direction,
                        float duration) {
        slideIn(actor, direction, duration,
					Interpolation.swing);
    }

    /**
     * Slides out an actor for a given duration then remove it from the scene once the animation is done.
     * @param actor the actor to animate
     * @param direction the direction of animation. Must be one of {@link #LEFT}, {@link #RIGHT}, {@link #UP}, {@link #DOWN}
     * @param duration the duration in seconds
     * @param easing an interpolation to use
     */
    public void slideOutThenRemove(Actor actor, int direction,
                                   float duration, Interpolation easing) {
        float w = actor.getWidth();
        float h = actor.getHeight();
        float maxW = screenWidth;
        float maxH = screenHeight;
        float x = actor.getX();
        float y = actor.getY();
        float toX = x;
        float toY = y;

        switch(direction) {
            case UP:
				toY = maxH;
				break;
            case DOWN:
				toY = -h;
				break;
            case LEFT:
				toX = -w;
				break;
            case RIGHT:
				toX = maxW;
				break;
        }

        Action slide = moveTo(toX, toY, duration, 
							  easing);
        actor.addAction(sequence(slide, removeActor()));
    }

    /**
     * Slides out an actor for a given duration then remove it from the scene once the animation is done. The easing defaults to {@link Interpolation#pow5}
     * @param actor the actor to animate
     * @param direction the direction of animation. Must be one of {@link #LEFT}, {@link #RIGHT}, {@link #UP}, {@link #DOWN}
     * @param duration the duration in seconds
     */
    public void slideOutThenRemove(Actor actor, int direction,
                                   float duration) {
        slideOutThenRemove(actor, direction, duration,
					Interpolation.pow5);
    }

    /**
     * Slides out an actor for a given duration.
     * @param actor the actor to animate
     * @param direction the direction of animation. Must be one of {@link #LEFT}, {@link #RIGHT}, {@link #UP}, {@link #DOWN}
     * @param duration the duration in seconds
     * @param easing an interpolation to use
     */
    public void slideOut(Actor actor, int direction,
						 float duration, Interpolation easing) {
        float w = actor.getWidth();
        float h = actor.getHeight();
        float maxW = screenWidth;
        float maxH = screenHeight;
        float x = actor.getX();
        float y = actor.getY();
        float toX = x;
        float toY = y;

        switch(direction) {
            case UP:
				toY = maxH;
				break;
            case DOWN:
				toY = -h;
				break;
            case LEFT:
				toX = -w;
				break;
            case RIGHT:
				toX = maxW;
				break;
        }

        Action slide = moveTo(toX, toY, duration, 
							  easing);
        actor.addAction(slide);
    }

    /**
     * Slides out an actor for a given duration. The easing defaults to {@link Interpolation#pow5}
     * @param actor the actor to animate
     * @param direction the direction of animation. Must be one of {@link #LEFT}, {@link #RIGHT}, {@link #UP}, {@link #DOWN}
     * @param duration the duration in seconds
     */
    public void slideOut(Actor actor, int direction,
						 float duration) {
        slideOut(actor, direction, duration,
				 Interpolation.pow5);
    }
}

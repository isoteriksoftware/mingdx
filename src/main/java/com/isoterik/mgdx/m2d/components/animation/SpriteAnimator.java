package com.isoterik.mgdx.m2d.components.animation;

import com.badlogic.gdx.utils.Array;
import com.isoterik.mgdx.Component;
import com.isoterik.mgdx.GameObject;
import com.isoterik.mgdx.ai.fsm.ICondition;
import com.isoterik.mgdx.ai.fsm.ManagedStateMachine;
import com.isoterik.mgdx.ai.fsm.Transition;
import com.isoterik.mgdx.m2d.animation.SpriteAnimation;

/**
 * A SpriteAnimator uses one or more {@link SpriteAnimation}s to animate a {@link GameObject} having a {@link com.isoterik.mgdx.m2d.components.SpriteRenderer} attached.
 * <p>
 * It uses a {@link ManagedStateMachine} internally to determine the previous animation played, the current animation and the next animation to be played. Every {@link SpriteAnimation} represents
 * a single state. States (animations) cannot be changed directly. To change state (animation), the animator relies on {@link Transition}s.
 *
 * @see ManagedStateMachine
 * @see SpriteAnimation
 * @see Transition
 *
 * @author isoteriksoftware
 */
public class SpriteAnimator extends Component {
	protected Array<SpriteAnimation> animations;
	
	protected ManagedStateMachine<GameObject, SpriteAnimation> stateMachine;
	
	protected SpriteAnimation initialAnimation;
	
	// This objects holds transitions that were added before the component is attached
	// At that point, no GameObject exists and the StateMachine is uninitialized
	private final Array<Transition<SpriteAnimation>> scheduledTransitions
		= new Array<>();

	/**
	 * Creates a new instance given an initial animation and zero or more other animations
	 * @param initialAnimation the initial animation
	 * @param otherAnimations other animations
	 */
	public SpriteAnimator(SpriteAnimation initialAnimation, 
		SpriteAnimation... otherAnimations) {
		this.initialAnimation = initialAnimation;
		
		animations = new Array<>();
		animations.add(initialAnimation);
		animations.addAll(otherAnimations);
	}

	/**
	 *
	 * @return the {@link ManagedStateMachine} used by this animator
	 */
	public ManagedStateMachine<GameObject, SpriteAnimation> getStateMachine()
	{ return stateMachine; }

	@Override
	public void attach() {
		/*
		Because we need a GameObject to animate, we waited till this instance is attached before initializing the state machine
		 */
		stateMachine = new ManagedStateMachine<>(
			gameObject, initialAnimation);
			
		// check if there are any scheduled transitions.
		// scheduled transitions are transitions added before this instance is attached to a GameObject
		for (Transition<SpriteAnimation> transition : scheduledTransitions) {
			stateMachine.addTransition(transition);
		}
		scheduledTransitions.clear();
	}

	@Override
	public void detach() {
		// Once this is detached, we no longer have a valid GameObject to animate.
		stateMachine = null;
	}

	@Override
	public void update(float deltaTime) {
		// Update the state machine
		// This will effectively change (switch) animation if any transition is triggered
		stateMachine.update();
	}

	/**
	 *
	 * @param animation the animation to check
	 * @return whether the given animation is one of the animations managed by this animator
	 */
	public boolean hasAnimation(SpriteAnimation animation)
	{ return animations.contains(animation, true); }

	/**
	 * Adds an animation to the list of animations that this animator manages. This has no effect if the animation was added before
	 * @param animation the animation to add
	 * @return this instance for chaining
	 */
	public SpriteAnimator addAnimation(SpriteAnimation animation) {
		if (!hasAnimation(animation))
			animations.add(animation);
			
		return this;
	}

	/**
	 * Removes an animation from the list of animations that this animator manages. This has no effect if the animation does not exist
	 * @param animation the animation to remove
	 * @return this instance for chaining
	 */
	public SpriteAnimator removeAnimation(SpriteAnimation animation) {
		if (hasAnimation(animation)) {
			animations.removeValue(animation, true);
			
			// Remove all transitions to or from this animation
			for (Transition<SpriteAnimation> transition : stateMachine.getTransitions()) {
				if (transition.getFromState() == animation || transition.getToState() == animation)
					stateMachine.removeTransition(transition);
			}
		}
		
		return this;
	}

	/**
	 * Adds a transition.
	 * <strong>Note:</strong> this will fail if the {@link Transition} is for animation(s) that are not yet added to the list of animations managed by this animator
	 * @param transition the transition to add
	 * @throws IllegalArgumentException if the {@link Transition} is for animation(s) that are not yet added to the list of animations managed by this animator
	 * @return this instance for chaining
	 */
	public SpriteAnimator addTransition(Transition<SpriteAnimation> transition) throws IllegalArgumentException {
		if (!hasAnimation(transition.getFromState()) &&
			!hasAnimation(transition.getToState())) {
				throw new IllegalArgumentException("None of the animations of this transition is managed by this animator!");
		}
		
		if (stateMachine != null)
			stateMachine.addTransition(transition);
		else
			scheduledTransitions.add(transition);
			
		return this;
	}

	/**
	 * Adds a transition given an animation to transition from, an animation to transition to and one or more conditions for the transition.
	 * <strong>Note:</strong> this will fail if either of the animations given is not yet added to the list of animations managed by this animator
	 * @param from the animation to transition from. Can be null. If it is null, transition will be from any state.
	 * @param to the animation to transition to
	 * @param condition a condition for the transition
	 * @param conditions more conditions for the transition
	 * @return this instance for chaining
	 * @throws IllegalArgumentException if either of the animations given is not yet added to the list of animations managed by this animator
	 */
	public SpriteAnimator addTransition(SpriteAnimation from, SpriteAnimation to,
										ICondition condition, ICondition... conditions) throws IllegalArgumentException {
		Array<ICondition> conditionArray = new Array<>();
		conditionArray.add(condition);
		conditionArray.addAll(conditions);
		return addTransition(new Transition<>(from, to, conditionArray));
	}

	/**
	 * Adds a transition given an animation to transition to from any animation state.
	 * <strong>Note:</strong> this will fail if the animation given is not yet added to the list of animations managed by this animator
	 * @param to the animation to transition to
	 * @param condition a condition for the transition
	 * @param conditions more conditions for the transition
	 * @return this instance for chaining
	 * @throws IllegalArgumentException if the animation given is not yet added to the list of animations managed by this animator
	 */
	public SpriteAnimator addTransition(SpriteAnimation to,
										ICondition condition, ICondition... conditions) throws IllegalArgumentException {
		Array<ICondition> conditionArray = new Array<>();
		conditionArray.add(condition);
		conditionArray.addAll(conditions);
		return addTransition(new Transition<>(null, to, conditionArray));
	}

	/**
	 * Removes a transition.
	 * @param transition the transition to remove
	 * @return this instance for chaining
	 */
	public SpriteAnimator removeTransition(Transition<SpriteAnimation> transition) {
		stateMachine.removeTransition(transition);
		return this;
	}

	/**
	 * Returns the current animation.
	 * @return the current animation. or null if the state machine is not initialized yet.
	 */
	public SpriteAnimation getCurrentAnimation() {
		if (stateMachine == null)
			return null;

		return stateMachine.getCurrentState();
	}

	/**
	 * Returns the previous animation.
	 * @return the previous animation. or null if the state machine is not initialized yet.
	 */
	public SpriteAnimation getPreviousAnimation() {
		if (stateMachine == null)
			return null;

		return stateMachine.getPreviousState();
	}

	/**
	 * Returns the global animation. or null if the state machine is not initialized yet.
	 * @return the global animation.
	 */
	public SpriteAnimation getGlobalAnimation() {
		if (stateMachine == null)
			return null;

		return stateMachine.getGlobalState();
	}

	/**
	 * Sets the global animation.
	 * @param globalAnimation the global animation.
	 */
	public void setGlobalAnimation(SpriteAnimation globalAnimation) {
		if (stateMachine == null)
			return;

		stateMachine.setGlobalState(globalAnimation);
	}

	/**
	 *
	 * @return the transitions managed by this animator
	 */
	public Array<Transition<SpriteAnimation>> getTransitions() {
		/*
		If the state machine is not initialized yet it means this instance is not attached so we simply return the scheduled transitions.
		 */
		if (stateMachine != null)
			return stateMachine.getTransitions();

		return scheduledTransitions;
	}
}

package com.isoterik.mgdx.ai.fsm;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.utils.Array;

/**
 * A <strong>ManagedStateMachine</strong> uses {@link Transition}s to automatically manage transitions between states.
 * The state machine uses given {@link Transition}s to determine the current state and the next state.
 * <p>
 * Because it is a <strong>finite state machine</strong>, in addition to the global state, this machine can only be in one state at any given time. {@link Transition}s are
 * evaluated from first to last. If more than one {@link Transition} is triggered, the first evaluated transition will determine the current state, others are ignored!
 * <p>
 * This state machine does not allow explicit change of states (except the global and initial state). If you need more control, use {@link DefaultStateMachine} instead.
 * @param <E> the type of the entity handled by this state machine
 * @param <S> the type of state managed by this state machine
 *
 * @see com.badlogic.gdx.ai.fsm.StateMachine
 * @see Transition
 * @author isoteriksoftware
 */
public class ManagedStateMachine<E, S extends State<E>> extends DefaultStateMachine<E, State<E>> {
	protected Array<Transition<S>> transitions;

	/**
	 * Creates a new instance given an owner, initial state, global state and an {@link Array} of transitions
	 * @param owner the owner of this state machine
	 * @param initialState the initial state
	 * @param globalState the global state
	 * @param transitions an array of transitions
	 */
	public ManagedStateMachine(E owner, S initialState, S globalState,
		Array<Transition<S>> transitions) {
		super(owner, initialState, globalState);
		this.transitions = transitions;
	}

	/**
	 * Creates a new instance given an owner, initial state, global state and no transitions
	 * @param owner the owner of this state machine
	 * @param initialState the initial state
	 * @param globalState the global state
	 */
	public ManagedStateMachine(E owner, S initialState, S globalState) {
		this(owner, initialState, globalState, new Array<>());
	}

	/**
	 * Creates a new instance given an owner and an initial state.
	 * @param owner the owner of this state machine
	 * @param initialState the initial state
	 */
	public ManagedStateMachine(E owner, S initialState) {
		this(owner, initialState, null);
	}

	/**
	 * Creates a new instance given an owner only.
	 * @param owner the owner of this state machine
	 */
	public ManagedStateMachine(E owner) {
		this(owner, null, null);
	}

	/**
	 * Creates a new instance with no owner, initial state, global state or transitions
	 */
	public ManagedStateMachine() {
		this(null, null, null);
	}

	/**
	 * Sets the transitions for this state machine.
	 * This will override the current transitions. If you want to add to the current array instead use {@link #addTransition(Transition)}
	 * @param transitions the array of transitions
	 */
	public void setTransitions(Array<Transition<S>> transitions) {
		this.transitions = transitions;
	}

	/**
	 * @return the array of transitions for this state machine
	 */
	public Array<Transition<S>> getTransitions() {
		return transitions;
	}

	/**
	 * Adds a transition.
	 * @param transition the transition to add.
	 */
	public void addTransition(Transition<S> transition) {
		transitions.add(transition);
	}

	/**
	 * Removes a transition if it exists
	 * @param transition the transition to remove
	 * @return {@code true} if the given transition was removed, {@code false} otherwise
	 */
	public boolean removeTransition(Transition<S> transition) {
		return transitions.removeValue(transition, true);
	}

	/**
	 * Checks if a given transition exists for this state machine
	 * @param transition the transition to check
	 * @return {@code true} if the given transition exists, {@code false} otherwise
	 */
	public boolean hasTransition(Transition<S> transition) {
		return transitions.contains(transition, true);
	}

	/** {@inheritDoc} */
	@Override
	public S getCurrentState() {
		return (S) super.getCurrentState();
	}

	/** {@inheritDoc} */
	@Override
	public S getGlobalState() {
		return (S) super.getGlobalState();
	}

	/** {@inheritDoc} */
	@Override
	public S getPreviousState() {
		return (S) super.getPreviousState();
	}

	/**
	 * This updates the state machine. The current state will be determined using available {@link Transition}s.
	 * {@link Transition}s are evaluated in the order in which they were added, first to last.
	 */
	@Override
	public void update() {
		// Update the global state if any
		if (globalState != null)
			globalState.update(owner);
			
		// Check for any transition
		// The loop breaks once a transition is found (ie. a transition that can occur now)
		for (Transition<S> transition : transitions) {
			boolean triggered = transition.isTriggered();

			if (triggered && !isInState(transition.getToState())) {
				if (transition.getFromState() != null && !isInState(transition.getFromState()))
					continue;

				previousState = currentState;
				currentState = transition.getToState();
			}

			if (triggered)
				break;
		}
		
		// Update the current state if any
		if (currentState != null)
			currentState.update(owner);
	}

	/**
	 * This state machine does not support explicit change of state.
	 * Do not call this method!
	 */
	@Override
	public void changeState(State<E> newState) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This state machine does not support explicit change of state.
	 * Do not call this method!
	 */
	@Override
	public boolean revertToPreviousState() {
		throw new UnsupportedOperationException();
	}
}

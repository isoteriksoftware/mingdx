package com.isoterik.mgdx.m2d.scenes.transition;

import com.badlogic.gdx.math.Interpolation;

/**
 * Provides convenient methods for quickly creating scene transitions.
 *
 * @author isoteriksoftware
 */
public final class SceneTransitions {
    public static Fade fade(float duration)
    { return Fade.init(duration); }

    public static Slide slide(float duration, int direction,
                               boolean slideOut, Interpolation easing)
    { return Slide.init(duration, direction, slideOut, easing); }

    public static Slice slice(float duration, int direction, int numSlices,
                               Interpolation easing)
    { return Slice.init(duration, direction, numSlices, easing); }

    public static Split split(float duration, int direction,
                               boolean slideOut, Interpolation easing)
    { return Split.init(duration, direction, slideOut, easing); }
}
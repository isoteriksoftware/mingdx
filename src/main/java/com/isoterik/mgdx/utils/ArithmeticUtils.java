package com.isoterik.mgdx.utils;

import com.badlogic.gdx.math.*;

/**
 * Defines some useful math functions.
 *
 *
 * @author dave baol
 */
public class ArithmeticUtils {
	/**
	 * Converts a {@link Vector2} to an angle in radians.
	 * @param vector the {@link Vector2} to convert.
	 * @return the calculated angle
	 */
	public static float vectorToAngle2d(Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	/**
	 * Converts an angle to a {@link Vector2}
	 * @param angle the angle in radians
	 * @param outVector an existing {@link Vector2} instance where the output can be set. This help avoid creating new instances every time
	 * @return the converted output vector
	 */
	public static Vector2 angleToVector2d(float angle, Vector2 outVector) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}

	/**
	 * Converts a {@link Vector3} to an angle in radians.
	 * @param vector the {@link Vector3} to convert.
	 * @return the calculated angle
	 */
	public static float vectorToAngle3d(Vector3 vector) {
		return (float)Math.atan2(-vector.z, vector.x);
	}

	/**
	 * Converts an angle to a {@link Vector3}
	 * @param angle the angle in radians
	 * @param outVector an existing {@link Vector3} instance where the output can be set. This help avoid creating new instances every time
	 * @return the converted output vector
	 */
	public static Vector3 angleToVector3d(float angle, Vector3 outVector) {
		outVector.z = -(float)Math.sin(angle);
		outVector.y = 0;
		outVector.x = (float)Math.cos(angle);
		return outVector;
	}

	/**
	 * @return a number in the range [-1, 1]
	 */
	public static float randomBinomial()
	{
		return MathUtils.random() - MathUtils.random();
	}

	/**
	 * Wraps the given angle to the range [-PI, PI]
	 * @param angle the angle in radians
	 * @return the given angle to the range [-PI, PI]
	 */
	public static float wrapAngleAroundZero (float angle)
	{
		if (angle >= 0)
		{
			float rotation = angle % MathUtils.PI2;
			if (rotation > MathUtils.PI) rotation -= MathUtils.PI2;
			return rotation;
		} 
		else 
		{
			float rotation = -angle % MathUtils.PI2;
			if (rotation > MathUtils.PI) rotation -= MathUtils.PI2;
			return -rotation;
		}
	}
	
	/** Returns the greatest common divisor of two <em>positive</em> numbers (this precondition is <em>not</em> checked and the
	 * result is undefined if not fulfilled) using the "binary gcd" method which avoids division and modulo operations. See Knuth
	 * 4.5.2 algorithm B. The algorithm is due to Josef Stein (1961).
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>The result of {@code gcd(x, x)}, {@code gcd(0, x)} and {@code gcd(x, 0)} is the value of {@code x}.</li>
	 * <li>The invocation {@code gcd(0, 0)} is the only one which returns {@code 0}.</li>
	 * </ul>
	 * 
	 * @param a a non negative number.
	 * @param b a non negative number.
	 * @return the greatest common divisor. */
	public static int gcdPositive (int a, int b)
	{
		if (a == 0) return b;

		if (b == 0) return a;

		// Make "a" and "b" odd, keeping track of common power of 2.
		final int aTwos = Integer.numberOfTrailingZeros(a);
		a >>= aTwos;
		final int bTwos = Integer.numberOfTrailingZeros(b);
		b >>= bTwos;
		final int shift = aTwos <= bTwos ? aTwos : bTwos; // min(aTwos, bTwos);

		// "a" and "b" are positive.
		// If a > b then "gdc(a, b)" is equal to "gcd(a - b, b)".
		// If a < b then "gcd(a, b)" is equal to "gcd(b - a, a)".
		// Hence, in the successive iterations:
		// "a" becomes the absolute difference of the current values,
		// "b" becomes the minimum of the current values.
		while (a != b) {
			final int delta = a - b;
			b = a <= b ? a : b; // min(a, b);
			a = delta < 0 ? -delta : delta; // abs(delta);

			// Remove any power of 2 in "a" ("b" is guaranteed to be odd).
			a >>= Integer.numberOfTrailingZeros(a);
		}

		// Recover the common power of 2.
		return a << shift;
	}

	/** Returns the least common multiple of the absolute value of two numbers, using the formula
	 * {@code lcm(a, b) = (a / gcd(a, b)) * b}.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>The invocations {@code lcm(Integer.MIN_VALUE, n)} and {@code lcm(n, Integer.MIN_VALUE)}, where {@code abs(n)} is a power
	 * of 2, throw an {@code ArithmeticException}, because the result would be 2^31, which is too large for an {@code int} value.</li>
	 * <li>The result of {@code lcm(0, x)} and {@code lcm(x, 0)} is {@code 0} for any {@code x}.
	 * </ul>
	 * 
	 * @param a a non-negative number.
	 * @param b a non-negative number.
	 * @return the least common multiple, never negative.
	 * @throws ArithmeticException if the result cannot be represented as a non-negative {@code int} value. */
	public static int lcmPositive (int a, int b) throws ArithmeticException
	{
		if (a == 0 || b == 0) return 0;

		int lcm = Math.abs(mulAndCheck(a / gcdPositive(a, b), b));
		if (lcm == Integer.MIN_VALUE) {
			throw new ArithmeticException("overflow: lcm(" + a + ", " + b + ") > 2^31");
		}
		return lcm;
	}
	
	/** Multiply two integers, checking for overflow.
	 * 
	 * @param x first factor
	 * @param y second factor
	 * @return the product {@code x * y}.
	 * @throws ArithmeticException if the result can not be represented as an {@code int}. */
	public static int mulAndCheck (int x, int y) throws ArithmeticException {
		long m = ((long)x) * ((long)y);
		if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
			throw new ArithmeticException();
		}
		return (int)m;
	}
}

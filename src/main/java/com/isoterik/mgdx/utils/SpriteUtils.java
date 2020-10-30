package com.isoterik.mgdx.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.isoterik.mgdx.MinGdx;
import com.isoterik.mgdx.io.GameAssetsLoader;

/**
 * Provides some useful methods for working with sprites ({@link Texture}s and {@link TextureRegion}s)
 *
 * @author isoteriksoftware
 */
public class SpriteUtils {
	/* Makes sures the number has at least the provided number of digits */
	private static String forceDigits(int number, int digits,
		com.badlogic.gdx.utils.StringBuilder builder) {
		String str = String.valueOf(number);
		if (str.length() == digits)
			return str;
			
		int remainder = str.length() % digits;
		
		builder.clear();
		for (int i = 0; i <= remainder; i++)
			builder.append("0");
			
		builder.append(number);
		String value = builder.toString();
		builder.clear();
		
		return value;
	}

	/**
	 * Gets all the regions from a sprite sequence. The sequence must be ordered and each individual sprite must be named with a similar pattern.
	 * <strong>Note:</strong> the assets must be loaded before calling this method!
	 * <p>
	 * For example; if we have a sprite sequence in this order: <strong>hero/sprite_000.png, hero/sprite_001.png, hero/sprite_002.png, hero/sprite_003.png,
	 * hero/sprite_004.png</strong>. Here <strong>'hero'</strong> is a folder where the sprites are kept. It is not compulsory for the assets to be in a folder but if they are,
	 * you'll have to take note of that fact. A snippet to retrieve the sequence may look like:
	 * <br><br>
	 * <pre>
	 *     String beforeIndex   = "hero/sprite_"; // before the numeric part
	 *     String afterIndex    = ".png"; // after the numeric part
	 *     int startingIndex    = 0; // the index we want to start with (usually the first number)
	 *     int stoppingIndex    = 4; // the last index to consider (usually the last number)
	 *     int digits           = 3; // the number of digits. Here it is 3 digits
	 *     boolean linearFilter = true; // should linear filter be applied to the textures??
	 *
	 *     Array{@literal <TextureRegion>} spriteSequence = SpriteUtils.getSpriteSequence(beforeIndex, afterIndex, startingIndex, stoppingIndex,
	 * 		       digits, linearFilter);
	 * </pre>
	 * @param beforeIndex what comes before the numeric part?
	 * @param afterIndex what comes after the numeric part (usually an extension with a leading dot)
	 * @param startingIndex the index to start with
	 * @param stoppingIndex the index to stop at
	 * @param digits the number of digits
	 * @param linearFilter if true, {@link com.badlogic.gdx.graphics.Texture.TextureFilter#Linear} will be applied, else {@link com.badlogic.gdx.graphics.Texture.TextureFilter#Nearest} will be applied
	 * @return an array of texture regions for the sequence
	 */
	public static Array<TextureRegion> getSpriteSequence(String beforeIndex, 
		String afterIndex, int startingIndex, int stoppingIndex, 
		int digits, boolean linearFilter) {
		Array<TextureRegion> sequence = new Array<>();
		
		com.badlogic.gdx.utils.StringBuilder builder = new 
			com.badlogic.gdx.utils.StringBuilder();
		
		GameAssetsLoader assets = MinGdx.instance().assets;
		
		for (int i = startingIndex; i <= stoppingIndex; i++) {
			String index = forceDigits(i, digits, builder);
			
			builder.clear();
			builder.append(beforeIndex)
				.append(index)
				.append(afterIndex);
				
			String fileName = builder.toString();
			if (assets.isLoaded(fileName, Texture.class))
				sequence.add(assets.regionForTexture(fileName, linearFilter));
		}
		
		return sequence;
	}

	/**
	 * Gets all the regions from a sprite sequence. The sequence must be ordered and each individual sprite must be named with a similar pattern.
	 * {@link com.badlogic.gdx.graphics.Texture.TextureFilter#Linear} is applied by default.
	 * <strong>Note:</strong> the assets must be loaded before calling this method!
	 * @param beforeIndex what comes before the numeric part?
	 * @param afterIndex what comes after the numeric part (usually an extension with a leading dot)
	 * @param startingIndex the index to start with
	 * @param stoppingIndex the index to stop at
	 * @param digits the number of digits
	 * @return an array of texture regions for the sequence
	 */
	public static Array<TextureRegion> getSpriteSequence(String beforeIndex, 
			String afterIndex, int startingIndex, int stoppingIndex, int digits) {
		return getSpriteSequence(beforeIndex, afterIndex, startingIndex, 
			stoppingIndex, digits, true);
	}
}

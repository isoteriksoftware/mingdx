package com.isoterik.mgdx.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * A singleton for managing game {@link Sound}s and {@link Music}s
 *
 * @author isoteriksoftware
 */
public final class AudioManager {
	protected boolean soundEnabled = true;
	protected boolean musicEnabled = true;
	
	private static AudioManager instance;

	/**
	 * Initializes the manager.
	 * This method is called internally by {@link com.isoterik.mgdx.MinGdx} and should never be called directly!
	 */
	public static void __init() {
		instance = new AudioManager();
	}

	/**
	 *
	 * @return a shared instance
	 */
	public static AudioManager instance() {
		return instance;
	}
	
	private AudioManager() {}

	/**
	 * Enables sound playback.
	 * <strong>Note:</strong> this only has effect on subsequent sounds that will be played, it doesn't affect sounds that are already playing
	 * @param soundEnabled sound playback is enabled if {@code true}, disabled otherwise
	 */
	public void setSoundEnabled (boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}

	/**
	 *
	 * @return {@code true} if sound playback is enabled, {@code false} otherwise
	 */
	public boolean isSoundEnabled() {
		return soundEnabled;
	}

	/**
	 * Enables music playback.
	 * <strong>Note:</strong> this only has effect on subsequent musics that will be played, it doesn't affect musics that are already playing
	 * @param musicEnabled music playback is enabled if {@code true}, disabled otherwise
	 */
	public void setMusicEnabled (boolean musicEnabled) {
		this.musicEnabled = musicEnabled;
	}

	/**
	 *
	 * @return {@code true} if music playback is enabled, {@code false} otherwise
	 */
	public boolean isMusicEnabled() {
		return musicEnabled;
	}

	/**
	 * Plays a given sound
	 * @param sound the sound to play
	 * @param volume the volume
	 */
	public void playSound (Sound sound, float volume) {
		if (soundEnabled)
			sound.play(volume);
	}

	/**
	 * Plays a given music
	 * @param music the music to play
	 * @param volume the volume
	 * @param loop if {@code true}, music will loop
	 */
	public void playMusic (Music music, float volume, boolean loop) {
		if (!musicEnabled)
			return;
			
		music.setVolume(volume);
		music.setLooping(loop);
		music.play();
	}
}

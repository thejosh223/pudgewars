package pudgewars.sfx;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.init() to pre-load all the
 * sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */
public enum SoundEffect {
	// GALLOP("gallop.wav"),
	LASSO_RELEASE("hurt.wav"), //
	NASAL("nasal.wav"), //
	NEIGH("neigh.wav"), //
	MOO("cow.wav");

	// Nested class for specifying volume
	public static enum Volume {
		MUTE, VLOW, LOW, NORMAL, HIGH, VHIGH
	}

	public static Volume volume = Volume.LOW;

	// Each sound effect has its own clip, loaded with its own sound file.
	private Clip clip;

	// Constructor to construct each element of the enum with its own sound file.
	SoundEffect(String soundFileName) {
		try {
			// Use URL (instead of File) to read from disk and JAR.
			URL url = this.getClass().getClassLoader().getResource("sfx/" + soundFileName);
			// Set up an audio input stream piped from the sound file.
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			// Get a clip resource.
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// Play or Re-play the sound effect from the beginning, by rewinding.
	public void play() {
		if (volume != Volume.MUTE) {
			FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			switch (volume) {
				case VLOW:
					vol.setValue(-20);
				case LOW:
					vol.setValue(-10);
					break;
				case NORMAL:
					vol.setValue(0);
					break;
				case HIGH:
					vol.setValue(10);
				case VHIGH:
					vol.setValue(20);
			}

			if (clip.isRunning()) clip.stop(); // Stop the player if it is still running
			clip.setFramePosition(0); // rewind to the beginning
			clip.start(); // Start playing
		}
	}

	public void stop() {
		clip.stop();
	}

	public boolean isPlaying() {
		if (clip.isRunning()) return true;
		return false;
	}

	// Optional static method to pre-load all the sound files.
	public static void init() {
		values(); // calls the constructor for all the elements
	}
}
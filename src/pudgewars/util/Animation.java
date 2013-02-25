package pudgewars.util;

import java.awt.Image;
import java.util.ArrayList;

public class Animation {
	private ArrayList<OneScene> scenes;
	private int sceneIndex; // The index for the scene in your ArrayList

	private boolean playing;
	private boolean stopAfterMovie;

	// Having 2 different variables so that you can compare the two
	private double movieTime; // How long your animation has been running since it was last called
	private double totalTime;

	public Animation() {
		// Refreshing all the variables
		scenes = new ArrayList<OneScene>();
		totalTime = 0;
		start();
	}

	// -Parameters: Image i = The image loaded, long t = the time that the image remains on the screen
	// -Method: Adds a scene to the ArrayList
	// -"synchronised": this method must run first, before all threads can continue to run
	public synchronized void add(Image i, double t) {
		// Sets the length of the animation based on the total amount of [t]s per scene
		totalTime += t;

		// Each picture will be one object, OneScene.
		scenes.add(new OneScene(i, totalTime));
	}

	// Method: Starts the animation from the beginning
	// -"synchronised": this method must run first, before all threads can continue to run
	public synchronized void start() {
		// You start at the beginning of your animation, resets everything to 0.
		movieTime = 0;
		sceneIndex = 0;
	}

	// Method: Changes the scene
	// Parameters: long timePassed = the time that passed from the last update
	public synchronized void update() {
		if (scenes.size() > 1 && playing) {
			// movieTime is the total amount of time passed since the beginning
			// movieTime is the sum of all the timePassed variables
			movieTime += Time.getTickInterval();

			// If the movieTime is going on for too long, restart the animation.
			if (movieTime >= totalTime) {
				if (!stopAfterMovie) {
					start();
				}
			}

			// Whenever you get a picture, run for a certain amount of time then change the scene.
			// -Sets the sceneIndex
			while (movieTime > getScene(sceneIndex).endTime) {
				sceneIndex++;

				if (sceneIndex >= scenes.size()) {
					sceneIndex = scenes.size() - 1;
					break;
				}
			}
		}
	}

	public void startAnimation() {
		playing = true;
	}

	public void stopAnimation() {
		start();
		playing = false;
	}

	public void play() {
		playing = (playing) ? false : true;
	}

	// Method: Gets the animation's current scene(aka. image)
	public synchronized Image getImage() {
		// System.out.println(sceneIndex);
		if (scenes.size() == 0) {
			return null;
		} else {
			return getScene(sceneIndex).pic;
		}
	}

	// Parameters: int x = the scene's index
	private OneScene getScene(int x) {
		return (OneScene) scenes.get(x);
	}

	public void setStopAfterMovie(boolean stopAfterMovie) {
		this.stopAfterMovie = stopAfterMovie;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public static Animation makeAnimation(String ref, int numAnimations, int width, int height, double interval) {
		Animation a = new Animation();
		for (int i = 0; i < numAnimations; i++)
			a.add(ImageHandler.get().getImage(ref, i, 0, width, height), interval);
		return a;
	}

	// -----------PRIVATE INNER CLASS---------------

	private class OneScene {
		Image pic;
		double endTime;

		public OneScene(Image pic, double endTime) {
			this.pic = pic;
			this.endTime = endTime;
		}
	}
}

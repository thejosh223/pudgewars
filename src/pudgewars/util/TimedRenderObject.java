package pudgewars.util;

import java.awt.Image;

import pudgewars.Game;

public class TimedRenderObject {

	// Rendering Info
	public Image i;
	public int x;
	public int y;

	// Killing Info
	public double killTime;

	public TimedRenderObject(Image i, int x, int y, boolean cx, boolean cy, double duration) {
		this.i = i;
		this.x = cx ? x - i.getWidth(null) / 2 : x;
		this.y = cy ? y - i.getHeight(null) / 2 : y;

		this.killTime = Time.timeSinceStart() + duration;
	}

	public void render() {
		Game.s.g.drawImage(i, x, y, null);
	}
}

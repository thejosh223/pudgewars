package pudgewars.components;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.render.GUI;
import pudgewars.util.ImageHandler;

public class BaseStat {

	public final static int MAX_LEVEL = 6;

	public Stats stats;

	public String name;
	public Image[] image;
	// public Image baseImage;
	// public Image modImage;

	private double baseValue;
	private double increment;
	private double cost;

	public int level;

	public BaseStat(Stats s, String name, int imgIndex, double baseValue, double increment, int cost) {
		this.stats = s;
		this.name = name;
		this.baseValue = baseValue;
		this.increment = increment;
		this.cost = cost;

		this.level = 0;

		image = new Image[2];
		image[0] = ImageHandler.get().getImage("stats/stats3", imgIndex, 0, 40, 40);
		image[1] = ImageHandler.get().getImage("stats/stats3", imgIndex, 1, 40, 40);
		// this.baseImage = ImageHandler.get().getImage("stats/stats3", imgIndex, 0, 40, 40);
		// this.modImage = ImageHandler.get().getImage("stats/stats3", imgIndex, 1, 40, 40);
	}

	public final static int XOFF = 3;

	public boolean drawButtons(int x, int y) {
		GUI.partialHorizontalBar(image, x, y, image[0].getWidth(null), image[0].getHeight(null), (double) level / MAX_LEVEL);
		if (GUI.button(null, x, y, image[0].getWidth(null), image[0].getHeight(null))) return true;

		// Game.s.g.drawImage(modImage, x, y, x + XOFF + (int) (23 * level / (double) MAX_LEVEL), y + modImage.getHeight(null), //
		// 0, 0, XOFF + (int) (23 * level / (double) MAX_LEVEL), modImage.getHeight(null), null);
		return false;
	}

	public void setLevel(int level) {
		this.level = level;
		stats.restoreDefaults();
	}

	public int getCost() {
		return (int) (cost * Math.pow(1.5, level));
	}

	public double getValue() {
		return baseValue + increment * level;
	}

	public String getNetString() {
		return "" + level;
	}

	public void setNetString(String s) {
		this.level = Integer.parseInt(s);
	}
}

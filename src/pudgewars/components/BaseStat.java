package pudgewars.components;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.render.GUI;
import pudgewars.util.ImageHandler;

public class BaseStat {

	public Stats stats;

	public String name;
	public Image baseImage;
	public Image modImage;

	public double baseValue;
	public double increment;
	public double cost;

	public int level;

	public BaseStat(Stats s, String name, int imgIndex, double baseValue, double increment, int cost) {
		this.stats = s;
		this.name = name;
		this.baseValue = baseValue;
		this.increment = increment;
		this.cost = cost;

		this.level = 0;

		this.baseImage = ImageHandler.get().getImage("stats", imgIndex, 0, 16, 16);
		this.modImage = ImageHandler.get().getImage("stats", imgIndex, 1, 16, 16);
	}

	public final static int XOFF = 3;

	public boolean drawButtons(int x, int y) {
		if (GUI.button(baseImage, x, y, baseImage.getWidth(null), baseImage.getHeight(null))) return true;
		Game.s.g.drawImage(modImage, x, y, x + XOFF + level, y + modImage.getHeight(null), 0, 0, XOFF + level, modImage.getHeight(null), null);
		return false;
	}

	public void levelUp() {
		level++;
		cost *= 1.4;
	}

	public double getValue() {
		return baseValue + increment * level;
	}
}

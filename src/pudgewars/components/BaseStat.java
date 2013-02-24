package pudgewars.components;

import java.awt.Image;

import pudgewars.util.ImageHandler;

public class BaseStat {

	public String name;
	public Image img;

	public double baseValue;
	public double increment;
	public double cost;

	public int level;

	public BaseStat(String name, int imgIndex, double baseValue, double increment, double cost) {
		this.name = name;
		this.baseValue = baseValue;
		this.increment = increment;
		this.cost = cost;

		this.level = 0;

		this.img = ImageHandler.get().getImage("stats", imgIndex, 0, 16, 16);
	}

	public void levelUp() {
		level++;
	}

	public double getValue() {
		return baseValue + increment * level;
	}
}

package pudgewars.components;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.entities.PudgeEntity;
import pudgewars.render.GUI;
import pudgewars.util.ImageHandler;

public class Stats {
	public PudgeEntity pudge;

	// Inventory Data
	public int experience = 0;
	public boolean isOpen = false;
	public Image[] statImages;

	// Movement Data
	public double moveSpeed = 3.8;

	// Hook Data
	public double hookSize = 1;
	public double hookSpeed = 8;
	public double hookRange = 14;
	public int hookDamage = 4;

	// Player Data
	public int life = 20;
	private int _life;

	public Stats(PudgeEntity p) {
		pudge = p;

		_life = life;

		// Load the Images
		statImages = new Image[CharStat.length];
		for (int i = 0; i < CharStat.length; i++) {
			statImages[i] = ImageHandler.get().getImage("stats", i, 0, 16, 16);
		}
	}

	public void restoreDefaults() {
		pudge.rigidbody.speed = moveSpeed;
	}

	/*
	 * Life
	 */
	public int getMaxLife() {
		return life;
	}

	public int life() {
		return _life;
	}

	public double lifePercentage() {
		return (double) _life / life;
	}

	public void addLife(int a) {
		_life += a;
		if (_life > life) _life = life;
	}

	public void subLife(int a) {
		_life -= a;
		if (_life <= 0) pudge.kill();
	}

	public void onGUI() {
		if (isOpen) {
			for (int i = 0; i < CharStat.length; i++) {
				GUI.button(statImages[i], 10, 10 + i * (16 + 4), statImages[i].getWidth(null), statImages[i].getHeight(null));
			}
		}
	}
}

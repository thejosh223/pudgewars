package pudgewars.components;

import java.awt.Image;

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
	public BaseStat moveSpeed = new BaseStat("MoveSpeed", 0, 3.8, 1, 1);

	// Hook Data
	public BaseStat hookSize = new BaseStat("Hook Size", 5, 1, 0.25, 1);
	public BaseStat hookSpeed = new BaseStat("Hook Speed", 3, 8, 1, 1);
	public BaseStat hookRange = new BaseStat("Hook Range", 4, 14, 2, 1);
	public BaseStat hookDamage = new BaseStat("Hook Damage", 2, 4, 1, 1);

	// Player Data
	public BaseStat life = new BaseStat("Life", 1, 20, 2, 1);
	public BaseStat[] ref;
	private int _life;

	public Stats(PudgeEntity p) {
		pudge = p;

		ref = new BaseStat[CharStat.length];
		ref[0] = life;
		ref[1] = moveSpeed;
		ref[2] = hookSize;
		ref[3] = hookSpeed;
		ref[4] = hookRange;
		ref[5] = hookDamage;

		_life = (int) life.getValue();

		// Load the Images
		statImages = new Image[CharStat.length];
		for (int i = 0; i < CharStat.length; i++) {
			statImages[i] = ImageHandler.get().getImage("stats", i, 0, 16, 16);
		}
	}

	public void restoreDefaults() {
		pudge.rigidbody.speed = moveSpeed.getValue();
	}

	/*
	 * Life
	 */
	public int life() {
		return _life;
	}

	public double lifePercentage() {
		return (double) _life / life.getValue();
	}

	public void addLife(int a) {
		_life += a;
		if (_life > life.getValue()) _life = (int) life.getValue();
	}

	public void subLife(int a) {
		_life -= a;
		if (_life <= 0) pudge.kill();
	}

	public void onGUI() {
		if (isOpen) {
			for (int i = 0; i < CharStat.length; i++) {
				if (GUI.button(ref[i].img, 10, 10 + i * (16 + 4), ref[i].img.getWidth(null), ref[i].img.getHeight(null))) {
					System.out.println("[" + ref[i].name + "] was clicked for : " + pudge.name);
				}
			}
		}
	}
}

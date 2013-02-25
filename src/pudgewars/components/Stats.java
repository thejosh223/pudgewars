package pudgewars.components;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.entities.PudgeEntity;
import pudgewars.util.ImageHandler;

public class Stats {
	public PudgeEntity pudge;

	// Inventory Data
	public int experience = 5;
	public boolean isOpen = false;
	public Image[] statImages;
	public Image[] expImages;

	// Movement Data
	public BaseStat moveSpeed = new BaseStat(this, "MoveSpeed", 0, 3.8, 1, 1);

	// Hook Data
	public BaseStat hookSize = new BaseStat(this, "Hook Size", 5, 1, 0.25, 1);
	public BaseStat hookSpeed = new BaseStat(this, "Hook Speed", 3, 8, 1, 1);
	public BaseStat hookRange = new BaseStat(this, "Hook Range", 4, 14, 2, 1);
	public BaseStat hookDamage = new BaseStat(this, "Hook Damage", 2, 4, 1, 1);

	// Player Data
	public BaseStat life = new BaseStat(this, "Life", 1, 20, 2, 1);
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
		for (int i = 0; i < CharStat.length; i++)
			statImages[i] = ImageHandler.get().getImage("stats", i, 0, 16, 16);
		expImages = new Image[4];
		for (int i = 0; i < 4; i++)
			expImages[i] = ImageHandler.get().getImage("exp_" + i);
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

	public boolean subLife(int a) {
		_life -= a;
		if (_life <= 0) {
			pudge.kill();
			return true;
		}
		return false;
	}

	public void onGUI() {
		// Draw Experience
		int expWidth = expImages[0].getWidth(null);
		int expHeight = expImages[0].getHeight(null);
		int expActual = (int) (((((int) experience) % 10) / 10.0) * expWidth);
		Game.s.g.drawImage(expImages[(int) (experience / 10)], (Game.s.width - expWidth) / 2, Game.s.height - expHeight, expWidth, expHeight, null);
		Game.s.g.drawImage(expImages[(int) (experience / 10) + 1], //
				(Game.s.width - expWidth) / 2, Game.s.height - expHeight, (Game.s.width - expWidth) / 2 + expActual, Game.s.height, //
				0, 0, expActual, expHeight, null);

		if (isOpen) {
			for (int i = 0; i < CharStat.length; i++) {
				if (ref[i].drawButtons(10, 10 + i * (16 + 4))) {
					if (ref[i].cost <= experience) {
						experience -= ref[i].cost;
						ref[i].levelUp();
					}
				}
			}
		}
	}
}

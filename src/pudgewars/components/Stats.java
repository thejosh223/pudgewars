package pudgewars.components;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.entities.PudgeEntity;
import pudgewars.render.GUI;
import pudgewars.util.ImageHandler;

public class Stats {
	public PudgeEntity pudge;

	// Inventory Data
	public int experience = 25;
	public boolean isOpen = false;
	public Image[] statImages;
	public Image[] expImages;
	public Image[] lifeImages;
	public Image[] hookCooldownImages;
	public Image[] grappleCooldownImages;

	// Movement Data
	public BaseStat moveSpeed = new BaseStat(this, "MoveSpeed", 0, 3.8, 1, 1);

	// Hook Data
	public BaseStat hookSize = new BaseStat(this, "Hook Size", 5, 0.75, 0.25, 1);
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

		// Life Images
		lifeImages = new Image[2];
		lifeImages[0] = ImageHandler.get().getImage("HUD_life_empty");
		lifeImages[1] = ImageHandler.get().getImage("HUD_life_full");

		hookCooldownImages = new Image[2];
		hookCooldownImages[0] = ImageHandler.get().getImage("hook_empty");
		hookCooldownImages[1] = ImageHandler.get().getImage("hook_full");
		grappleCooldownImages = new Image[2];
		grappleCooldownImages[0] = ImageHandler.get().getImage("grapple_empty");
		grappleCooldownImages[1] = ImageHandler.get().getImage("grapple_full");
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
		// Draw Life
		GUI.partialHorizontalBar(lifeImages, 10, Game.s.height - 16, lifePercentage());

		// int lifebarWidth = lifeImages[0].getWidth(null);
		// int lifebarHeight = lifeImages[0].getHeight(null);
		// int lifebarActual = (int) (lifeImages[0].getWidth(null) * lifePercentage());
		// Game.s.g.drawImage(lifeImages[0], 10, Game.s.height - 10 - lifebarHeight, 10 + lifebarWidth, Game.s.height - 10, //
		// 0, 0, lifebarWidth, lifebarHeight, null);
		// Game.s.g.drawImage(lifeImages[1], 10, Game.s.height - 10 - lifebarHeight, 10 + lifebarActual, Game.s.height - 10, //
		// 0, 0, lifebarActual, lifebarHeight, null);

		// Hook Cooldown
		GUI.partialHorizontalBar(hookCooldownImages, 10, Game.s.height - 32, (1 - (pudge.hookCooldown / PudgeEntity.HOOK_COOLDOWN)));

		// int hookWidth = hookCooldownImages[0].getWidth(null);
		// int hookHeight = hookCooldownImages[0].getHeight(null);
		// int hookActual = (int) (hookWidth * (1 - (pudge.hookCooldown / PudgeEntity.HOOK_COOLDOWN)));
		// Game.s.g.drawImage(hookCooldownImages[0], 10, Game.s.height - 20 - hookHeight - lifebarHeight, 10 + hookWidth, Game.s.height - 20 - lifebarHeight, //
		// 0, 0, hookWidth, hookHeight, null);
		// Game.s.g.drawImage(hookCooldownImages[1], 10, Game.s.height - 20 - hookHeight - lifebarHeight, 10 + hookActual, Game.s.height - 20 - lifebarHeight, //
		// 0, 0, hookActual, hookHeight, null);

		// Grapple Cooldown
		GUI.partialHorizontalBar(grappleCooldownImages, 10, Game.s.height - 48, (1 - (pudge.grappleCooldown / PudgeEntity.GRAPPLEHOOK_COOLDOWN)));
		// int grappleWidth = grappleCooldownImages[0].getWidth(null);
		// int grappleHeight = grappleCooldownImages[0].getHeight(null);
		// int grappleActual = (int) (grappleWidth * (1 - (pudge.grappleCooldown / PudgeEntity.GRAPPLEHOOK_COOLDOWN)));
		// Game.s.g.drawImage(grappleCooldownImages[0], 10, Game.s.height - 30 - grappleHeight - hookHeight - lifebarHeight, 10 + grappleWidth, Game.s.height - 30 - lifebarHeight - hookHeight, //
		// 0, 0, grappleWidth, grappleHeight, null);
		// Game.s.g.drawImage(grappleCooldownImages[1], 10, Game.s.height - 30 - grappleHeight - hookHeight - lifebarHeight, 10 + grappleActual, Game.s.height - 30 - lifebarHeight - hookHeight, //
		// 0, 0, grappleActual, grappleHeight, null);

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

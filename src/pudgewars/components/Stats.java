package pudgewars.components;

import pudgewars.entities.PudgeEntity;

public class Stats {
	public PudgeEntity pudge;

	public double moveSpeed = 3.8;

	// Hook Data
	public double hookSize = 1;
	public double hookSpeed = 8;
	public double hookRange = 14;
	public int hookDamage = 4;

	public int life = 20;
	private int _life;

	public Stats(PudgeEntity p) {
		pudge = p;

		_life = life;
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
}

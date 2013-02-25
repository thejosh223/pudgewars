package pudgewars.entities;

import pudgewars.Game;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HealingFountainEntity extends LightSourceEntity {

	public final static int HEAL_AMOUNT = 1;
	public final static double HEALING_RADIUS = 3;
	public final static double HEALING_INTERVAL = 1;

	public double interval;

	public HealingFountainEntity(Vector2 position) {
		super(position, Team.freeForAll, 6, 6);
	}

	public void update() {
		if (interval < 0) {
			System.out.println("Magic!");
			for (int i = 0; i < Game.entities.entities.size(); i++) {
				Entity e = Game.entities.entities.get(i);
				if (e.transform.position.distance(transform.position) <= HEALING_RADIUS) {
					if (e instanceof PudgeEntity) ((PudgeEntity) e).stats.addLife(HEAL_AMOUNT);
				}
			}
			interval = HEALING_INTERVAL;
		}
		interval -= Time.getTickInterval();
	}
}

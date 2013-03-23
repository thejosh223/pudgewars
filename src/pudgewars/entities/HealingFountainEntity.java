package pudgewars.entities;

import pudgewars.Game;
import pudgewars.particles.FollowParticle;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HealingFountainEntity extends LightSourceEntity {

	public final static int HEAL_AMOUNT = 1;
	public final static double HEALING_RADIUS = 4;
	public final static double HEALING_INTERVAL = 1;

	public double interval;

	public HealingFountainEntity(Vector2 position) {
		super(position, Team.freeForAll, HEALING_RADIUS * 2, HEALING_RADIUS * 2);
	}

	public void update() {
		if (interval < 0) {
			for (int i = 0; i < Game.entities.entities.size(); i++) {
				Entity e = Game.entities.entities.get(i);
				if (e.transform.position.distance(transform.position) <= HEALING_RADIUS) {
					if (e instanceof PudgeEntity) {
						if (Game.isServer) ((PudgeEntity) e).stats.addLife(HEAL_AMOUNT);
						Game.entities.addParticle(new FollowParticle("sparkle2", 16, 16, 4, e, HEALING_INTERVAL));
					}
				}
			}
			interval = HEALING_INTERVAL;
		}
		interval -= Time.getTickInterval();
	}
}

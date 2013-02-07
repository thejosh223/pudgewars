package pudgewars.util;

import pudgewars.entities.Entity;

public interface BBOwner {
	public void handleCollision(Entity e, double vx, double vy);
}

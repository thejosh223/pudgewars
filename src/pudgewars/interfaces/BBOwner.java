package pudgewars.interfaces;

import pudgewars.entities.Entity;
import pudgewars.level.Tile;

public interface BBOwner {
	public void handleCollision(Entity e, double vx, double vy);

	public void collides(Entity e, double vx, double vy);

	public void collides(Tile t, double vx, double vy);

	public boolean blocks(BBOwner b);

	public boolean shouldBlock(BBOwner b);
}

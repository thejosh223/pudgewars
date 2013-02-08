package pudgewars.entities;

import pudgewars.Game;
import pudgewars.components.Rigidbody;
import pudgewars.components.Transform;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Rotation;
import pudgewars.util.Vector2;

public abstract class Entity implements BBOwner {
	public Transform transform;
	public Rigidbody rigidbody;

	public boolean remove;

	public Entity(Vector2 position) {
		this(position, Vector2.ZERO.clone());
	}

	public Entity(Vector2 position, Vector2 collision) {
		transform = new Transform(this, position, Rotation.ZERO, Vector2.ONE);
		rigidbody = new Rigidbody(this, collision);
	}

	public abstract void update();

	public abstract void render();

	public void onGUI() {
	}

	public void lateUpdate() {
	}

	// public abstract void collides(Entity e);

	public void kill() {
		remove = true;
	}

	public boolean[] isWorldCollision(double tx, double ty) {
		if (Game.map.isCollides(tx, ty, this)) {
			boolean xCol = Game.map.isCollides(tx, transform.position.y, this);
			boolean yCol = Game.map.isCollides(transform.position.x, ty, this);

			return new boolean[] { xCol, yCol };
		}
		return new boolean[] { false, false };
	}

	public Entity isEntityCollision(double tx, double ty) {
		for (int i = 0; i < Game.entities.entities.size(); i++) {
			Entity e = Game.entities.entities.get(i);
			if (e != this) {
				// TODO!
				// if (new Rectangle2D.Double( //
				// tx - rigidbody.collision.x / 2, //
				// ty - rigidbody.collision.y / 2, //
				// rigidbody.collision.x, //
				// rigidbody.collision.y).intersects(e.rigidbody.getCollisionBox())) {
				// return e;
				// }
			}
		}
		return null;
	}

	public void isCollides(Entity e) {
	}

	public double getX() {
		return transform.position.x;
	}

	public double getY() {
		return transform.position.y;
	}

	public void setVerticalMovement(double vx) {
		rigidbody.velocity.x = vx;
	}

	public void setHorizontalMovement(double vy) {
		rigidbody.velocity.y = vy;
	}

	public void setPosition(double x, double y) {
		transform.position.set(x, y);
	}

	/*
	 * Collisions
	 */
	public final boolean blocks(BBOwner b) {
		return b.shouldBlock(this) && this.shouldBlock(b);
	}

	public boolean shouldBlock(BBOwner b) {
		return false;
	}

	public void handleCollision(Entity e, double vx, double vy) {
		if (blocks(e)) {
			this.collides(e, vx, vy);
			e.collides(this, -vx, -vy);
		}
	}

	public void collides(Entity e, double vx, double vy) {
	}

	public void collides(Tile t, double vx, double vy) {
	}

}

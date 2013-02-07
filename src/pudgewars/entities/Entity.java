package pudgewars.entities;

import java.awt.geom.Rectangle2D;

import pudgewars.Game;
import pudgewars.util.Rigidbody;
import pudgewars.util.Rotation;
import pudgewars.util.Transform;
import pudgewars.util.Vector2;

public abstract class Entity {
	public Transform transform;
	public Rigidbody rigidbody;

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

	public abstract void collides(Entity e);

	public abstract void kill();

	public boolean[] isWorldCollision(double tx, double ty) {
		if (Game.map.isCollides(tx, ty, this)) {
			boolean xCol = Game.map.isCollides(tx, transform.position.y, this);
			boolean yCol = Game.map.isCollides(transform.position.x, ty, this);

			return new boolean[] { xCol, yCol };
		}
		return new boolean[] { false, false };
	}

	public Entity isEntityCollision(double tx, double ty) {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e != this) {
				if (new Rectangle2D.Double( //
						tx - rigidbody.collision.x / 2, //
						ty - rigidbody.collision.y / 2, //
						rigidbody.collision.x, //
						rigidbody.collision.y).intersects(e.rigidbody.getCollisionBox())) {
					return e;
				}
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
		// this.vx = vx;
	}

	public void setHorizontalMovement(double vy) {
		rigidbody.velocity.y = vy;
		// this.vy = vy;
	}

	public void setPosition(double x, double y) {
		transform.position.set(x, y);
	}
}

package pudgewars.entities;

import java.awt.geom.Rectangle2D;

import pudgewars.Game;
import pudgewars.util.Physics;
import pudgewars.util.Transform;
import pudgewars.util.Vector2;

public abstract class Entity {
	public Transform transform;
	public Physics rigidbody;

	// protected double x;
	// protected double y;
	protected double vx;
	protected double vy;
	protected double ax;
	protected double ay;
	protected float speed;

	protected double collisionHeight;
	protected double collisionWidth;

	public Entity(double x, double y) {
		transform = new Transform(this, new Vector2(x, y), 0, new Vector2(1, 1));
	}

	public abstract void update();

	public abstract void render();

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
				if (new Rectangle2D.Double(tx - this.collisionWidth / 2, ty - this.collisionHeight / 2, //
						this.collisionWidth, this.collisionHeight).intersects( //
						new Rectangle2D.Double(e.transform.position.x - e.getCollisionWidth() / 2, e.transform.position.y - e.getCollisionHeight() / 2, //
								e.getCollisionWidth(), e.getCollisionHeight()))) {
					return e;
				}
			}
		}
		return null;
	}

	public void setDirection(Vector2 target) {
		setDirection(target, speed);
	}

	public void setDirection(Vector2 target, float speed) {
		double angle = Math.atan2(transform.position.x - target.x, transform.position.y - target.y);
		setVerticalMovement(-speed * Math.sin(angle));
		setHorizontalMovement(-speed * Math.cos(angle));
	}

	public double getX() {
		return transform.position.x;
	}

	public double getY() {
		return transform.position.y;
	}

	public void setVerticalMovement(double vx) {
		this.vx = vx;
	}

	public void setHorizontalMovement(double vy) {
		this.vy = vy;
	}

	public void setPosition(double x, double y) {
		transform.position.set(x, y);
	}

	public double getCollisionHeight() {
		return collisionHeight;
	}

	public void setCollisionHeight(double collisionHeight) {
		this.collisionHeight = collisionHeight;
	}

	public double getCollisionWidth() {
		return collisionWidth;
	}

	public void setCollisionWidth(double collisionWidth) {
		this.collisionWidth = collisionWidth;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}

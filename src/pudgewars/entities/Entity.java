package pudgewars.entities;

import java.awt.geom.Rectangle2D;

import pudgewars.Game;
import pudgewars.util.Vector2;

public abstract class Entity {
	protected double x;
	protected double y;
	protected double vx;
	protected double vy;
	protected double ax;
	protected double ay;
	protected float speed;

	protected double collisionHeight;
	protected double collisionWidth;

	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public abstract void update();

	public abstract void render();

	public abstract void collides(Entity e);

	public abstract void kill();

	public boolean[] isWorldCollision(double tx, double ty) {
		if (Game.map.isCollides(tx, ty, this)) {
			boolean xCol = Game.map.isCollides(tx, y, this);
			boolean yCol = Game.map.isCollides(x, ty, this);

			return new boolean[] { xCol, yCol };
		}
		return new boolean[] { false, false };
	}

	public Entity isEntityCollision(double tx, double ty) {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (e != this) {
				if (new Rectangle2D.Double(tx - this.collisionWidth / 2, ty - this.collisionHeight / 2, this.collisionWidth, this.collisionHeight).intersects(new Rectangle2D.Double(e.x - e.getCollisionWidth() / 2, e.y - e.getCollisionHeight() / 2, e.getCollisionWidth(), e.getCollisionHeight()))) {
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
		double angle = Math.atan2(this.x - target.x, this.y - target.y);
		setVerticalMovement(-speed * Math.sin(angle));
		setHorizontalMovement(-speed * Math.cos(angle));
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setVerticalMovement(double vx) {
		this.vx = vx;
	}

	public void setHorizontalMovement(double vy) {
		this.vy = vy;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
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

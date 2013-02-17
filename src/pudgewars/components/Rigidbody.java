package pudgewars.components;

import java.util.List;

import pudgewars.Game;
import pudgewars.entities.Entity;
import pudgewars.util.CollisionBox;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class Rigidbody {
	public Entity gameObject;
	public Transform transform;

	public Vector2 velocity;
	public Vector2 collision;

	public double speed;

	public boolean physicsSlide = false;

	public Rigidbody(Entity e, Vector2 collision) {
		this.gameObject = e;
		this.transform = gameObject.transform;
		this.collision = collision.clone();

		velocity = Vector2.ZERO.clone();
	}

	public void updateVelocity() {
		velocity.setMagnitude(speed);
		move(velocity.x * Time.getTickInterval(), velocity.y * Time.getTickInterval());
	}

	public void setDirection(Vector2 target) {
		double angle = Math.atan2(gameObject.transform.position.x - target.x, gameObject.transform.position.y - target.y);
		velocity.set(-Math.sin(angle), -Math.cos(angle));
	}

	public void setDirection(Vector2 target, double speed) {
		setDirection(target);
		this.speed = speed;
	}

	/*
	 * Movement
	 */
	public boolean noMove() {
		List<CollisionBox> l = Game.entities.getListCollisionBoxes(this);
		return semiMove(l, 0, 0);
	}

	public boolean move(double vx, double vy) {
		List<CollisionBox> l = Game.entities.getListCollisionBoxes(this);
		if (physicsSlide) {
			boolean moved = false;
			if (!gameObject.remove) moved |= semiMove(l, vx, 0); // Move in X Dir.
			if (!gameObject.remove) moved |= semiMove(l, 0, vy); // Move in Y Dir.
			return moved;
		} else {
			boolean moved = true;
			if (!gameObject.remove) moved &= semiMove(l, vx, 0); // Move in X Dir.
			if (!gameObject.remove) moved &= semiMove(l, 0, vy); // Move in Y Dir.
			return moved;
		}
	}

	protected boolean semiMove(List<CollisionBox> l, double vx, double vy) {
		double ovx = vx;
		double ovy = vy;

		CollisionBox me = getCollisionBox();
		CollisionBox closest = null;
		double movebacker = 0.001;
		for (int i = 0; i < l.size(); i++) {
			CollisionBox them = l.get(i);
			if (me.intersects(them)) {
				them.owner.handleCollision(gameObject, ovx, ovy);
				continue;
			}

			// Horizontal Movement (Along X Direction)
			if (vx != 0) {
				if (me.y1 <= them.y0 || me.y0 >= them.y1) continue;

				if (vx > 0) {
					double xRightDir = them.x0 - me.x1;
					if (xRightDir >= 0 && vx > xRightDir) {
						closest = them;
						vx = xRightDir - movebacker;
						if (vx < 0) vx = 0;
					}
				} else if (vx < 0) {
					double xLeftDir = them.x1 - me.x0;
					if (xLeftDir <= 0 && vx < xLeftDir) {
						closest = them;
						vx = xLeftDir + movebacker;
						if (vx > 0) vx = 0;
					}
				}
			}

			// Vertical Movement (Along Y Direction)
			if (vy != 0) {
				if (me.x1 <= them.x0 || me.x0 >= them.x1) continue;

				if (vy > 0) {
					double yDownDir = them.y0 - me.y1;
					if (yDownDir > 0 && vy > yDownDir) {
						closest = them;
						vy = yDownDir - movebacker;
						if (vy < 0) vy = 0;
					}
				} else if (vy < 0) {
					double yUpDir = them.y1 - me.y0;
					if (yUpDir < 0 && vy < yUpDir) {
						closest = them;
						vy = yUpDir + movebacker;
						if (vy > 0) vy = 0;
					}
				}
			}
		}

		if (closest != null) closest.owner.handleCollision(gameObject, ovx, ovy);

		if (vx != 0 || vy != 0) {
			transform.position.x += vx;
			transform.position.y += vy;

			// Return FALSE if v(x/y) is so small that it doesn't change (x/y) anymore.
			// return !(x - vx == x && y - vy == y);
			return true;
		}
		return false;
	}

	public boolean isMoving() {
		return !(velocity.x == 0 && velocity.y == 0);
	}

	/*
	 * Collision Detection
	 */
	public boolean insersects(Entity e) {
		return e.rigidbody.getCollisionBox().intersects(this.getCollisionBox());
	}

	public boolean intersects(CollisionBox b) {
		return getCollisionBox().intersects(b);
	}

	public CollisionBox getCollisionBox() {
		Vector2 v = Vector2.componentMultiply(collision, transform.scale);
		return new CollisionBox(gameObject, transform.position.x - v.x / 2, transform.position.y - v.y / 2, //
				transform.position.x + v.x / 2, transform.position.y + v.y / 2);
	}

	// TODO
	public void handleCollision(Entity e, double vx, double vy) {
	}
}

package pudgewars.util;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import pudgewars.entities.Entity;

public class Rigidbody {
	public Entity gameObject;
	public Transform transform;

	public Vector2 velocity;
	public Vector2 acceleration;
	public Vector2 collision;

	public double speed;

	public Rigidbody(Entity e, Vector2 collision) {
		this.gameObject = e;
		this.transform = gameObject.transform;
		this.collision = collision.clone();

		velocity = Vector2.ZERO.clone();
		acceleration = Vector2.ZERO.clone();
	}

	public void updateVelocity() {
		velocity.x += acceleration.x * Time.getTickInterval();
		velocity.y += acceleration.y * Time.getTickInterval();
	}

	public void setDirection(Vector2 target) {
		setDirection(target, speed);
	}

	public void setDirection(Vector2 target, double speed) {
		double angle = Math.atan2(gameObject.transform.position.x - target.x, gameObject.transform.position.y - target.y);
		velocity.set(-speed * Math.sin(angle), -speed * Math.cos(angle));
	}

	public Rectangle.Double getCollisionBox() {
		return new Rectangle2D.Double( //
				gameObject.transform.position.x - collision.x / 2, //
				gameObject.transform.position.y - collision.y / 2, //
				collision.x, //
				collision.y);
	}
}

package pudgewars.util;

import pudgewars.entities.Entity;

public class Physics {
	public Entity gameObject;

	public Vector2 velocity;
	public Vector2 acceleration;
	public double radius;

	public Physics(Entity e, double radius) {
		this.gameObject = e;
		this.radius = radius;
	}
}

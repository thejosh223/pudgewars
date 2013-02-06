package pudgewars.util;

import pudgewars.entities.Entity;

public class Transform {
	public Entity gameObject;

	public Vector2 position;
	public double rotation;
	public Vector2 scale;

	public Transform(Entity e) {
		this(e, Vector2.ZERO, 0, Vector2.ZERO);
	}

	public Transform(Entity e, Vector2 position, double rotation, Vector2 scale) {
		this.gameObject = e;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
}

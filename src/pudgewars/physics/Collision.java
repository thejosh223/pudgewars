package pudgewars.physics;

import pudgewars.components.Rigidbody;
import pudgewars.components.Transform;
import pudgewars.entities.Entity;
import pudgewars.util.CollisionBox;

public class Collision {
	public Entity gameObject;
	public Transform transform;
	public Rigidbody rigidbody;
	public CollisionBox collider;

	public Collision(Entity e, Transform t, Rigidbody r, CollisionBox c) {
		this.gameObject = e;
		this.transform = t;
		this.rigidbody = r;
		this.collider = c;
	}
}

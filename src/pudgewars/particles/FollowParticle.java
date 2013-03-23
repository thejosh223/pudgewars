package pudgewars.particles;

import pudgewars.components.Transform;
import pudgewars.entities.Entity;
import pudgewars.util.Vector2;

public class FollowParticle extends Particle {

	public Transform t;
	public Vector2 offset;

	public FollowParticle(String img, int width, int height, int amt, Entity e, double duration, Vector2 offset) {
		super(img, width, height, amt, e.transform.position, duration);
		this.t = e.transform;
		this.offset = offset;
	}

	public FollowParticle(String img, int width, int height, int amt, Entity e, double duration) {
		this(img, width, height, amt, e, duration, Vector2.ZERO.clone());
	}

	public void update() {
		position = t.position.clone();
		super.update();
	}

}

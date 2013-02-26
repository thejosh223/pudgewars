package pudgewars.particles;

import pudgewars.components.Transform;
import pudgewars.entities.Entity;

public class FollowParticle extends Particle {

	public Transform t;

	public FollowParticle(String img, int width, int height, int amt, Entity e, double duration) {
		super(img, width, height, amt, e.transform.position, duration);
		this.t = e.transform;
	}

	public void update() {
		position = t.position.clone();
		super.update();
	}

}

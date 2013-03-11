package pudgewars.particles;

import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class VelocityParticle extends Particle {
	public Vector2 velocity;

	public VelocityParticle(String img, int width, int height, int amt, Vector2 pos, Vector2 vel, double duration) {
		super(img, width, height, amt, pos, duration);
		velocity = vel.clone();
		velocity.scale(-0.2 * Time.getTickInterval());
	}

	public void update() {
		super.update();
		position.add(velocity);
	}
}

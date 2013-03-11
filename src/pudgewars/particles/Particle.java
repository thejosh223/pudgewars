package pudgewars.particles;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.util.Animation;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class Particle {
	public Vector2 position;

	public int duration;
	public boolean remove;

	public Animation ani;

	public Particle(String img, int width, int height, int amt, Vector2 pos, double duration) {
		ani = Animation.makeAnimation(img, amt, width, height, duration / amt);
		ani.startAnimation();
		this.duration = (int) (duration * Time.TICKS_PER_SECOND); // Convert to tick count
		this.position = pos.clone();
	}

	public void update() {
		ani.update();
		duration--;
		if (duration == 0) remove = true;
	}

	public void render() {
		Vector2 v = Game.s.worldToScreenPoint(position);
		Image i = ani.getImage();
		Game.s.g.drawImage(ani.getImage(), (int) v.x - i.getWidth(null) / 2, (int) v.y - i.getHeight(null) / 2, null);
	}
}

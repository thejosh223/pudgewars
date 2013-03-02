package pudgewars.components;

import java.awt.geom.AffineTransform;

import pudgewars.Game;
import pudgewars.entities.Entity;
import pudgewars.util.Rotation;
import pudgewars.util.Vector2;

public class Transform {
	public Entity gameObject;

	public Vector2 position;
	public Vector2 scale;
	public Vector2 drawScale;
	public double rotation;

	public Transform(Entity e) {
		this(e, Vector2.ZERO, Rotation.ZERO, new Vector2(1, 1));
	}

	public Transform(Entity e, Vector2 position) {
		this(e, position, Rotation.ZERO, new Vector2(1, 1));
	}

	public Transform(Entity e, Vector2 position, double rotation, Vector2 scale) {
		this.gameObject = e;
		this.position = position.clone();
		this.rotation = rotation;
		this.scale = scale.clone();
		this.drawScale = new Vector2(1, 1);
	}

	public void rotateTowards(Vector2 target, double amt) {
		double r = -Math.atan2(position.x - target.x, position.y - target.y);
		r = Rotation.clampRotation(r);

		if (r - rotation > Math.PI) r -= Math.PI * 2;
		else if (r - rotation < -Math.PI) r += Math.PI * 2;

		rotation = Rotation.clampRotation(rotation + (r - rotation) * amt);
	}

	public void rotateTowards(Vector2 target) {
		rotation = Rotation.clampRotation(-Math.atan2(position.x - target.x, position.y - target.y));
	}

	public AffineTransform getAffineTransformation() {
		Vector2 v = Game.s.worldToScreenPoint(position);

		AffineTransform a = new AffineTransform();
		a.translate((int) (v.x - (Game.TILE_SIZE * scale.x * drawScale.x) / 2), (int) (v.y - (Game.TILE_SIZE * scale.y * drawScale.x) / 2));
		a.rotate(rotation, (Game.TILE_SIZE * scale.x * drawScale.x) / 2, (Game.TILE_SIZE * scale.y * drawScale.x) / 2);
		a.scale(scale.x, scale.y);

		return a;
	}

	public Vector2 getScreenSize() {
		return new Vector2(Game.TILE_SIZE * scale.x, Game.TILE_SIZE * scale.y);
	}
}

package pudgewars.util;

import java.awt.geom.AffineTransform;

import pudgewars.Game;
import pudgewars.entities.Entity;

public class Transform {
	public Entity gameObject;

	public Vector2 position;
	public double rotation;
	public Vector2 scale;

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
	}

	public AffineTransform getAffineTransformation() {
		Vector2 v = Game.s.worldToScreenPoint(position);

		AffineTransform a = new AffineTransform();
		a.translate((int) (v.x - (Game.TILE_SIZE * scale.x) / 2), (int) (v.y - (Game.TILE_SIZE * scale.y) / 2));
		a.rotate(rotation, (Game.TILE_SIZE * scale.x) / 2, (Game.TILE_SIZE * scale.y) / 2);
		a.scale(scale.x, scale.y);

		return a;
	}

	public Vector2 getScreenSize() {
		return new Vector2(Game.TILE_SIZE * scale.x, Game.TILE_SIZE * scale.y);
	}
}

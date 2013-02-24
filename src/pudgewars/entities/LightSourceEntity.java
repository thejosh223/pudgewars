package pudgewars.entities;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.util.Vector2;

public class LightSourceEntity extends Entity implements LightSource {

	public double width;
	public double height;

	public LightSourceEntity(Vector2 position, Team team, double width, double height) {
		super(position);
		this.team = team;
		this.width = width * Game.TILE_SIZE;
		this.height = height * Game.TILE_SIZE;
	}

	public void update() {
	}

	public void render() {
	}

	public Shape getLightShape() {
		Vector2 v = Game.s.worldToScreenPoint(transform.position);
		v.scale(1.0 / Window.LIGHTMAP_MULT);
		// Rectangle2D rect = new Rectangle2D.Double(v.x - width / 2, v.y - height / 2, width, height);
		Ellipse2D rect = new Ellipse2D.Double(v.x - width / 2, v.y - height / 2, width, height);
		return rect;
	}
}

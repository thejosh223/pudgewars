package pudgewars.entities;

import pudgewars.util.Vector2;

public class LightSourceEntity extends Entity implements LightSource {

	public double radius;

	public LightSourceEntity(Vector2 position, double radius) {
		super(position);
		this.radius = radius;
	}

	public double getLightRadius() {
		return radius;
	}

	public void update() {
	}

	public void render() {
	}

}

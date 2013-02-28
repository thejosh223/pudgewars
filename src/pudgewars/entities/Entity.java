package pudgewars.entities;

import pudgewars.components.Rigidbody;
import pudgewars.components.Transform;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Rotation;
import pudgewars.util.Vector2;

public abstract class Entity implements BBOwner {
	// Basic Components
	public Transform transform;
	public Rigidbody rigidbody;

	// Name
	public String name;

	// Kill Data
	public boolean remove = false;

	// Render Data
	public boolean shouldRender = true;

	// Team Data
	public Team team;

	public Entity(Vector2 position) {
		this(position, Vector2.ZERO.clone());
	}

	public Entity(Vector2 position, Vector2 collision) {
		transform = new Transform(this, position, Rotation.ZERO, Vector2.ONE);
		rigidbody = new Rigidbody(this, collision);

		team = Team.freeForAll;
	}

	public abstract void update();

	public abstract void render();

	public void onGUI() {
	}

	public void lateUpdate() {
	}

	public void kill() {
		remove = true;
	}

	public boolean isTeammate(Entity e) {
		return e.team == team || e.team == Team.freeForAll;
	}

	/*
	 * Collisions
	 */

	public final boolean blocks(BBOwner b) {
		return b.shouldBlock(this) && this.shouldBlock(b);
	}

	public boolean shouldBlock(BBOwner b) {
		return false;
	}

	public void handleCollision(Entity e, double vx, double vy) {
		if (blocks(e)) {
			this.collides(e, vx, vy);
			e.collides(this, -vx, -vy);
		}
	}

	public void collides(Entity e, double vx, double vy) {
	}

	public void collides(Tile t, double vx, double vy) {
	}

	/*
	 * Network
	 */
	public String getNetworkString() {
		return null;
	}

	public void setNetworkString(String s) {
	}
}

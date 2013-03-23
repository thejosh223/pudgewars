package pudgewars.entities;

import pudgewars.Game;
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
	public boolean wasUpdated = false;
	public boolean respawn = false;
	public boolean respawning = false;

	// Render Data
	public boolean shouldRender = true;

	// Team Data
	public Team team;

	// Network Data
	public int networkID;
	public int ClientID = -1;
	public boolean shouldSendNetworkData = false;

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

	public void respawnUpdate() {
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

	public boolean isTangible() {
		return true;
	}

	public final boolean blocks(BBOwner b) {
		if (b.isTangible() && this.isTangible()) {
			return b.shouldBlock(this) && this.shouldBlock(b);
		} else {
			return false;
		}
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
	public int getClientID() {
		return ClientID;
	}

	public void sendNetworkData() {
		if (shouldSendNetworkData) {
			Game.net.sendEntityData(getNetworkString());
			shouldSendNetworkData = false;
		}
	}

	public String getNetworkString() {
		String s = "" + networkID + ":";
		s += transform.position.getNetString();
		s += ":" + rigidbody.velocity.getNetString();
		return s;
	}

	public void setNetworkString(String s) {
		String[] t = s.split(":");
		transform.position.setNetString(t[0]);
		rigidbody.velocity.setNetString(t[1]);
	}
}

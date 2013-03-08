package pudgewars.entities;

import pudgewars.Game;
import pudgewars.entities.hooks.HookEntity;
import pudgewars.entities.hooks.NormalHookEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Animation;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class CowEntity extends Entity {
	public final static double COLLISION_WIDTH = 1;
	public final static double COLLISION_HEIGHT = 1;

	// Hooking
	public boolean canMove;
	public boolean canTileCollide;
	public boolean canEntityCollide;
	public NormalHookEntity attachedHook;

	// Movement
	protected Vector2 target;

	// Rendering
	protected Animation ani;

	public CowEntity(Vector2 position, Team team) {
		super(position, new Vector2(COLLISION_WIDTH, COLLISION_HEIGHT));

		transform.drawScale = new Vector2(2, 2);

		this.team = team;

		canTileCollide = true;
		canEntityCollide = true;
		canMove = true;

		rigidbody.physicsSlide = true;

		ani = Animation.makeAnimation("cow", 8, 32, 32, 0.05);
		ani.startAnimation();

		target = null;
	}

	public void update() {
		if (rigidbody.isMoving()) ani.update();

		rigidbody.updateVelocity();

		if (!canMove) target = null;

		// Target Movement
		if (target != null) {
			transform.rotateTowards(target, 0.1);

			double dist = transform.position.distance(target);
			if (dist < rigidbody.velocity.magnitude() * Time.getTickInterval()) {
				rigidbody.velocity = new Vector2(0, 0);
				target = null;
			} else {
				rigidbody.setDirection(target);
			}
		}
	}

	public void render() {
		if (!shouldRender) return;

		// Draw Pudge
		Game.s.g.drawImage(ani.getImage(), transform.getAffineTransformation(), null);
	}

	/*
	 * Collisions
	 */
	public boolean shouldBlock(BBOwner b) {
		if (b instanceof HookEntity) return true;
		if (b instanceof CowEntity) return true;
		if (b instanceof PudgeEntity) return true;
		if (canTileCollide) {
			if (b instanceof Tile) {
				if (((Tile) b).isPudgeSolid()) return true;
			}
		}
		return false;
	}

	public void collides(Entity e, double vx, double vy) {
	}

	public void kill() {
		if (Game.isServer) {
			System.out.println("Pudge was Killed");
			respawning = true;
			super.kill();
		}
	}

	/*
	 * Network
	 */

	public void sendNetworkData() {
	}

	public String getNetworkString() {
		String s = "COW:";
		s += ClientID + ":";
		s += transform.position.getNetString();
		s += ":" + rigidbody.velocity.getNetString() + ":";
		s += (target == null) ? "null" : target.getNetString();
		s += ":" + team + ":";
		return s;
	}

	public void setNetworkString(String s) {
		wasUpdated = true;
		String[] t = s.split(":");

		transform.position.setNetString(t[2]);
		rigidbody.velocity.setNetString(t[3]);
		if (t[4].equals("null")) {
			target = null;
		} else {
			target = new Vector2();
			target.setNetString(t[4]);
		}
	}
}

package pudgewars.entities;

import java.awt.Image;
import java.util.Random;

import pudgewars.Game;
import pudgewars.entities.hooks.HookEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Animation;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class CowEntity extends HookableEntity {
	public final static double MOVESPEED = 2;

	public final static double COLLISION_WIDTH = 1;
	public final static double COLLISION_HEIGHT = 1;

	protected Image clicker;

	// Movement
	protected Vector2 target;
	private int movementDelay;

	// Rendering
	protected Animation ani;

	public CowEntity(Vector2 position, Team team) {
		super(position, new Vector2(COLLISION_WIDTH, COLLISION_HEIGHT));

		transform.drawScale = new Vector2(2, 2);

		this.team = team;

		rigidbody.physicsSlide = true;

		ani = Animation.makeAnimation("cow", 1, 32, 32, 0.05);
		ani.startAnimation();

		clicker = ImageHandler.get().getImage("selector");

		target = null;
		movementDelay = 0;

		restoreDefaults();
	}

	public void update() {
		if (rigidbody.isMoving()) ani.update();

		if (!canMove) target = null;

		if (Game.isServer) {
			if (movementDelay <= 0) {
				Random r = new Random();
				double theta = Math.toRadians(r.nextInt(360));
				target = Vector2.add(transform.position, new Vector2(Math.sin(theta) * 4, Math.cos(theta) * 4));
				movementDelay = r.nextInt(120);
			} else {
				movementDelay--;
			}
		}

		if (Time.totalTicks % 60 == 0) {
			if (attachedHook != null) {
				System.out.println("NN");
			}
			// System.out.println(">> " + canTileCollide);
		}

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
		} else {
		}

		rigidbody.updateVelocity();
	}

	public void render() {
		if (!shouldRender) return;

		// Draw Pudge
		Game.s.g.drawImage(ani.getImage(), transform.getAffineTransformation(), null);

		// if (target != null) {
		// Vector2 targetLocation = Game.s.worldToScreenPoint(target);
		// AffineTransform a = new AffineTransform();
		// a.translate((int) (targetLocation.x - clicker.getWidth(null) / 2), (int) (targetLocation.y - clicker.getHeight(null) / 2));
		// Game.s.g.drawImage(clicker, a, null);
		// }
	}

	public void restoreDefaults() {
		rigidbody.speed = MOVESPEED;
	}

	/*
	 * Collisions
	 */
	public boolean shouldBlock(BBOwner b) {
		if (b instanceof HookEntity) return true;
		if (b instanceof PudgeEntity || b instanceof CowEntity) {
			return canEntityCollide;
		}
		if (canTileCollide) {
			if (b instanceof Tile) {
				if (((Tile) b).isPudgeSolid()) return true;
			}
		}
		return false;
	}

	public void collides(Entity e, double vx, double vy) {
		super.collides(e, vx, vy);
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

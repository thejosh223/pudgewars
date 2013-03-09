package pudgewars.entities.hooks;

import pudgewars.entities.Entity;
import pudgewars.entities.HookableEntity;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Vector2;

public class NormalHookEntity extends HookEntity {

	public NormalHookEntity(PudgeEntity e, Vector2 target) {
		super(e, "lasso3", target);
		transform.scale = new Vector2(e.stats.hookSize.getValue(), e.stats.hookSize.getValue());
		transform.drawScale = new Vector2(2, 2);
	}

	/*
	 * Pudge Hooking
	 */
	public void attachHookableEntity(HookableEntity e) {
		e.transform.position = transform.position.clone(); // Set the pudge as this position
		if (!isTeammate(e)) {
			if (e instanceof PudgeEntity && ((PudgeEntity) e).stats.subLife(damage)) {
				// Pudge was Killed!
				owner.stats.addExp(2);
				return;
			}
		}
		e.canTileCollide = false;
		// e.canEntityCollide = false;
		e.canMove = false;
		e.attachedHook = this;

		System.out.println("Attached Hook: " + e.getClass());

		hooked = e;
		canHook = false;
		isRotating = false;
	}

	public void detachHookableEntity() {
		System.out.println("Detached!");
		hooked.canTileCollide = true;
		// hooked.canEntityCollide = true;
		hooked.canMove = true;
		hooked.attachedHook = null;
		hooked.restoreDefaults();
		hooked = null;
	}

	/*
	 * Collisions
	 */
	public boolean shouldBlock(BBOwner b) {
		if (b instanceof HookEntity) return false;
		if (b instanceof HookableEntity) {
			if (b == owner) return false;
			else return true;
		}
		if (b instanceof Tile) {
			if (((Tile) b).isHookSolid()) return true;
			else return false;
		}
		return false;
	}

	public void collides(Entity e, double vx, double vy) {
		if (hooked == null && canHook) {
			if (e instanceof HookableEntity) {
				if (e != owner) {
					attachHookableEntity((HookableEntity) e);
					setMovementType(MovementScheme.REVERSE);
				}
			}
		}
	}

	/*
	 * Network
	 */
	public String getNetworkString() {
		String s = "NORMALHOOK:" + owner.ClientID + ":";
		s += (target == null) ? "null" : target.getNetString();
		s += ":" + transform.position.getNetString();
		s += ":" + rigidbody.velocity.getNetString();
		return s;
	}

	public void setNetworkString(String s) {
		String[] t = s.split(":");
		transform.position.setNetString(t[0]);
		rigidbody.velocity.setNetString(t[1]);
	}
}

package pudgewars.entities.hooks;

import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Vector2;

public class NormalHookEntity extends HookEntity {

	public NormalHookEntity(PudgeEntity e, Vector2 target) {
		super(e, "hook", target);
		transform.scale = new Vector2(e.stats.hookSize.getValue(), e.stats.hookSize.getValue());
	}

	/*
	 * Pudge Hooking
	 */
	public void attachPudge(PudgeEntity e) {
		e.transform.position = transform.position.clone(); // Set the pudge as this position
		if (!isTeammate(e)) {
			if (e.stats.subLife(damage)) {
				// Pudge was Killed!
				owner.stats.experience += 2;
				return;
			}
		}
		e.canTileCollide = false;
		e.canMove = false;
		e.attachedHook = this;

		hooked = e;
		canHook = false;
		isRotating = false;
	}

	public void detachPudge() {
		hooked.canTileCollide = true;
		hooked.canMove = true;
		hooked.attachedHook = null;
		hooked = null;
	}

	/*
	 * Collision Detection and Response
	 */
	public boolean shouldBlock(BBOwner b) {
		if (b instanceof HookEntity) return false;
		if (b instanceof PudgeEntity) {
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
			if (e instanceof PudgeEntity) {
				if (e != owner) {
					attachPudge((PudgeEntity) e);
					setMovementType(MovementScheme.REVERSE);
				}
			}
		}
	}
}

package pudgewars.entities.hooks;

import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Vector2;

public class NormalHookEntity extends HookEntity {

	public NormalHookEntity(PudgeEntity e, Vector2 target, boolean specialHook) {
		super(e, target, specialHook);
	}

	/*
	 * Pudge Hooking
	 */
	public void attachPudge(PudgeEntity e) {
		e.attachedHook = this; // Set the hookEntity as this
		e.transform.position = transform.position.clone(); // Set the pudge as
															// this position
		// TODO: If killed, check for null (?)
		e.subLife(damage); // Do some damage
		hooked = e;
		canHook = false;
	}

	public void detachPudge() {
		hooked.attachedHook = null;
		hooked = null;
	}

	/*
	 * Collision Detection and Response
	 */

	// Method Override
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
					setMovementType(HookMovement.REVERSE);
				}
			}
		}
	}
}

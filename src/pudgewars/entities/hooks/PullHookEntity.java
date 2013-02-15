package pudgewars.entities.hooks;

import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class PullHookEntity extends HookEntity {

	public PullHookEntity(PudgeEntity e, Vector2 target) {
		super(e, target);
	}

	/*
	 * Pudge Hooking
	 */
	public void attachPudge(PudgeEntity e) {
		// e.attachedHook = this; // Set the hookEntity as this
		e.transform.position = transform.position.clone(); // Set the pudge as this position
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
		if (b instanceof Tile) {
			if (((Tile) b).isHookSolid()) return true;
			else if (((Tile) b).isHookable()) return true;
			else return false;
		}
		return false;
	}

	public void collides(Tile t, double vx, double vy) {
		if (t.isHookable()) {
			rigidbody.speed = 0;
			setMovementType(MovementScheme.PULL_FORWARD);
		} else {
			setMovementType(MovementScheme.REVERSE);
		}
	}
}

package pudgewars.entities.hooks;

import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Vector2;

public class PullHookEntity extends HookEntity {

	public PullHookEntity(PudgeEntity e, Vector2 target) {
		super(e, "specialhook", target);
	}

	/*
	 * Collision Detection and Response
	 */
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
			owner.canTileCollide = false;
			owner.canMove = false;
			rigidbody.speed = 0;
			isRotating = false;
			setMovementType(MovementScheme.PULL_FORWARD);
		} else {
			setMovementType(MovementScheme.REVERSE);
		}
	}
}

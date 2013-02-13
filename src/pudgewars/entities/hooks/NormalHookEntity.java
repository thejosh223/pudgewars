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
	public boolean shouldBlock(BBOwner b) {
		if (b instanceof HookEntity) return false;
		if (b instanceof PudgeEntity) {
			if (b == owner) return false;
			else return true;
		}
		if (b instanceof Tile) {
			if (((Tile) b).isHookable()) return true;
			else if (((Tile) b).isHookSolid()) return true;
			else return false;
		}
		return false;
	}

	public void collides(Tile t, double vx, double vy) {
		if (vx == 0) {
			rigidbody.velocity.y *= -1;
		} else if (vy == 0) {
			rigidbody.velocity.x *= -1;
		}
		rigidbody.velocity.scale(SLOW_DOWN_VECTOR);
		rigidbody.speed *= SLOW_DOWN_VECTOR;

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.rigidbody.speed = rigidbody.speed;
			temp = temp.getConnected();
		}
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

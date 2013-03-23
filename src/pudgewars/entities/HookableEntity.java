package pudgewars.entities;

import pudgewars.entities.hooks.NormalHookEntity;
import pudgewars.util.Vector2;

public abstract class HookableEntity extends Entity {

	public boolean canMove;
	public boolean canTileCollide;
	public boolean canEntityCollide;

	public NormalHookEntity attachedHook;

	public HookableEntity(Vector2 position, Vector2 collision) {
		super(position, collision);

		canTileCollide = true;
		canEntityCollide = true;
		canMove = true;
	}

	public void restoreDefaults() {
		canTileCollide = true;
		canEntityCollide = true;
		canMove = true;
	}

	public boolean isTangible() {
		return canEntityCollide || canTileCollide;
		// return false;
	}

	public void collides(Entity e, double vx, double vy) {
		// if (e instanceof PudgeEntity) {
		// PudgeEntity p = (PudgeEntity) e;
		// if (p.attachedHook != null) {
		// if (p.attachedHook.owner == this) {
		// p.attachedHook.detachPudge();
		// p.rigidbody.velocity = Vector2.ZERO.clone();
		// }
		// }
		// }
	}
}

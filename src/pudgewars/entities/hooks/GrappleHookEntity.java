package pudgewars.entities.hooks;

import pudgewars.Game;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.Vector2;

public class GrappleHookEntity extends HookEntity {

	public GrappleHookEntity(PudgeEntity e, Vector2 target) {
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
	
	public String getNetworkString() {
		String s = "GRAPPLEHOOK:" + Game.entities.entities.indexOf(owner) + ":";
		s += (target == null) ? "null" : target.getNetString() + ":";
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

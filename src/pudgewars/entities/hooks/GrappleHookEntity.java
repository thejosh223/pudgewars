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

	public void kill() {
		super.kill();
		owner.restoreDefaults();
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
			owner.canEntityCollide = false;
			owner.canMove = false;
			rigidbody.speed = 0;
			isRotating = false;
			setMovementType(MovementScheme.PULL_FORWARD);
		} else {
			setMovementType(MovementScheme.REVERSE);
		}
	}

	public String getNetworkString() {
		String s = "GRAPPLEHOOK:" + owner.ClientID + ":";
		s += (target == null) ? "null" : target.getNetString() + ":";
		s += transform.position.getNetString();
		s += ":" + rigidbody.velocity.getNetString();
		return s;
	}

	public void setNetworkString(String s) {
		wasUpdated = true;
		String[] t = s.split(":");
		if(!Game.isServer){
			transform.position.setNetString(t[0]);
			rigidbody.velocity.setNetString(t[1]);
		}
	}
}

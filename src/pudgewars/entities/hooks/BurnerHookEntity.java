package pudgewars.entities.hooks;

import pudgewars.entities.PudgeEntity;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class BurnerHookEntity extends NormalHookEntity {

	public double dist;

	public BurnerHookEntity(PudgeEntity e, Vector2 target) {
		super(e, target);
	}

	public void update() {
		double xDist = rigidbody.velocity.x * Time.getTickInterval();
		double yDist = rigidbody.velocity.y * Time.getTickInterval();
		double tDist = Math.sqrt(xDist * xDist + yDist * yDist);

		dist += tDist;
		if (dist >= 1) {
			dist -= 1;
			if (hooked instanceof PudgeEntity) {
				((PudgeEntity) hooked).stats.subLife(1);
			}
		}

		super.update();
	}

	public String getNetworkString() {
		String s = "BURNERHOOK:" + owner.ClientID + ":";
		s += (target == null) ? "null" : target.getNetString();
		s += ":" + transform.position.getNetString();
		s += ":" + rigidbody.velocity.getNetString();
		return s;
	}

	public void setNetworkString(String s) {
		wasUpdated = true;
		String[] t = s.split(":");
		transform.position.setNetString(t[0]);
		rigidbody.velocity.setNetString(t[1]);
	}
}

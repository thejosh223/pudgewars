package pudgewars.network;

import java.util.ArrayList;
import java.util.List;

import pudgewars.entities.Entity;
import pudgewars.util.Vector2;

public class Network {
	public List<Vector2> moveTargets;
	public List<Vector2> hookTargets;
	public List<Boolean> isSpecialHook;
	
	public Network() {
		moveTargets = new ArrayList<Vector2>();
		hookTargets = new ArrayList<Vector2>();
		isSpecialHook = new ArrayList<Boolean>();
	}

	public void getEntityData() {
	}

	public void sendEntityData() {
	}
	
	public void sendEntityData(String msg) {
	}
	
	public void sendPudgeEntities(List<Entity> entities) {
	}

	public void getMoveTargets() {
	}

	public void getHookTargets() {
	}

	public void sendMoveTargets() {
	}

	public void sendHookTargets() {
	}

	public void generateEntities() {
	}

	public void sendMoveTarget(Vector2 target) {
	}

	public void sendHookTarget(Vector2 target, boolean isSpecialHook) {
	}
}

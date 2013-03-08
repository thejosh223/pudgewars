package pudgewars.network;

import pudgewars.Game;

public class ClientNetwork extends Network {
	private MyConnection clientConn;

	public ClientNetwork(MyConnection conn) {
		super();
		this.clientConn = conn;
	}

	public void sendEntityData(String msg) {
		System.out.println("ACTION COMMITTED >> " + msg + " sent to server.");
		clientConn.sendMessage(msg);
	}

	public void getEntityData() {
		// format for PUDGE >> controllable:PUDGE:CientID:position:velocity:moveTarget:team:hookTarget:isGrapple:life
		// format for HOOKS >> controllable:HOOKNAME:ownerIndex:target:position:velocity
		String msg;

		while (!(msg = clientConn.getMessage()).equals("EOM")) {
			String t[] = msg.split(":");

			if (t[1].equals("PUDGE")) {
				int pudgeIndex = Game.entities.containsPudge(Integer.parseInt(t[2]));
				if (pudgeIndex == -1) Game.entities.addPudgeEntity(msg);
				else Game.entities.entities.get(pudgeIndex).setNetworkString(msg.substring(msg.indexOf(':') + 1));
			} else if (t[1].equals("NORMALHOOK") || t[1].equals("GRAPPLEHOOK")) {
				int hookIndex = Game.entities.containsHook(Integer.parseInt(t[2]), t[3]);
				if (hookIndex == -1) Game.entities.addHookEntity(msg);
				else Game.entities.entities.get(hookIndex).setNetworkString(t[4] + ":" + t[5]);
			}
		}
		Game.entities.removeUnupdatedEntities();
	}

	public void clearBuffer() {
		String msg;
		while (!(msg = clientConn.getMessage()).equals("EOM"));
	}
}

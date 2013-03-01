package pudgewars.network;

import java.util.ArrayList;
import java.util.List;

import pudgewars.Game;
import pudgewars.entities.Entity;
import pudgewars.util.Vector2;

public class ServerNetwork extends Network {
	public List<MyConnection> serverConn;

	public ServerNetwork(List<ClientNode> clients) {
		super();
		serverConn = new ArrayList<MyConnection>();
		for (int x = 0; x < clients.size(); x++)
			serverConn.add(clients.get(x).getConnection());

		for (int x = 0; x < serverConn.size(); x++) {
			moveTargets.add(null);
			hookTargets.add(null);
			isSpecialHook.add(false);
		}
	}
	
	public void sendEntityData(){
		//System.out.println("Sending Server Data");
		for (int x = 0; x < serverConn.size(); x++) {
			for(int y = 0; y < Game.entities.entities.size(); y++){
				boolean controllable = (x == y) ? true : false;
				serverConn.get(x).sendMessage(y + ":" + Game.entities.entities.get(y).getNetworkString() + ":" + controllable);
				//System.out.println(y + ":" + Game.entities.entities.get(y).getNetworkString() + ":" + controllable);
			}
			serverConn.get(x).sendMessage("EOM");
		}
	}

	public void sendPudgeEntities(List<Entity> entities) {
		for (int x = 0; x < serverConn.size(); x++) {
			for (int y = 0; y < entities.size(); y++) {
				boolean controllable = (x == y) ? true : false;
				serverConn.get(x).sendMessage("GENERATE " + entities.get(y).transform.position.x + " " + entities.get(y).transform.position.y //
						+ " " + entities.get(y).team + " " + controllable);
			}
			serverConn.get(x).sendMessage("EOM");
		}
	}

	public void getMoveTargets() {
		System.out.println("Received move target!");

		for (int i = 0; i < serverConn.size(); i++) {
			moveTargets.add(null);
			String msg = serverConn.get(i).getMessage();
			if (msg.equals("null")) moveTargets.set(i, null);
			else {
				String parts[] = msg.split(" ");
				moveTargets.set(i, new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
			}
		}
	}

	public void getHookTargets() {
		for (int i = 0; i < serverConn.size(); i++) {
			hookTargets.add(null);
			String msg = serverConn.get(i).getMessage();
			if (msg.equals("null")) {
				hookTargets.set(i, null);
				isSpecialHook.set(i, false);
			} else {
				String parts[] = msg.split(" ");
				hookTargets.set(i, new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
				isSpecialHook.set(i, (parts[2].equals("true")) ? true : false);
			}
		}
	}

	public void sendMoveTargets() {
		for (int x = 0; x < serverConn.size(); x++) {
			for (int y = 0; y < serverConn.size(); y++) {
				if (moveTargets.get(y) != null) serverConn.get(x).sendMessage(moveTargets.get(y).x + " " + moveTargets.get(y).y);
				else serverConn.get(x).sendMessage("null");
			}
			serverConn.get(x).sendMessage("EOM");
		}
	}

	public void sendHookTargets() {
		for (int x = 0; x < serverConn.size(); x++) {
			for (int y = 0; y < serverConn.size(); y++) {
				if (hookTargets.get(y) != null) serverConn.get(x).sendMessage(hookTargets.get(y).x + " " + hookTargets.get(y).y + " " + isSpecialHook.get(y));
				else serverConn.get(x).sendMessage("null");
			}
			serverConn.get(x).sendMessage("EOM");
		}
	}
}

package pudgewars.network;

import java.util.ArrayList;
import java.util.List;

import pudgewars.Game;

public class ServerNetwork extends Network {
	public List<MyConnection> serverConn;

	public ServerNetwork(List<ClientNode> clients) {
		super();
		serverConn = new ArrayList<MyConnection>();
		for (int x = 0; x < clients.size(); x++)
			serverConn.add(clients.get(x).getConnection());
	}

	public void sendEntityData() {
		for (int x = 0; x < serverConn.size(); x++) {
			for (int y = 0; y < Game.entities.entities.size(); y++) {
				boolean controllable = (x == Game.entities.entities.get(y).getClientID()) ? true : false;
				serverConn.get(x).sendMessage(controllable + ":" + Game.entities.entities.get(y).getNetworkString());
				System.out.println("SENDING SERVER ENTITY DATA >> " + y + ":" + Game.entities.entities.get(y).getNetworkString() + ":" + controllable);
			}
			serverConn.get(x).sendMessage("EOM");
		}
	}

	public void getEntityData(String msg) {
		// format for PUDGE >> PUDGE:CientID:position:velocity:moveTarget:team:hookTarget:isGrapple:life
		String t[] = msg.split(":");
		int pudgeIndex = Game.entities.containsPudge(Integer.parseInt(t[1]));
		if (pudgeIndex != -1) Game.entities.entities.get(pudgeIndex).setNetworkString(msg);
	}
}

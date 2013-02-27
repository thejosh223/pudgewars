package pudgewars.entities;

import java.util.Vector;

import pudgewars.components.Network;
import pudgewars.network.ClientNode;
import pudgewars.util.Vector2;

public class ServerEntityManager extends EntityManager{
	public ServerEntityManager(Vector<ClientNode> clients){
		super();
		int x = 0, y = 0;
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getTeam() == 0)
				entities.add(new PudgeEntity(new Vector2(4, 8 * x++ + 4),
						Team.leftTeam));
			else
				entities.add(new PudgeEntity(new Vector2(20, 8 * y++ + 4),
						Team.rightTeam));
		}
	}
	
	public void sendServerEntities(Vector<Network> clients) {
		for (int x = 0; x < clients.size(); x++) {
			for (int y = 0; y < entities.size(); y++) {
				boolean controllable = (x == y) ? true : false;
				clients.get(x).sendServerPudgeEntity(
						new Vector2(entities.get(y).getX(), entities.get(y)
								.getY()), entities.get(y).team, controllable);
			}
			clients.get(x).sendMessage("EOM");
		}
	}
}

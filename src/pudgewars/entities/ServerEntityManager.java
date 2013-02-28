package pudgewars.entities;

import java.util.List;

import pudgewars.Game;
import pudgewars.network.ClientNode;
import pudgewars.util.Vector2;

public class ServerEntityManager extends EntityManager {
	public ServerEntityManager(List<ClientNode> clients) {
		super();

		// Add the Pudge Entities to the list
		int x = 0, y = 0;
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getTeam() == 0) entities.add(new PudgeEntity(new Vector2(4, 8 * x++ + 4), Team.leftTeam));
			else entities.add(new PudgeEntity(new Vector2(20, 8 * y++ + 4), Team.rightTeam));
		}
	}

	public void sendPudgeEntities() {
		Game.net.sendPudgeEntities(entities);
	}

	public void updateEntities() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).update();
	}
}

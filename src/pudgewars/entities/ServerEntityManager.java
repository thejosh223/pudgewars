package pudgewars.entities;

import java.util.List;

import pudgewars.network.ClientNode;
import pudgewars.util.Vector2;

public class ServerEntityManager extends EntityManager {
	public ServerEntityManager(List<ClientNode> clients) {
		super();

		// Add the Pudge Entities
		int x = 0, y = 0;
		for (int i = 0; i < clients.size(); i++) {
			PudgeEntity p;
			if (clients.get(i).getTeam() == 0) p = new PudgeEntity(new Vector2(4, 8 * x++ + 4), Team.leftTeam);
			else p = new PudgeEntity(new Vector2(20, 8 * y++ + 4), Team.rightTeam);
			p.ClientID = i;
			entities.add(p);
		}

		// Add Cow Entities
		

		map.addLightSources(entities);
	}

	// public void sendPudgeEntities() {
	// Game.net.sendPudgeEntities(entities);
	// }

	public void updateEntities() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).update();
	}

	public void respawn() {
	}
}

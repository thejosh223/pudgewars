package pudgewars.entities;

import java.util.List;

import pudgewars.Game;
import pudgewars.network.ClientNode;
import pudgewars.util.Vector2;

public class ServerEntityManager extends EntityManager {
	public ServerEntityManager(List<ClientNode> clients) {
		super();

		// Add the Pudge Entities
		int x = 0, y = 0;
		for (int i = 0; i < clients.size(); i++) {
			PudgeEntity p;
			if (clients.get(i).getTeam() == 0) {
				p = new PudgeEntity(new Vector2(4, 8 * x + 4), Team.leftTeam, 2 * x++);
				// p.ClientID = 2 * x++;
			} else {
				p = new PudgeEntity(new Vector2(20, 8 * y + 4), Team.rightTeam, 2 * y++ + 1);
				// p.ClientID = 2*y++ + 1;
			}
			clients.get(i).setID(p.ClientID);
			p.name = clients.get(i).getName();

			entities.add(p);
		}

		// Add Cow Entities
		x = 0;
		y = 0;
		for (int i = 0; i < clients.size(); i++) {
			CowEntity c;
			if (clients.get(i).getTeam() == 0) {
				c = new CowEntity(new Vector2(6, 8 * x++ + 4), Team.noTeam);
			} else {
				c = new CowEntity(new Vector2(22, 8 * y++ + 4), Team.noTeam);
			}
			c.ClientID = i;
			entities.add(c);
		}

		map.addLightSources(entities);
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

	public void respawn() {
	}
}

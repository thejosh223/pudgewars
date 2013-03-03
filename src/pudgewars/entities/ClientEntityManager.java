package pudgewars.entities;

import pudgewars.Game;
import pudgewars.util.Vector2;

public class ClientEntityManager extends EntityManager {
	public ClientEntityManager() {
		super();
	}

	public void addClientEntity(String msg) {
		System.out.println("GENERATE! " + msg);
		String t[] = msg.split(":");
		String u[] = t[2].split(" ");
		Vector2 position = new Vector2(Float.parseFloat(u[0]), Float.parseFloat(u[1]));
		
		Team team = (t[5].equals("leftTeam")) ? Team.leftTeam : Team.rightTeam;
		boolean controllable = (t[6].equals("true")) ? true : false;
		PudgeEntity pudge = new PudgeEntity(position, team);
		
		if (controllable) {
			player = pudge;
			player.controllable = true;
		}
		entities.add(pudge);
	}

	public void generateClientEntities() {
		Game.net.getEntityData();
		map.addLightSources(entities);
	}

	public void updateEntities() {
		map.update();
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).update();
	}
}

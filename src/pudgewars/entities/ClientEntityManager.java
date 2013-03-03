package pudgewars.entities;

import pudgewars.Game;
import pudgewars.entities.hooks.GrappleHookEntity;
import pudgewars.entities.hooks.NormalHookEntity;
import pudgewars.util.Vector2;

public class ClientEntityManager extends EntityManager {
	public ClientEntityManager() {
		super();
	}

	public void addPudgeEntity(String msg) {
		System.out.println("GENERATE! " + msg);
		String t[] = msg.split(":");
		String u[] = t[2].split(" ");
		Vector2 position = new Vector2(Float.parseFloat(u[0]), Float.parseFloat(u[1]));
		
		Team team = (t[4].equals("leftTeam")) ? Team.leftTeam : Team.rightTeam;
		boolean controllable = (t[8].equals("true")) ? true : false;
		PudgeEntity pudge = new PudgeEntity(position, team);
		
		if (controllable) {
			player = pudge;
			player.controllable = true;
		}
		entities.add(pudge);
	}
	
	public void addHookEntity(String msg) {
		String t[] = msg.split(":");
		String[] u = t[3].split(" ");
		Vector2 click = new Vector2(Float.parseFloat(u[0]),Float.parseFloat(u[1]));
		Entity p = Game.entities.entities.get(Integer.parseInt(t[2]));
		Entity e = (t[1].equals("NORMALHOOK")) ? new NormalHookEntity((PudgeEntity)p, click) : new GrappleHookEntity((PudgeEntity)p, click);
		Game.entities.entities.add(e);
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

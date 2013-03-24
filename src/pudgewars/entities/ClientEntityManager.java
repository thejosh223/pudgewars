package pudgewars.entities;

import pudgewars.Game;
import pudgewars.entities.hooks.BurnerHookEntity;
import pudgewars.entities.hooks.GrappleHookEntity;
import pudgewars.entities.hooks.NormalHookEntity;
import pudgewars.util.Vector2;

public class ClientEntityManager extends EntityManager {
	public ClientEntityManager() {
		super();
	}

	public void addPudgeEntity(String msg) {
		System.out.println("PUDGE GENERATED! >> " + msg);
		String t[] = msg.split(":");
		String u[] = t[3].split(" ");
		Vector2 position = new Vector2(Float.parseFloat(u[0]), Float.parseFloat(u[1]));

		Team team = (t[6].equals("leftTeam")) ? Team.leftTeam : Team.rightTeam;

		boolean controllable = (t[0].equals("true")) ? true : false;
		PudgeEntity pudge = new PudgeEntity(position, team, Integer.parseInt(t[2]));
		pudge.name = t[10];
		pudge.wasUpdated = true;

		if (controllable) {
			player = pudge;
			player.controllable = true;
		}
		entities.add(pudge);

		int pudgeIndex = Game.entities.getIndexByClientID(Game.entities.respawnEntities, pudge.ClientID, PudgeEntity.class);
		if (pudgeIndex != -1) Game.entities.respawnEntities.remove(pudgeIndex);
	}

	public void addCowEntity(String msg) {
		System.out.println("COW GENERATED! >> " + msg);
		String t[] = msg.split(":");
		Vector2 position = new Vector2(t[3]);

		// Team team = (t[6].equals("leftTeam")) ? Team.leftTeam : Team.rightTeam;
		// boolean controllable = (t[0].equals("true")) ? true : false;

		CowEntity c = new CowEntity(position, Team.noTeam);
		c.ClientID = Integer.parseInt(t[2]);
		c.wasUpdated = true;

		entities.add(c);
	}

	public void addHookEntity(String msg) {
		System.out.println("HOOK GENERATED! >> " + msg);
		String t[] = msg.split(":");
		String[] u = t[3].split(" ");

		Vector2 click = new Vector2(Float.parseFloat(u[0]), Float.parseFloat(u[1]));
		Entity p = Game.entities.entities.get(getIndexByClientID(Game.entities.entities, Integer.parseInt(t[2]), PudgeEntity.class));

		Entity e = null;
		if (t[1].equals("NORMALHOOK")) {
			e = new NormalHookEntity((PudgeEntity) p, click);
		} else if (t[1].equals("GRAPPLEHOOK")) {
			e = new GrappleHookEntity((PudgeEntity) p, click);
		} else if (t[1].equals("BURNERHOOK")) {
			e = new BurnerHookEntity((PudgeEntity) p, click);
		}
		e.wasUpdated = true;
		Game.entities.entities.add(e);
	}

	public void generateClientEntities() {
		Game.net.getEntityData();
		map.addLightSources(entities);
	}

	public void removeUnupdatedEntities() {
		Game.showRespawningScreen = true;
		for (int x = 0; x < entities.size(); x++) {
			Entity e = Game.entities.entities.get(x);
			if (e instanceof PudgeEntity) {
				if (((PudgeEntity) e).controllable) Game.showRespawningScreen = false;
				if (!e.wasUpdated) {
					e.remove = true;
					Game.entities.respawnEntities.add(e);
				} else e.wasUpdated = false;
			} 
			//else if (e instanceof HookEntity) {
			//	if (!e.wasUpdated) {
			//		e.remove = true;
			//		e.kill();
			//	} else e.wasUpdated = false;
			//}
		}
	}

	public void updateEntities() {
		map.update();
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).update();
	}

	public void sendNetworkData() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).sendNetworkData();
	}
}

package pudgewars.entities;

import pudgewars.Game;
import pudgewars.util.Vector2;

public class ClientEntityManager extends EntityManager{
	public ClientEntityManager(){
		super();
	}
	
	public void addClientEntity(Vector2 position, Team team, boolean controllable) {
		PudgeEntity pudge = new PudgeEntity(position, team);
		if (controllable) {
			player = pudge;
			player.controllable = true;
		}
		entities.add(pudge);
	}
	
	public void generateClientEntities() {
		Game.net.generateEntities();
		map.addLightSources(entities);
	}
	
	public void updateEntities(){
		map.update();
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).update();
	}
}

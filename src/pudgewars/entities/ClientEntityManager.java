package pudgewars.entities;

import pudgewars.components.Network;
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
	
	public void generateClientEntities(Network client) {
		String msg = client.getMessage();
		while (!msg.equals("EOM")) {
			System.out.println(msg);
			String parts[] = msg.split(" ");
			Vector2 position = new Vector2(Float.parseFloat(parts[1]),
					Float.parseFloat(parts[2]));
			Team team = (parts[3].equals("leftTeam")) ? Team.leftTeam
					: Team.rightTeam;
			boolean controllable = (parts[4].equals("true")) ? true : false;
			addClientEntity(position, team, controllable);
			msg = client.getMessage();
		}
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

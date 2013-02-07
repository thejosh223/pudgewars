package pudgewars.entities;

import java.util.ArrayList;
import java.util.List;

import pudgewars.Game;
import pudgewars.level.Map;
import pudgewars.util.Vector2;

public class EntityManager {

	public List<Entity> entities;
	public PudgeEntity player;
	public Map map;

	public EntityManager() {
		entities = new ArrayList<Entity>();
		player = new PudgeEntity(new Vector2(4, 4));
		player.controllable = true;
		entities.add(player);
		entities.add(new PudgeEntity(new Vector2(4, 12)));

		map = Game.map;
	}

	public void updateEntities() {
		map.update();
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	public void lateUpdateEntities() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).lateUpdate();
		}
	}

	public void render() {
		map.render();
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render();
		}
	}

	public void renderGUI() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).onGUI();
		}
	}
}

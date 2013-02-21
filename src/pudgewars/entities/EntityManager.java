package pudgewars.entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.components.Rigidbody;
import pudgewars.level.Map;
import pudgewars.util.CollisionBox;
import pudgewars.util.Vector2;

public class EntityManager {

	public List<Entity> entities;
	public PudgeEntity player;
	public Map map;

	public EntityManager() {
		entities = new ArrayList<Entity>();
		player = new PudgeEntity(new Vector2(4, 4), Team.leftTeam);
		player.controllable = true;
		entities.add(player);
		entities.add(new PudgeEntity(new Vector2(4, 12), Team.leftTeam));
		entities.add(new PudgeEntity(new Vector2(20, 4), Team.rightTeam));
		entities.add(new PudgeEntity(new Vector2(20, 12), Team.rightTeam));

		map = Game.map;
		map.addLightSources(entities);
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

	public void killUpdateEntities() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).remove) {
				entities.remove(i);
				i--;
			}
		}
	}

	public void render() {
		map.render();
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render();
		}
		map.postRender();
		renderLightmap();
	}

	public void renderGUI() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).onGUI();
		}
	}

	public void renderLightmap() {
		// int lightmapMultiple = 1;

		BufferedImage lightMap = new BufferedImage(Game.s.width / Window.LIGHTMAP_MULT, Game.s.height / Window.LIGHTMAP_MULT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) lightMap.getGraphics();

		// Init Drawing
		g.setColor(Color.black);
		g.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());

		g.setComposite(AlphaComposite.Clear);

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof LightSource) {
				if (e.team == Team.freeForAll || e.team == player.team) {
					g.fill(((LightSource) e).getLightShape());
				}
			}
		}

		Game.s.g.drawImage(lightMap, 0, 0, Game.s.width, Game.s.height, null);
	}

	/*
	 * GetEntities Function by CollisionBox
	 * -Returns a list of entities that intersect with the given box
	 * -NOTE: Does not take into consideration blocks()
	 */
	// public List<Entity> getEntitiesFromBB(CollisionBox b) {
	// List<Entity> l = new ArrayList<Entity>();
	// for (Entity e : entities) {
	// if (e.intersects(b)) {
	// l.add(e);
	// }
	// }
	// return l;
	// }

	/*
	 * GetEntities Function by Entity
	 * -Returns a list of entities that intersect with the Entity's
	 * Bounding Box grown by 1.
	 * -Only returns a list that is in blocks() && != entity
	 * -Used for semiMove(vx, vy) in Entity.
	 */
	public List<CollisionBox> getListCollisionBoxes(Rigidbody r) {
		CollisionBox b = r.getCollisionBox().grow(1);
		// List<CollisionBox> l = new ArrayList<CollisionBox>();
		List<CollisionBox> l = map.getCollisionBoxes(r);
		for (Entity e : entities) {
			if (e != r.gameObject && e.rigidbody.intersects(b) && b.owner.blocks(e)) {
				l.add(e.rigidbody.getCollisionBox());
			}
		}
		return l;
	}
}

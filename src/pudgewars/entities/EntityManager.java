package pudgewars.entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import pudgewars.Game;
import pudgewars.ServerGame;
import pudgewars.Window;
import pudgewars.components.Rigidbody;
import pudgewars.level.Map;
import pudgewars.particles.FollowParticle;
import pudgewars.particles.Particle;
import pudgewars.particles.ParticleTypes;
import pudgewars.util.CollisionBox;
import pudgewars.util.Vector2;

public abstract class EntityManager {

	public List<Entity> entities;
	public List<Particle> particles;
	public PudgeEntity player;
	public Map map;

	public EntityManager() {
		entities = new ArrayList<Entity>();
		particles = new ArrayList<Particle>();
		map = Game.map;
	}

	public void addParticle(ParticleTypes p, Entity e, Vector2 pos, double duration) {
		switch (p) {
			case SPARKLE:
				particles.add(new Particle("sparkle2", 16, 16, 4, pos.clone(), duration));
				break;
			case FOLLOW_SPARKLE:
				particles.add(new FollowParticle("sparkle2", 16, 16, 4, e, duration));
				break;
			case DIE:
				particles.add(new FollowParticle("damage", 16, 16, 2, e, duration));
				break;
		}
	}

	public abstract void updateEntities();

	public void lateUpdateEntities() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).lateUpdate();
		}
	}

	public void killUpdateEntities() {
		for (int i = 0; i < entities.size(); i++)
			if (entities.get(i).remove) {
				entities.remove(i);
				i--;
			}

		for (int i = 0; i < particles.size(); i++)
			if (particles.get(i).remove) {
				particles.remove(i);
				i--;
			}
	}

	public void render() {
		map.render();

		// Init shouldRender = false
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).shouldRender = true;

		for (int i = 0; i < entities.size(); i++) {
			Vector2 v = Game.s.worldToScreenPoint(entities.get(i).transform.position);
			for (int o = 0; o < entities.size(); o++) {
				Entity e = entities.get(o);
				if (e instanceof LightSource) {
					// if (e.team == Team.freeForAll || e.team == player.team) {
					// if (((LightSource) e).getLightShape().contains(v.x, v.y)) {
					// entities.get(i).shouldRender = true;
					// }
					// }
				}
			}
		}

		for (int i = 0; i < entities.size(); i++)
			entities.get(i).render();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).render();

		map.postRender();
		// renderLightmap();
	}

	public void renderGUI() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).onGUI();
		}
		map.onGUI();
	}

	public void renderLightmap() {
		BufferedImage lightMap = new BufferedImage(Game.s.width / Window.LIGHTMAP_MULT, Game.s.height / Window.LIGHTMAP_MULT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) lightMap.getGraphics();

		// Init Drawing
		g.setColor(new Color(0, 0, 0, 0.5f));
		// g.setColor(Color.black);
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

	public List<CollisionBox> getEntityListCollisionBoxes(Vector2 v) {
		List<CollisionBox> l = new ArrayList<CollisionBox>();
		for (Entity e : entities)
			if (e.rigidbody.intersects(v)) l.add(e.rigidbody.getCollisionBox());
		return l;
	}

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

	public void addClientEntity(String msg) {
	}

	public void generateClientEntities() {
	}

	public void sendPudgeEntities() {
	}
}

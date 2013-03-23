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
import pudgewars.particles.FollowParticle;
import pudgewars.particles.Particle;
import pudgewars.particles.ParticleTypes;
import pudgewars.util.CollisionBox;
import pudgewars.util.Vector2;

public abstract class EntityManager {

	public List<Entity> entities;
	public List<Entity> respawnEntities;
	public List<Particle> particles;
	public PudgeEntity player;
	public Map map;

	public EntityManager() {
		entities = new ArrayList<Entity>();
		respawnEntities = new ArrayList<Entity>();
		particles = new ArrayList<Particle>();
		map = Game.map;
	}

	public void addParticle(Particle p) {
		particles.add(p);
	}

	public void addParticle(ParticleTypes p, Entity e, Vector2 pos, double duration) {
		switch (p) {
			case SPARKLE:
				particles.add(new Particle("sparkle2", 16, 16, 4, pos, duration));
				break;
			case DIE:
				particles.add(new FollowParticle("damage", 16, 16, 2, e, duration));
				break;
			case DUST:
				particles.add(new Particle("dust", 3, 3, 1, pos, duration));
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
				if (entities.get(i).respawning) respawnEntities.add(entities.get(i));
				entities.remove(i);
				i--;
			}

		for (int i = 0; i < particles.size(); i++)
			if (particles.get(i).remove) {
				particles.remove(i);
				i--;
			}
	}

	public void updateRespawnEntities() {
		for (int i = 0; i < respawnEntities.size(); i++) {
			respawnEntities.get(i).respawnUpdate();
			if (respawnEntities.get(i).respawn) {
				respawnEntities.get(i).respawn = false;
				respawnEntities.get(i).respawning = false;
				entities.add(respawnEntities.get(i));
				respawnEntities.remove(i);
				i--;
			}
		}
	}

	public void render() {
		map.render();

		// Init shouldRender = false
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).shouldRender = true;
		// entities.get(i).shouldRender = false;

		for (int i = 0; i < entities.size(); i++) {
			Entity other = entities.get(i);
			Vector2 v = Game.s.worldToScreenPoint(other.transform.position);
			for (int o = 0; o < entities.size(); o++) {
				Entity lightSourceTemp = entities.get(o);
				if (player.isTeammate(lightSourceTemp) && lightSourceTemp instanceof LightSource) {
					if (((LightSource) lightSourceTemp).getLightShape().contains(v.x, v.y)) {
						other.shouldRender = true;
					}
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
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).onGUI();
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

	/*
	 * Searching Functions
	 */
	@SuppressWarnings("rawtypes")
	public int getIndexByClientID(List<Entity> list, int clientID, Class c) {
		for (int x = 0; x < list.size(); x++) {
			if (list.get(x).getClass().equals(c)) {
				if (list.get(x).ClientID == clientID) {
					return x;
				}
			}
		}
		return -1;
	}

	public int containsHook(int ownerIndex, String click) {
		String[] u = click.split(" ");
		Vector2 target = new Vector2(Float.parseFloat(u[0]), Float.parseFloat(u[1]));
		for (int x = 0; x < entities.size(); x++) {
			String net = entities.get(x).getNetworkString();
			String[] t = net.split(":");
			if (t[0].equals("NORMALHOOK") || t[0].equals("GRAPPLEHOOK") || t[0].equals("BURNERHOOK")) {
				u = t[2].split(" ");
				if (Integer.parseInt(t[1]) == ownerIndex && target.x == Float.parseFloat(u[0]) && target.y == Float.parseFloat(u[1])) return x;
			}
		}
		return -1;
	}

	public void addPudgeEntity(String msg) {
	}

	public void addCowEntity(String msg) {
	}

	public void addHookEntity(String msg) {
	}

	public void generateClientEntities() {
	}

	public void sendPudgeEntities() {
	}

	public void sendNetworkData() {
	}

	public void respawn() {
	}

	public void removeUnupdatedEntities() {
	}
}

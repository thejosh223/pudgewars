package pudgewars.level;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.components.Rigidbody;
import pudgewars.entities.Entity;
import pudgewars.entities.HealingFountainEntity;
import pudgewars.entities.LightSourceEntity;
import pudgewars.entities.PudgeEntity;
import pudgewars.entities.Team;
import pudgewars.entities.hooks.HookEntity;
import pudgewars.render.GUI;
import pudgewars.util.CollisionBox;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.TimedRenderObject;
import pudgewars.util.Vector2;

public class Map {
	public final static int MAP_WIDTH = 30;
	public final static int MAP_HEIGHT = 30;
	public final static int DIVISION_WIDTH = 2; // This should always be even.

	// Map Scrolling Constants
	public final static double SCROLLBAR_X = (1.0 / 4) * 0.25;
	public final static double SCROLLBAR_Y = (1.0 / 3) * 0.25;
	public final static double SCROLL_SPEED = 10; // tiles / second

	// Map Updates
	public final static int TILEUPDATES_PERTICK = 32; // # of tiles updated per
														// tick
	// Controls
	public boolean mouseScrollingEnabled = true;

	// Map Data
	private Tile[][] map = new Tile[MAP_HEIGHT][MAP_WIDTH];
	private int[][] mapData = new int[MAP_HEIGHT][MAP_WIDTH];

	// Minimap
	// private Image minimapBase;
	// private BufferedImage minimap;

	public Image[] respawnImages;
	public double respawnInterval = PudgeEntity.RESPAWN_INTERVAL;

	// Scores
	int leftTeamScore;
	int leftTeamCount;
	int rightTeamScore;
	int rightTeamCount;

	public Map() {
		Random r = new Random();

		/*
		 * Level
		 */
		int hookableIndex = 0;

		// Set Grass, Boundaries, Hookables
		for (int i = 0; i < map.length; i++)
			for (int o = 0; o < map[0].length; o++)
				if (i == 0) map[i][o] = Tile.TREE[2];
				else if (o == 0) map[i][o] = Tile.TREE[1];
				else if (i == MAP_HEIGHT - 1) map[i][o] = Tile.TREE[0];
				else if (o == MAP_WIDTH - 1) map[i][o] = Tile.TREE[3];
				else if ((i == (int) (MAP_HEIGHT * 0.25) || i == (int) (MAP_HEIGHT * 0.75) || i == (int) (MAP_HEIGHT * 0.50)) //
						&& (o == (int) (MAP_WIDTH * 0.25) || o == (int) (MAP_WIDTH * 0.75))) {
					map[i][o] = Tile.HOOKABLE[hookableIndex];
					hookableIndex = (hookableIndex + 1) % Tile.HOOKABLE.length;
				} else {
					map[i][o] = Tile.GRASS[r.nextInt(Tile.GRASS.length)];
					// map[i][o] = Tile.NEWGRASS;
				}

		int o1 = (MAP_WIDTH - DIVISION_WIDTH) / 2 - 1;
		int o2 = (MAP_WIDTH + DIVISION_WIDTH) / 2;
		for (int i = 0; i < map.length; i++) {
			map[i][o1] = Tile.T_Mound;
			for (int o = o1 + 1; o < o2; o++)
				map[i][o] = Tile.WATER;
			map[i][o2] = Tile.T_Mound;
		}

		// Fountain
		map[MAP_HEIGHT / 2 - 1][MAP_WIDTH / 2 - 1] = Tile.FOUNTAIN[0];
		map[MAP_HEIGHT / 2 - 1][MAP_WIDTH / 2] = Tile.FOUNTAIN[1];
		map[MAP_HEIGHT / 2][MAP_WIDTH / 2 - 1] = Tile.FOUNTAIN[2];
		map[MAP_HEIGHT / 2][MAP_WIDTH / 2] = Tile.FOUNTAIN[3];

		// for (int i = 0; i < map.length; i++)
		// for (int o = 0; o < map[0].length; o++)
		// mapData[i][o] = r.nextInt(100);

		/*
		 * Minimap
		 */
		// minimapBase = ImageHandler.get().getImage("minimap");
		// minimap = new BufferedImage(minimapBase.getWidth(null), minimapBase.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Respawn Images
		respawnImages = new Image[2];
		// respawnImages[0] = ImageHandler.get().getImage("respawn2_empty");
		// respawnImages[1] = ImageHandler.get().getImage("respawn2_full");
		respawnImages[0] = ImageHandler.get().getImage("respawn_0");
		respawnImages[1] = ImageHandler.get().getImage("respawn_1");
	}

	public void update() {
		/*
		 * Map Scrolling
		 */
		double vx = 0;
		double vy = 0;
		// Scrolling
		if (mouseScrollingEnabled && Game.mouseInput.mousePosition.x >= 0 && Game.mouseInput.mousePosition.x < SCROLLBAR_X //
				|| Game.keyInput.left.isDown) vx = -SCROLL_SPEED;
		else if (mouseScrollingEnabled && Game.mouseInput.mousePosition.x >= 1 - SCROLLBAR_Y && Game.mouseInput.mousePosition.x < 1 //
				|| Game.keyInput.right.isDown) vx = SCROLL_SPEED;
		if (mouseScrollingEnabled && Game.mouseInput.mousePosition.y >= 0 && Game.mouseInput.mousePosition.y < SCROLLBAR_Y //
				|| Game.keyInput.up.isDown) vy = -SCROLL_SPEED;
		else if (mouseScrollingEnabled && Game.mouseInput.mousePosition.y >= 1 - SCROLLBAR_Y && Game.mouseInput.mousePosition.y < 1 //
				|| Game.keyInput.down.isDown) vy = SCROLL_SPEED;

		// Set Focus
		Game.focus.set(Game.focus.x + Time.getTickInterval() * vx, Game.focus.y + Time.getTickInterval() * vy);

		// Boundary Checks
		if (Game.focus.x < Game.TILE_WIDTH / 2.0) Game.focus.set(Game.TILE_WIDTH / 2.0, Game.focus.y);
		else if (Game.focus.x > Map.MAP_WIDTH - Game.TILE_WIDTH / 2.0) Game.focus.set(Map.MAP_WIDTH - Game.TILE_WIDTH / 2.0, Game.focus.y);
		if (Game.focus.y < Game.TILE_HEIGHT / 2.0) Game.focus.set(Game.focus.x, Game.TILE_HEIGHT / 2.0);
		else if (Game.focus.y > Map.MAP_HEIGHT - Game.TILE_HEIGHT / 2.0) Game.focus.set(Game.focus.x, Map.MAP_HEIGHT - Game.TILE_HEIGHT / 2.0);

		// if (!Game.isServer) {
		// Game.focus = Game.entities.player.transform.position.clone();
		// }

		/*
		 * Map Updates
		 */
		// Random r = new Random();
		// for (int i = 0; i < TILEUPDATES_PERTICK; i++) {
		// int t = r.nextInt(MAP_WIDTH * MAP_HEIGHT);
		// mapData[t / MAP_WIDTH][t % MAP_WIDTH]++;
		// }
		for (int i = 0; i < MAP_WIDTH * MAP_HEIGHT; i++) {
			mapData[i / MAP_WIDTH][i % MAP_HEIGHT]++;
		}
	}

	public void render() {
		Game.s.clear();

		// Smart Rendering!
		int dx = (int) (Window.CENTER_X - (Game.focus.x * Game.TILE_SIZE));
		int dy = (int) (Window.CENTER_Y - (Game.focus.y * Game.TILE_SIZE));

		// TODO: Figure out why I have to (-1) from sx/y
		int sx = -(int) Math.floor(dx / Game.TILE_SIZE) - 1;
		int sy = -(int) Math.floor(dy / Game.TILE_SIZE) - 1;
		int lx = sx + Game.TILE_WIDTH + 2;
		int ly = sy + Game.TILE_HEIGHT + 2;

		for (int i = sy; i < ly; i++) {
			for (int o = sx; o < lx; o++) {
				if (i < 0 || o < 0 || i >= MAP_HEIGHT || o >= MAP_WIDTH) {
					Tile.VOID.render(dx + o * Game.TILE_SIZE, dy + i * Game.TILE_SIZE, 0);
				} else {
					map[i][o].render(dx + o * Game.TILE_SIZE, dy + i * Game.TILE_SIZE, mapData[i][o]);
				}
			}
		}
	}

	public void postRender() {
		int dx = (int) (Window.CENTER_X - (Game.focus.x * Game.TILE_SIZE));
		int dy = (int) (Window.CENTER_Y - (Game.focus.y * Game.TILE_SIZE));
		for (int i = 0; i < MAP_HEIGHT; i++) {
			for (int o = 0; o < MAP_WIDTH; o++) {
				map[i][o].postRender(dx + o * Game.TILE_SIZE, dy + i * Game.TILE_SIZE);
			}
		}
	}

	public void onGUI() {
		/*
		 * Minimap
		 */
		// Graphics2D g = (Graphics2D) minimap.getGraphics();
		// g.drawImage(minimapBase, 0, 0, null);
		// Game.s.g.drawImage(minimap, 10, 10, minimap.getWidth(), minimap.getHeight(), null);

		/*
		 * Respawning
		 */
		if (Game.showRespawningScreen) {
			respawnInterval -= Time.getTickInterval();
			GUI.partialHorizontalBar(respawnImages, (Game.s.width / 2) - (respawnImages[0].getWidth(null) / 2), (Game.s.height / 2) - (respawnImages[0].getHeight(null) / 2), 1 - (respawnInterval / PudgeEntity.RESPAWN_INTERVAL));
		} else {
			respawnInterval = PudgeEntity.RESPAWN_INTERVAL;
		}

		/*
		 * Win/Lose condition
		 */
		Team winningTeam = null;
		winningTeam = (leftTeamScore >= 10) ? Team.leftTeam : (rightTeamScore >= 10) ? Team.rightTeam : null;

		if (winningTeam != null) {
			if (Game.entities.player.team == winningTeam) {
				Game.entities.screenOverlays.add(new TimedRenderObject(ImageHandler.get().getImage("messages/winner"), Game.s.cx, Game.s.cy, true, true, 30));
			} else {
				Game.entities.screenOverlays.add(new TimedRenderObject(ImageHandler.get().getImage("messages/loser"), Game.s.cx, Game.s.cy, true, true, 30));
			}

			// if (Game.entities.player.team == winningTeam) JOptionPane.showMessageDialog(Game.w, "Your team won! Yeeha! " + leftTeamScore + " " + rightTeamScore);
			// else JOptionPane.showMessageDialog(Game.w, "Your team lost. Go kill yourselves." + leftTeamScore + " " + rightTeamScore);
			// System.exit(1);
		}

		/*
		 * Scores
		 */
		int oldLeftTeamScore = leftTeamScore;
		int oldRightTeamScore = rightTeamScore;

		leftTeamScore = 0;
		leftTeamCount = 0;
		rightTeamScore = 0;
		rightTeamCount = 0;

		for (int i = 0; i < Game.entities.entities.size(); i++) {
			if (Game.entities.entities.get(i) instanceof PudgeEntity) {
				PudgeEntity p = (PudgeEntity) Game.entities.entities.get(i);
				if (p.team == Team.leftTeam) {
					leftTeamScore += p.stats.getKills();
					leftTeamCount++;
				} else if (p.team == Team.rightTeam) {
					rightTeamScore += p.stats.getKills();
					rightTeamCount++;
				}
				// p.stats.showScore();
			}
		}

		for (int i = 0; i < Game.entities.respawnEntities.size(); i++) {
			if (Game.entities.respawnEntities.get(i) instanceof PudgeEntity) {
				PudgeEntity p = (PudgeEntity) Game.entities.respawnEntities.get(i);
				if (p.team == Team.leftTeam) {
					leftTeamScore += p.stats.getKills();
					leftTeamCount++;
				} else if (p.team == Team.rightTeam) {
					rightTeamScore += p.stats.getKills();
					rightTeamCount++;
				}
				// p.stats.showScore();
			}
		}

		if (oldLeftTeamScore + oldRightTeamScore == 0 && leftTeamScore + rightTeamScore > 0) {
			// First Blood!
			Game.entities.screenOverlays.add(new TimedRenderObject(ImageHandler.get().getImage("messages/firstblood"), Game.s.cx, 10, true, false, 4));
		}

		// GUI.showText("Team 1 Score", TextSize.normal, TextColor.black, 10, 10);
		// GUI.showText("Team 2 Score", TextSize.normal, TextColor.black, Game.s.width - 82, 10);
		// GUI.showText("Total " + leftTeamScore, TextSize.normal, TextColor.black, 10, (leftTeamCount + 2) * 10);
		// GUI.showText("Total " + rightTeamScore, TextSize.normal, TextColor.black, Game.s.width - 82, (rightTeamCount + 2) * 10);
		GUI.showTally(leftTeamScore, 4, 4, true);
		GUI.showTally(rightTeamScore, Game.s.width - GUI.rightBorder.getWidth(null) - 4, 4, false);
	}

	public List<CollisionBox> getCollisionBoxes(Rigidbody r) {
		CollisionBox b = r.getCollisionBox().grow(1);
		List<CollisionBox> l = new ArrayList<CollisionBox>();

		for (double y = b.y0; y <= b.y1; y++) {
			for (double x = b.x0; x <= b.x1; x++) {
				if ((int) y < 0 || (int) y >= map.length || (int) x < 0 || (int) x >= map[0].length) continue;
				Tile t = map[(int) y][(int) x];
				if (t.blocks(b.owner)) {
					l.add(new CollisionBox(t, (int) x, (int) y, (int) x + 1, (int) y + 1));
				}
			}
		}

		return l;
	}

	public boolean isCollides(double x, double y, HookEntity e) {
		double ty = y - e.rigidbody.collision.y / 2;
		for (int i = 0; i < 2; i++, ty += e.rigidbody.collision.y) {
			double tx = x - e.rigidbody.collision.x / 2;
			for (int o = 0; o < 2; o++, tx += e.rigidbody.collision.x) {
				if (map[(int) ty][(int) tx].isHookSolid()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCollides(double x, double y, Entity e) {
		double ty = y - e.rigidbody.collision.y / 2;
		for (int i = 0; i < 2; i++, ty += e.rigidbody.collision.y) {
			double tx = x - e.rigidbody.collision.x / 2;
			for (int o = 0; o < 2; o++, tx += e.rigidbody.collision.x) {
				if (map[(int) ty][(int) tx].isPudgeSolid()) {
					return true;
				}
			}
		}
		return false;
	}

	public void addLightSources(List<Entity> entities) {
		for (int i = 0; i < MAP_HEIGHT; i++) {
			for (int o = 0; o < MAP_WIDTH; o++) {
				if (map[i][o] instanceof LightTile) {
					LightTile t = ((LightTile) map[i][o]);
					entities.add(new LightSourceEntity(new Vector2(o + 0.5, i + 0.5), //
							o < MAP_WIDTH / 2 ? Team.leftTeam : Team.rightTeam, //
							t.lightWidth, t.lightHeight));
				}
			}
		}

		// Healing Fountain
		entities.add(new HealingFountainEntity(new Vector2(MAP_WIDTH / 2.0, MAP_HEIGHT / 2.0)));
	}
}

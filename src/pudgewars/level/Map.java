package pudgewars.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.components.Rigidbody;
import pudgewars.entities.Entity;
import pudgewars.entities.HookEntity;
import pudgewars.physics.Physics;
import pudgewars.util.CollisionBox;
import pudgewars.util.Time;

public class Map {
	public final static int MAP_WIDTH = 26;
	public final static int MAP_HEIGHT = 30;
	public final static int DIVISION_WIDTH = 2; // This should always be even.

	// Map Scrolling Constants
	public final static double SCROLLBAR_X = (1.0 / 4) * 0.25;
	public final static double SCROLLBAR_Y = (1.0 / 3) * 0.25;
	public final static double SCROLL_SPEED = 10; // tiles / second

	private Tile[][] map = new Tile[MAP_HEIGHT][MAP_WIDTH];

	public Map() {
		for (int i = 0; i < map.length; i++) {
			for (int o = 0; o < map[0].length; o++) {
				if (o + 1 == (MAP_WIDTH - DIVISION_WIDTH) / 2) {
					map[i][o] = Tile.T_Mound;
				} else if (o == (MAP_WIDTH + DIVISION_WIDTH) / 2) {
					map[i][o] = Tile.T_Mound;
				} else if (i == 0 || o == 0 || i == MAP_HEIGHT - 1 || o == MAP_WIDTH - 1) {
					map[i][o] = Tile.T_Block;
				} else {
					int r = new Random().nextInt(3);
					if (r == 0) {
						map[i][o] = Tile.T_Dirt1;
					} else if (r == 1) {
						map[i][o] = Tile.T_Dirt2;
					} else {
						map[i][o] = Tile.T_Dirt3;
					}
				}
			}
		}
	}

	public void update() {
		// Scrolling of th Map
		double vx = 0;
		double vy = 0;
		if (Game.mouseInput.mousePosition.y >= 0 && Game.mouseInput.mousePosition.y < SCROLLBAR_Y || Game.keyInput.up.isDown) {
			vy = -SCROLL_SPEED;
		} else if (Game.mouseInput.mousePosition.y >= 1 - SCROLLBAR_Y && Game.mouseInput.mousePosition.y < 1 || Game.keyInput.down.isDown) {
			vy = SCROLL_SPEED;
		}

		if (Game.mouseInput.mousePosition.x >= 0 && Game.mouseInput.mousePosition.x < SCROLLBAR_X || Game.keyInput.left.isDown) {
			vx = -SCROLL_SPEED;
		} else if (Game.mouseInput.mousePosition.x >= 1 - SCROLLBAR_Y && Game.mouseInput.mousePosition.x < 1 || Game.keyInput.right.isDown) {
			vx = SCROLL_SPEED;
		}
		Game.focus.set(Game.focus.x + Time.getTickInterval() * vx, Game.focus.y + Time.getTickInterval() * vy);
	}

	public List<CollisionBox> getCollisionBoxes(Rigidbody r) {
		CollisionBox b = r.getCollisionBox().grow(1);
		List<CollisionBox> l = new ArrayList<CollisionBox>();

		for (double y = b.y0; y <= b.y1; y++) {
			for (double x = b.x0; x <= b.x1; x++) {
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

	public void render() {
		int dx = (int) (Window.CENTER_X - (Game.focus.x * Game.TILE_SIZE));
		int dy = (int) (Window.CENTER_Y - (Game.focus.y * Game.TILE_SIZE));
		for (int i = 0; i < MAP_HEIGHT; i++) {
			for (int o = 0; o < MAP_WIDTH; o++) {
				map[i][o].render(dx + o * Game.TILE_SIZE, dy + i * Game.TILE_SIZE);
			}
		}
	}
}

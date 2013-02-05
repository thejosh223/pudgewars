package pudgewars.level;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Random;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.entities.Entity;
import pudgewars.entities.HookEntity;

public class Map {
	public final static int MAP_WIDTH = 26;
	public final static int MAP_HEIGHT = 30;
	public final static int DIVISION_WIDTH = 2;	//This should always be even.
	
	private Tile[][] map = new Tile[MAP_HEIGHT][MAP_WIDTH];
	
	public Map() {
		for (int i = 0; i < map.length; i++) {
			for (int o = 0; o < map[0].length; o++) {
				if (o + 1 == (MAP_WIDTH - DIVISION_WIDTH) / 2) {
					map[i][o] = Tile.T_Block;
				} else if (o == (MAP_WIDTH + DIVISION_WIDTH) / 2) {
					map[i][o] = Tile.T_Block;
				} else if (i == 0 || o == 0 || i == MAP_HEIGHT - 1 || o == MAP_WIDTH - 1) {
					map[i][o] = Tile.T_Block;
				} else {
					int r = new Random().nextInt(3);
					if (r == 0) {
						map[i][o] = Tile.T_Dirt1;
					} else if (r == 1){
						map[i][o] = Tile.T_Dirt2;
					} else {
						map[i][o] = Tile.T_Dirt3;
					}
				}
			}
		}
	}
	
	public void update(long timePassed) {
		
	}

	public boolean isCollides(double x, double y, HookEntity e) {
		double ty = y - e.getCollisionHeight() / 2;
		for (int i = 0; i < 2; i++, ty += e.getCollisionHeight()) {
			double tx = x - e.getCollisionWidth() / 2;
			for (int o = 0; o < 2; o++, tx += e.getCollisionWidth()) {
				if (map[(int) ty][(int) tx].isHookSolid()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isCollides(double x, double y, Entity e) {
		double ty = y - e.getCollisionHeight() / 2;
		for (int i = 0; i < 2; i++, ty += e.getCollisionHeight()) {
			double tx = x - e.getCollisionWidth() / 2;
			for (int o = 0; o < 2; o++, tx += e.getCollisionWidth()) {
				if (map[(int) ty][(int) tx].isSolid()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void draw(Graphics2D g, Point2D focus) {
//		int x = (int) Math.floor(focus.getX() - Game.TILE_WIDTH / 2);
//		int y = (int) Math.floor(focus.getY() - Game.TILE_HEIGHT / 2);
//		
//		if (x < 0) {
//			x = 0;
//		} else if (x > MAP_WIDTH - Game.TILE_WIDTH / 2) {
//			x = MAP_WIDTH - Game.TILE_WIDTH / 2;
//		}
//		
//		if (y < 0) {
//			y = 0;
//		} else if (y > MAP_HEIGHT - Game.TILE_HEIGHT / 2) {
//			y = MAP_HEIGHT- Game.TILE_HEIGHT / 2;
//		}
		
		int dx = (int) (Window.CENTER_X - (focus.getX() * Game.TILE_SIZE));
		int dy = (int) (Window.CENTER_Y - (focus.getY() * Game.TILE_SIZE));
		for (int i = 0; i < MAP_HEIGHT; i++) {
			for (int o = 0; o < MAP_WIDTH; o++) {
				map[i][o].draw(g, dx + o * Game.TILE_SIZE, dy + i * Game.TILE_SIZE);
			}
		}
	}
}

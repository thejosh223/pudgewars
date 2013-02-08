package pudgewars;

import java.awt.Graphics2D;

import pudgewars.entities.EntityManager;
import pudgewars.input.Keys;
import pudgewars.input.MouseHandler;
import pudgewars.level.Map;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class Game {
	public static final int TILE_SIZE = 16;
	public static final int TILE_WIDTH = 20;
	public static final int TILE_HEIGHT = 15;

	private boolean gameRunning;
	// public static ArrayList<Entity> entities;
	public static EntityManager entities;
	public static Map map;
	public static Screen s;
	public static Vector2 focus;

	// private PudgeEntity player;

	/*
	 * Input Classes
	 */
	public static Keys keyInput;
	public static MouseHandler mouseInput;

	// Constructor Method
	public Game(Keys k, MouseHandler m) {
		Game.keyInput = k;
		Game.mouseInput = m;

		init();
		gameLoop();
	}

	public void init() {
		map = new Map();
		focus = new Vector2(Map.MAP_WIDTH / 2, Map.MAP_HEIGHT / 2);
		gameRunning = true;

		entities = new EntityManager();

		s = new Screen(Window.WIDTH, Window.HEIGHT);
	}

	public void gameLoop() {
		long timeBefore = System.nanoTime();
		long timePassed = System.nanoTime() - timeBefore;
		float unprocessedSeconds = 0;

		// FPS Counter
		int fps = 0;
		tick();

		while (gameRunning) {
			/*
			 * Time Processing
			 */
			long now = System.nanoTime();
			timePassed = now - timeBefore;
			timeBefore = now;

			// If < 10fps, slow down the game.
			if (timePassed > 100000000) {
				timePassed = 100000000;
			}

			unprocessedSeconds += timePassed / 1000000000.0;

			/*
			 * Tick Controller -limits the # of ticks to TPS. -if FPS < 60, it ticks rather than rendering.
			 */
			boolean ticked = false;
			while (unprocessedSeconds > Time.getBaseTickInterval()) {
				tick();
				unprocessedSeconds -= Time.getBaseTickInterval();
				ticked = true;

				Time.totalTicks++;
				if (Time.totalTicks % 60 == 0) {
					Window.container.setTitle("PudgeWars (fps:" + fps + ")");
					fps = 0;
				}
			}

			/*
			 * Rendering
			 */
			// if (ticked) {
			render();
			fps++;
			// }

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void tick() {
		/*
		 * UPDATES
		 */

		// Entities and Map Update
		entities.updateEntities();
		entities.lateUpdateEntities();
		entities.killUpdateEntities();

		controls();
	}

	private void render() {
		// Render Map and Entities
		entities.render();

		// Render GUI
		entities.renderGUI();

		// Flips the page between the two buffers
		s.drawToGraphics((Graphics2D) Window.strategy.getDrawGraphics());
		Window.strategy.show();
	}

	public void controls() {
		keyInput.tick();

		if (focus.x < Game.TILE_WIDTH / 2) {
			focus.set(Game.TILE_WIDTH / 2, focus.y);
		} else if (focus.x > Map.MAP_WIDTH - Game.TILE_WIDTH / 2) {
			focus.set(Map.MAP_WIDTH - Game.TILE_WIDTH / 2, focus.y);
		}

		if (focus.y < Game.TILE_HEIGHT / 2) {
			focus.set(focus.x, Game.TILE_HEIGHT / 2);
		} else if (focus.y > Map.MAP_HEIGHT - Game.TILE_HEIGHT / 2) {
			focus.set(focus.x, Map.MAP_HEIGHT - Game.TILE_HEIGHT / 2);
		}
	}
}

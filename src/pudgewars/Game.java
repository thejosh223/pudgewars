package pudgewars;

import java.awt.Graphics2D;

import pudgewars.entities.EntityManager;
import pudgewars.input.Keys;
import pudgewars.input.MouseButtons;
import pudgewars.level.Map;
import pudgewars.network.Network;
import pudgewars.render.CursorManager;
import pudgewars.util.Profiler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class Game {
	public static final int TILE_SIZE = 16;
	public static final int TILE_WIDTH = 20;
	public static final int TILE_HEIGHT = 15;

	public static boolean isServer;

	public static boolean showRespawningScreen = false;

	public boolean gameRunning = true;
	public static CursorManager cursor;
	public static EntityManager entities;
	public static Map map;
	public static Network net;

	// Client-side Data (this is null on the ServerGame)
	public static Window w;
	public static Screen s;
	public static Vector2 focus;

	/*
	 * Profiler
	 */
	public static Profiler p;

	/*
	 * Input Classes
	 */
	public static Keys keyInput;
	public static MouseButtons mouseInput;

	// Constructor Method
	public Game(Window w, Keys k, MouseButtons m) {
		Game.w = w;
		Game.keyInput = k;
		Game.mouseInput = m;

		// String[] names = { "Tick", "Render", "Sleep" };
		String[] names = { "Tick", "Render" };
		p = new Profiler(names);
	}

	public void init() {
		map = new Map();
		focus = new Vector2(Map.MAP_WIDTH / 2, Map.MAP_HEIGHT / 2);
		gameRunning = true;
		s = new Screen(Window.WIDTH, Window.HEIGHT);
	}

	public void gameLoop() {
		long timeBefore = System.nanoTime();
		long timePassed = System.nanoTime() - timeBefore;
		double unprocessedSeconds = 0;

		double sleepTime = 0;

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
			if (unprocessedSeconds < Time.getBaseTickInterval()) {
				sleepTime += timePassed / 1000000000.0;
			}

			boolean ticked = false;
			while (unprocessedSeconds > Time.getBaseTickInterval()) {
				p.start();
				tick();
				p.end();

				unprocessedSeconds -= Time.getBaseTickInterval();
				ticked = true;

				Time.totalTicks++;
				if (Time.totalTicks % 60 == 0) {
					// p.printResults();
					Window.container.setTitle("PudgeWars (fps:" + fps + ") (spd:" + (int) (sleepTime * 100 / 0.94) + "%)");
					fps = 0;
					sleepTime = 0;
				}
			}

			/*
			 * Rendering
			 */
			if (ticked) {
				p.start();
				render();
				p.end();
				fps++;
			}

			postRender();

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void tick() {
		/*
		 * UPDATES
		 */
		// Entities and Map Update
		entities.updateEntities();
		entities.lateUpdateEntities();
		entities.killUpdateEntities();
		if(Game.isServer) entities.updateRespawnEntities();
	}

	protected void render() {
		// Render Map and Entities
		entities.render();
		// Render GUI
		entities.renderGUI();

		// Flips the page between the two buffers
		s.drawToGraphics((Graphics2D) Window.strategy.getDrawGraphics());
		Window.strategy.show();
	}

	protected void postRender() {
	}

	public void controls() {
		keyInput.tick();
		mouseInput.tick();
	}
}

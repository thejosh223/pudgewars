package pudgewars;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.level.Map;
import pudgewars.util.Time;

public class Game {
	public static final int TILE_SIZE = 16;
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 12;

	public final static double SCROLL_SPEED = 8;

	private boolean gameRunning;
	public static ArrayList<Entity> entities;
	public static Map map;

	private PudgeEntity player;

	public static Point2D focus;

	// Constructor Method
	public Game() {
		init();
		gameLoop();
	}

	public void init() {
		map = new Map();
		focus = new Point2D.Double(Map.MAP_WIDTH / 2, Map.MAP_HEIGHT / 2);
		gameRunning = true;

		entities = new ArrayList<Entity>();
		player = new PudgeEntity(4, 4);
		entities.add(player);
		entities.add(new PudgeEntity(4, 12));
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

			if (ticked) {
				render();
				fps++;
			}

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

		long time = (long) (Time.getTickInterval() * 1000);

		// Entities/Player Update
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update(time);
		}

		// World Update
		map.update(time);

		controls(time);
	}

	private void render() {
		Graphics2D g = (Graphics2D) Window.strategy.getDrawGraphics();
		// Anti-Aliasing (THIS IS AWESOME.)
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// World Drawing
		map.draw(g, focus);

		// Entity/Player Drawing
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw(g);
		}

		// Clean up unused variables.
		g.dispose();

		// Flips the page between the two buffers
		Window.strategy.show();
	}

	public final static int SCROLLBAR_SIZE = Game.TILE_SIZE / 2;

	public void controls(long timePassed) {
		double vx = 0;
		double vy = 0;
		// int yScroll = 0;
		// int xScroll = 0;

		if (Window.inputter.activePosition.y >= 0 && Window.inputter.activePosition.y < SCROLLBAR_SIZE || Window.inputter.up) {
			vy = -SCROLL_SPEED;
		} else if (Window.inputter.activePosition.y >= Window.HEIGHT - SCROLLBAR_SIZE && Window.inputter.activePosition.y < Window.HEIGHT || Window.inputter.down) {
			vy = SCROLL_SPEED;
		}

		if (Window.inputter.activePosition.x >= 0 && Window.inputter.activePosition.x < SCROLLBAR_SIZE || Window.inputter.left) {
			vx = -SCROLL_SPEED;
		} else if (Window.inputter.activePosition.x >= Window.WIDTH - SCROLLBAR_SIZE && Window.inputter.activePosition.x < Window.WIDTH || Window.inputter.right) {
			vx = SCROLL_SPEED;
		}

		focus.setLocation(focus.getX() + timePassed * vx / 1000, focus.getY() + timePassed * vy / 1000);

		if (focus.getX() < Game.TILE_WIDTH / 2) {
			focus.setLocation(Game.TILE_WIDTH / 2, focus.getY());
		} else if (focus.getX() > Map.MAP_WIDTH - Game.TILE_WIDTH / 2) {
			focus.setLocation(Map.MAP_WIDTH - Game.TILE_WIDTH / 2, focus.getY());
		}

		if (focus.getY() < Game.TILE_HEIGHT / 2) {
			focus.setLocation(focus.getX(), Game.TILE_HEIGHT / 2);
		} else if (focus.getY() > Map.MAP_HEIGHT - Game.TILE_HEIGHT / 2) {
			focus.setLocation(focus.getX(), Map.MAP_HEIGHT - Game.TILE_HEIGHT / 2);
		}

		if (Window.inputter.lastRightClicked != null) {
			Point2D click = Window.inputter.lastRightClicked;
			player.setTarget(click);

			Window.inputter.lastRightClicked = null;
		}
		if (Window.inputter.lastLeftClicked != null) {
			Point2D click = Window.inputter.lastLeftClicked;
			player.setHook(click);

			Window.inputter.lastLeftClicked = null;
		}
	}
}

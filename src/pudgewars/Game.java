package pudgewars;

import java.awt.Graphics2D;
import java.util.Vector;

import pudgewars.components.Network;
import pudgewars.entities.EntityManager;
import pudgewars.input.Keys;
import pudgewars.input.MouseHandler;
import pudgewars.level.Map;
import pudgewars.render.CursorManager;
import pudgewars.util.Time;
import pudgewars.util.Vector2;
import pudgewars.network.MyConnection;

public class Game {
	public static final int TILE_SIZE = 16;
	public static final int TILE_WIDTH = 20;
	public static final int TILE_HEIGHT = 15;

	private boolean gameRunning;
	//public static ArrayList<Entity> entities;
	public static Window w;
	public static CursorManager cursor;
	public static EntityManager entities;
	public static Map map;
	public static Screen s;
	public static Vector2 focus;
	public MyConnection conn;
	public static Network client;
	//private PudgeEntity player;
	public static Vector<Vector2> positions;
	
	/*
	 * Input Classes
	 */
	public static Keys keyInput;
	public static MouseHandler mouseInput;

	// Constructor Method
	public Game(Window w, Keys k, MouseHandler m) {
		Game.w = w;
		Game.keyInput = k;
		Game.mouseInput = m;
	}

	public void init(MyConnection conn) {
		positions = new Vector<Vector2>();

		map = new Map();
		focus = new Vector2(Map.MAP_WIDTH / 2, Map.MAP_HEIGHT / 2);
		gameRunning = true;
		
		client = new Network(conn);
		entities = new EntityManager();
		entities.generateClientEntities(client);
		
		cursor = new CursorManager(w);
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
			if (ticked) {
			}
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
		String msg = client.getMessage();
		int x = 0;
		do{
			positions.add(null);
			if(msg.equals("null")) positions.set(x, null);
			else{
				String parts[] = msg.split(" ");
				positions.set(x, new Vector2(Float.parseFloat(parts[0]),Float.parseFloat(parts[1])));
			}
			x++;
			msg = client.getMessage();
		}while(!msg.equals("EOM"));
		/*
		 * UPDATES
		 */

		// Entities and Map Update
		entities.updateEntities();
		entities.lateUpdateEntities();
		entities.killUpdateEntities();

		// System.out.println("Entities: " + entities.entities.size());

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
		mouseInput.tick();
	}
}

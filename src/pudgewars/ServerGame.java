package pudgewars;

import java.util.Vector;

import pudgewars.entities.ServerEntityManager;
import pudgewars.level.Map;
import pudgewars.util.Time;
import pudgewars.network.ClientNode;
import pudgewars.components.Network;
import pudgewars.util.Vector2;

public class ServerGame {
	private boolean gameRunning;
	public static ServerEntityManager entities;
	public static Map map;
	public static Vector<Network> clients;
	public static Vector<Vector2> moveTargets;
	public static Vector<Vector2> hookTargets;
	//public static Vector isSpecialHook = false;

	public ServerGame(Vector<ClientNode> clients) {
		initNetwork(clients);
		entities = new ServerEntityManager(clients);
	}

	public void init() {
		map = new Map();
		gameRunning = true;
		entities.sendServerEntities(clients);
		moveTargets = new Vector<Vector2>();
		for (int i = 0; i < clients.size(); i++) {
			moveTargets.add(null);
		}
		
		hookTargets = new Vector<Vector2>();
		for (int i = 0; i < clients.size(); i++) {
			hookTargets.add(null);
		}
		// System.exit(1);
		// cursor = new CursorManager(w);
		// s = new Screen(Window.WIDTH, Window.HEIGHT);
	}

	private static class handleClientMessages implements Runnable {
		//change code to have multiple threads handling each client
		public void run() {
			String msg = null;
			while (true) {
				for (int i = 0; i < clients.size(); i++) {
					//assuming that the message is for moveTarget
					msg = clients.get(i).getMessage();
					if (msg.equals("null")) moveTargets.set(i, null);
					else {
						String parts[] = msg.split(" ");
						moveTargets.set(i, new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
					}
					
					msg = clients.get(i).getMessage();
					if (msg.equals("null")) hookTargets.set(i, null);
					else {
						String parts[] = msg.split(" ");
						hookTargets.set(i, new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
						//isSpecialHook = (parts[2].equals("true")) ? true : false;
					}
					
				}
			}
		}
	}

	public void gameLoop() {
		Thread t = new Thread(new handleClientMessages());
		t.start();
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
			 * Tick Controller -limits the # of ticks to TPS. -if FPS < 60, it
			 * ticks rather than rendering.
			 */
			boolean ticked = false;
			while (unprocessedSeconds > Time.getBaseTickInterval()) {
				tick();
				unprocessedSeconds -= Time.getBaseTickInterval();
				ticked = true;

				Time.totalTicks++;
				if (Time.totalTicks % 60 == 0) {
					// Window.container.setTitle("PudgeWars (fps:" + fps + ")");
					fps = 0;
				}
			}

			/*
			 * Rendering
			 */
			if (ticked) {
			}
			// render();
			// sendToAll();
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
		// broadcast moveTargets of all players
		for (int x = 0; x < clients.size(); x++) {
			for (int y = 0; y < clients.size(); y++) {
				if (moveTargets.get(y) != null) clients.get(x).sendMessage(moveTargets.get(y).x + " " + moveTargets.get(y).y);
				else clients.get(x).sendMessage("null");
			}
			clients.get(x).sendMessage("EOM");
		}
		
		// broadcast hookTargets of all players
		for (int x = 0; x < clients.size(); x++) {
			for (int y = 0; y < clients.size(); y++) {
				if (hookTargets.get(y) != null) clients.get(x).sendMessage(hookTargets.get(y).x + " " + hookTargets.get(y).y);
				else clients.get(x).sendMessage("null");
			}
			clients.get(x).sendMessage("EOM");
		}
		//System.out.println("Hey");
		// Entities and Map Update
		entities.updateEntities();
		entities.lateUpdateEntities();
		entities.killUpdateEntities();
		//entities.sendPosition();
		//System.out.println("Entities: " + entities.entities.size());

		// controls();
	}

	private void initNetwork(Vector<ClientNode> clients) {
		this.clients = new Vector<Network>();
		for (int x = 0; x < clients.size(); x++)
			this.clients.add(new Network(clients.get(x).getConnection()));
	}
}

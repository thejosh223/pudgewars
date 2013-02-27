package pudgewars;

import java.util.ArrayList;
import java.util.List;

import pudgewars.components.Network;
import pudgewars.entities.ServerEntityManager;
import pudgewars.level.Map;
import pudgewars.network.ClientNode;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class ServerGame {
	private boolean gameRunning;
	public static ServerEntityManager entities;
	public static Map map;
	public static List<Network> clients;
	public static List<Vector2> moveTargets;
	public static List<Vector2> hookTargets;
	public static List<Boolean> isSpecialHook;

	public ServerGame(List<ClientNode> clients) {
		initNetwork(clients);
		entities = new ServerEntityManager(clients);
	}

	public void init() {
		map = new Map();
		gameRunning = true;
		entities.sendServerEntities(clients);
		moveTargets = new ArrayList<Vector2>(10);
		for (int i = 0; i < clients.size(); i++) {
			moveTargets.add(null);
		}

		hookTargets = new ArrayList<Vector2>(10);
		for (int i = 0; i < clients.size(); i++) {
			hookTargets.add(null);
		}
		
		isSpecialHook = new ArrayList<Boolean>(10);
		for (int i = 0; i < clients.size(); i++) {
			isSpecialHook.add(false);
		}
		// System.exit(1);
		// cursor = new CursorManager(w);
		// s = new Screen(Window.WIDTH, Window.HEIGHT);
	}

	private static class HandleClientMessages implements Runnable {
		// change code to have multiple threads handling each client
		public void run() {
			String msg = null;
			while (true) {
				for (int i = 0; i < clients.size(); i++) {
					// assuming that the message is for moveTarget
					msg = clients.get(i).getMessage();
					if (msg.equals("null")) moveTargets.set(i, null);
					else {
						String parts[] = msg.split(" ");
						moveTargets.set(i, new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
					}

					msg = clients.get(i).getMessage();
					if (msg.equals("null")){
						hookTargets.set(i, null);
						isSpecialHook.set(i, false);
					}
					else {
						String parts[] = msg.split(" ");
						hookTargets.set(i, new Vector2(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
						isSpecialHook.set(i, (parts[2].equals("true")) ? true : false);
					}

				}
			}
		}
	}

	public void gameLoop() {
		Thread t = new Thread(new HandleClientMessages());
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
			// if (ticked) {
			// }
			// fps++;
			// }

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("ERROR!");
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
				if (hookTargets.get(y) != null) clients.get(x).sendMessage(hookTargets.get(y).x + " " + hookTargets.get(y).y + " " + isSpecialHook.get(y));
				else clients.get(x).sendMessage("null");
			}
			clients.get(x).sendMessage("EOM");
		}
		// System.out.println("Hey");
		// Entities and Map Update
		entities.updateEntities();
		entities.lateUpdateEntities();
		entities.killUpdateEntities();
		// entities.sendPosition();
		// System.out.println("Entities: " + entities.entities.size());
	}

	private void initNetwork(List<ClientNode> clients) {
		ServerGame.clients = new ArrayList<Network>();
		for (int x = 0; x < clients.size(); x++)
			ServerGame.clients.add(new Network(clients.get(x).getConnection()));
	}
}

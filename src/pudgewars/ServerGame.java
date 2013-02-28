package pudgewars;

import java.util.List;

import pudgewars.components.ServerNetwork;
import pudgewars.entities.ServerEntityManager;
import pudgewars.level.Map;
import pudgewars.network.ClientNode;
import pudgewars.util.Time;

public class ServerGame {
	private boolean gameRunning;
	public static ServerEntityManager entities;
	public static Map map;
	public static ServerNetwork net;
	
	public ServerGame(List<ClientNode> clients) {
		net = new ServerNetwork(clients);
		entities = new ServerEntityManager(clients);
	}

	public void init() {
		map = new Map();
		gameRunning = true;
		entities.sendPudgeEntities();
		// System.exit(1);
		// cursor = new CursorManager(w);
		// s = new Screen(Window.WIDTH, Window.HEIGHT);
	}

	private static class HandleClientMessages implements Runnable {
		// change code to have multiple threads handling each client
		public void run() {
			while (true) {
				net.getMoveTargets();
				net.getHookTargets();
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
		net.sendMoveTargets();

		// broadcast hookTargets of all players
		net.sendHookTargets();
		
		// Entities and Map Update
		entities.updateEntities();
		entities.lateUpdateEntities();
		entities.killUpdateEntities();
	}
}

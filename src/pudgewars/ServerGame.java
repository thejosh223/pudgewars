package pudgewars;

import java.util.List;

import pudgewars.entities.ServerEntityManager;
import pudgewars.network.ClientNode;
import pudgewars.network.ServerNetwork;
import pudgewars.util.Time;

public class ServerGame extends Game {

	List<ClientNode> clients;

	public ServerGame(List<ClientNode> clients) {
		super(null, null, null);

		this.clients = clients;
		net = new ServerNetwork(clients);
	}

	public void init() {
		super.init();
		entities = new ServerEntityManager(clients);

		entities.sendPudgeEntities();
		// System.exit(1);
		// cursor = new CursorManager(w);
		// s = new Screen(Window.WIDTH, Window.HEIGHT);
	}

	private static class HandleClientMessages implements Runnable {
		// TODO: change code to have multiple threads handling each client
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
			// boolean ticked = false;
			while (unprocessedSeconds > Time.getBaseTickInterval()) {
				tick();
				unprocessedSeconds -= Time.getBaseTickInterval();
				// ticked = true;

				Time.totalTicks++;
				if (Time.totalTicks % 60 == 0) {
					// Window.container.setTitle("PudgeWars (fps:" + fps + ")");
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void tick() {
		// broadcast moveTargets of all players
		net.sendMoveTargets();

		// broadcast hookTargets of all players
		net.sendHookTargets();

		super.tick();
	}
}

package pudgewars;

import java.util.List;

import pudgewars.entities.ServerEntityManager;
import pudgewars.network.ClientNode;
import pudgewars.network.Network;
import pudgewars.network.ServerNetwork;
import pudgewars.network.ClientNetwork;
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
		
		//net.sendEntityData();
	}

	private static class handleClientMessages implements Runnable {
		private ClientNode client;
		int index;
		
		public handleClientMessages(ClientNode client, int index){
			this.client = client;
			this.index = index;
		}
		
		public void run() {
			while (true) {
				String msg = client.getConnection().getMessage();
				System.out.println(msg);
				entities.entities.get(index).setNetworkString(msg);
				//net.getMoveTargets();
				//net.getHookTargets();
			}
		}
	}

	public void gameLoop() {
		// Have separate threads listening to each client
		for(int x = 0; x< clients.size(); x++){
			Thread t = new Thread(new handleClientMessages(clients.get(x), x));
			t.start();
		}
		
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
		net.sendEntityData();

		// broadcast moveTargets of all players
		// net.sendMoveTargets();

		// broadcast hookTargets of all players
		// net.sendHookTargets();

		super.tick();
	}
}

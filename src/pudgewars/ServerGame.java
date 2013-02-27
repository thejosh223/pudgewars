package pudgewars;

import java.util.Vector;

import pudgewars.entities.EntityManager;
import pudgewars.level.Map;
import pudgewars.util.Time;
import pudgewars.network.ClientNode;
import pudgewars.components.Network;
import pudgewars.util.Vector2;

public class ServerGame {
	private boolean gameRunning;
	public static EntityManager entities;
	public static Map map;
	public static Vector<Network> clients;
	public static Vector<Vector2> positions;
	
	public ServerGame(Vector<ClientNode> clients){
		initNetwork(clients);
		entities = new EntityManager();
		entities.ServerEntityManager(clients);	
	}
	
	public void init() {
		map = new Map();
		gameRunning = true;
		entities.sendServerEntities(clients);
		positions = new Vector<Vector2>();
		for(int i = 0; i < clients.size();i++){
			positions.add(null);
		}
		//System.exit(1);
		//cursor = new CursorManager(w);
		//s = new Screen(Window.WIDTH, Window.HEIGHT);
	}
	
	private static class handleClientMessages implements Runnable{
		public void run(){
			String msg = null;
			while(true){
				for(int i = 0; i < clients.size();i++){
					msg = clients.get(i).getMessage();
					if(msg.equals("null")) positions.set(i, null);
					else{
						String parts[] = msg.split(" ");
						positions.set(i, new Vector2(Float.parseFloat(parts[0]),Float.parseFloat(parts[1])));
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
			 * Tick Controller -limits the # of ticks to TPS. -if FPS < 60, it ticks rather than rendering.
			 */
			boolean ticked = false;
			while (unprocessedSeconds > Time.getBaseTickInterval()) {
				tick();
				unprocessedSeconds -= Time.getBaseTickInterval();
				ticked = true;

				Time.totalTicks++;
				if (Time.totalTicks % 60 == 0) {
					//Window.container.setTitle("PudgeWars (fps:" + fps + ")");
					fps = 0;
				}
			}

			/*
			 * Rendering
			 */
			if (ticked) {
			}
			//render();
			//sendToAll();
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
		for(int x = 0; x < clients.size(); x++){
			for(int y = 0; y < clients.size(); y++){
				if(positions.get(y) != null) clients.get(x).sendMessage(positions.get(y).x + " " + positions.get(y).y);
				else clients.get(x).sendMessage("null");
			}
			clients.get(x).sendMessage("EOM");
		}
		// Entities and Map Update
		//entities.updateEntities();
		//entities.lateUpdateEntities();
		//entities.killUpdateEntities();
		//entities.sendPosition();
		// System.out.println("Entities: " + entities.entities.size());

		//controls();
		//Conn.getMessage();
	}
	
	private void initNetwork(Vector<ClientNode> clients){
		this.clients = new Vector<Network>();
		for(int x = 0; x < clients.size(); x++) this.clients.add(new Network(clients.get(x).getConnection()));
	}
}

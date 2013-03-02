package pudgewars;

import pudgewars.entities.ClientEntityManager;
import pudgewars.input.Keys;
import pudgewars.input.MouseButtons;
import pudgewars.network.ClientNetwork;
import pudgewars.network.MyConnection;
import pudgewars.render.CursorManager;

public class ClientGame extends Game {

	public ClientGame(Window w, Keys k, MouseButtons m, MyConnection conn) {
		super(w, k, m);
		net = new ClientNetwork(conn);
	}

	public void init() {
		super.init();
		cursor = new CursorManager(w);

		entities = new ClientEntityManager();
		entities.generateClientEntities();
		
		Thread t = new Thread(new getEntityData());
		t.start();
	}

	private static class getEntityData implements Runnable {
		public void run() {
			while(true){
				net.getEntityData();
			}
		}
	}
		
	protected void tick() {
		super.tick();
		controls();
	}
}

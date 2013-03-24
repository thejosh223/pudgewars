package pudgewars;

import pudgewars.entities.ClientEntityManager;
import pudgewars.input.Keys;
import pudgewars.input.MouseButtons;
import pudgewars.network.ClientNetwork;
import pudgewars.network.MyConnection;
import pudgewars.render.CursorManager;
import pudgewars.sfx.SoundEffect;

public class ClientGame extends Game {
	private static Object lock = new Object();

	public ClientGame(Window w, Keys k, MouseButtons m, MyConnection conn) {
		super(w, k, m);
		net = new ClientNetwork(conn);
	}

	public void init() {
		super.init();
		cursor = new CursorManager(w);

		entities = new ClientEntityManager();
		entities.generateClientEntities();

		SoundEffect.init();

		Thread t = new Thread(new getEntityData());
		t.start();
	}

	private static class getEntityData implements Runnable {
		public void run() {
			while (true) {
				for (int i = 0; i < 10; i++)
					net.clearBuffer();
				synchronized (lock) {
					net.getEntityData();
				}
			}
		}
	}

	protected void tick() {
		synchronized (lock) {
			super.tick();
			controls();
		}
	}

	protected void postRender() {
		super.postRender();
		entities.sendNetworkData();
	}
}

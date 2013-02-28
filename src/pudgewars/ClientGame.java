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
	}

	protected void tick() {
		// get moveTargets of all players
		net.getMoveTargets();

		// get hookTargets of all players
		net.getHookTargets();

		super.tick();

		controls();
	}
}

package pudgewars.input;

import java.util.ArrayList;
import java.util.List;

public class Keys {
	public final class Key {
		public boolean nextState;
		public boolean wasDown;
		public boolean isDown;

		public Key() {
			keys.add(this);
		}

		public void tick() {
			wasDown = isDown;
			isDown = nextState;
		}

		public boolean wasPressed() {
			return isDown && !wasDown;
		}

		public boolean wasReleased() {
			return !isDown && wasDown;
		}
	}

	private List<Key> keys = new ArrayList<Key>();

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key specialHook = new Key();
	public Key buyMode = new Key();

	public void tick() {
		for (Key k : keys) {
			k.tick();
		}
	}
}

package pudgewars.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import pudgewars.input.Keys.Key;

public class KeyHandler implements KeyListener {
	private HashMap<Integer, Key> keys = new HashMap<Integer, Key>();

	public KeyHandler(Keys k) {
		keys.put(KeyEvent.VK_W, k.up);
		keys.put(KeyEvent.VK_UP, k.up);

		keys.put(KeyEvent.VK_S, k.down);
		keys.put(KeyEvent.VK_DOWN, k.down);

		keys.put(KeyEvent.VK_A, k.left);
		keys.put(KeyEvent.VK_LEFT, k.left);

		keys.put(KeyEvent.VK_D, k.right);
		keys.put(KeyEvent.VK_RIGHT, k.right);

		keys.put(KeyEvent.VK_SHIFT, k.specialHook);

		keys.put(KeyEvent.VK_B, k.buyMode);

		keys.put(KeyEvent.VK_SPACE, k.space);
	}

	public void keyPressed(KeyEvent e) {
		toggle(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggle(e.getKeyCode(), false);
	}

	// Useless
	public void keyTyped(KeyEvent e) {
	}

	private void toggle(int key, boolean newState) {
		Key k = keys.get(key);
		if (k != null) {
			k.nextState = newState;
		}
	}
}

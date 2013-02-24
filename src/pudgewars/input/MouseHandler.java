package pudgewars.input;

import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.event.MouseInputListener;

import pudgewars.Window;
import pudgewars.input.MouseButtons.MouseButton;
import pudgewars.util.Vector2;

public class MouseHandler implements MouseInputListener {

	private MouseButtons m;
	private HashMap<Integer, MouseButton> mouseButtons = new HashMap<Integer, MouseButton>();

	public MouseHandler(MouseButtons m) {
		this.m = m;
		mouseButtons.put(MouseEvent.BUTTON1, m.left);
		mouseButtons.put(MouseEvent.BUTTON2, m.middle);
		mouseButtons.put(MouseEvent.BUTTON3, m.right);
	}

	public void mousePressed(MouseEvent e) {
		toggle(e.getButton(), getPositionVector(e));
	}

	public void mouseReleased(MouseEvent e) {
		toggle(e.getButton(), null);
	}

	public void mouseMoved(MouseEvent e) {
		m.mousePosition = new Vector2((double) e.getX() / Window.ACTUAL_WIDTH, (double) e.getY() / Window.ACTUAL_HEIGHT);
	}

	public void mouseExited(MouseEvent e) {
		m.mousePosition = new Vector2(Window.CENTER_X, Window.CENTER_Y);
	}

	private void toggle(int key, Vector2 newState) {
		MouseButton b = mouseButtons.get(key);
		if (b != null) {
			b.nextState = newState;
		}
	}

	private Vector2 getPositionVector(MouseEvent e) {
		return new Vector2((double) e.getX() / Window.ACTUAL_WIDTH, (double) e.getY() / Window.ACTUAL_HEIGHT);
	}

	/*
	 * Not Used!
	 */

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {
	}

}

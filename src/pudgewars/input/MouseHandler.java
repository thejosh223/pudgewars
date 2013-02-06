package pudgewars.input;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import pudgewars.Window;
import pudgewars.util.Vector2;

public class MouseHandler implements MouseInputListener {
	public Vector2 mousePosition;
	public Vector2[] lastClicked;

	public MouseHandler() {
		mousePosition = new Vector2();
		lastClicked = new Vector2[3];
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
		mousePosition.set(Window.CENTER_X, Window.CENTER_Y);
	}

	public void mousePressed(MouseEvent e) {
		// Determine which mouse button was clicked
		int mouseButton = 0;
		if (e.getButton() == MouseEvent.BUTTON1) mouseButton = 0;
		else if (e.getButton() == MouseEvent.BUTTON3) mouseButton = 1;
		else if (e.getButton() == MouseEvent.BUTTON2) mouseButton = 2;
		lastClicked[mouseButton] = new Vector2((double) e.getX() / Window.ACTUAL_WIDTH, (double) e.getY() / Window.ACTUAL_HEIGHT);
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {
	}

	public void mouseMoved(MouseEvent e) {
		mousePosition.set((double) e.getX() / Window.ACTUAL_WIDTH, (double) e.getY() / Window.ACTUAL_HEIGHT);
	}
}

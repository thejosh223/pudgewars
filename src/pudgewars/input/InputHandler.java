package pudgewars.input;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.event.MouseInputListener;

import pudgewars.Game;
import pudgewars.Window;

public class InputHandler implements MouseInputListener, KeyListener {
	public boolean left;
	public boolean right;
	public boolean up;
	public boolean down;
	
	public Point2D lastRightClicked;
	public Point2D lastLeftClicked;
	public Point activePosition = new Point();
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		activePosition.setLocation(Window.CENTER_X, Window.CENTER_Y);
	}

	public void mousePressed(MouseEvent e) {
		double x = Game.focus.getX() - ((Window.CENTER_X - e.getX()) / (double) Game.TILE_SIZE);
		double y = Game.focus.getY() - ((Window.CENTER_Y - e.getY()) / (double) Game.TILE_SIZE);
		if (e.getButton() == MouseEvent.BUTTON1) {
			lastLeftClicked = new Point2D.Double(x, y);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			lastRightClicked = new Point2D.Double(x, y);
			//System.out.println("Tile at (" + x + ", " + y + ")");
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
//		System.out.println("Mouse Moved");
		activePosition.setLocation(e.getPoint());
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				up = true;
				break;
			case KeyEvent.VK_DOWN:
				down = true;
				break;
			case KeyEvent.VK_LEFT:
				left = true;
				break;
			case KeyEvent.VK_RIGHT:
				right = true;
				break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				up = false;
				break;
			case KeyEvent.VK_DOWN:
				down = false;
				break;
			case KeyEvent.VK_LEFT:
				left = false;
				break;
			case KeyEvent.VK_RIGHT:
				right = false;
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

package pudgewars.render;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.util.ImageHandler;
import pudgewars.util.Vector2;

public class GUI {
	public Image font = ImageHandler.get().getImage("font");

	public GUI() {
	}

	public static boolean button(Image i, int x, int y, int width, int height) {
		Game.s.g.drawImage(i, x, y, width, height, null);
		Vector2 v = Game.mouseInput.left.wasReleased();
		if (v != null) {
			if (v.x * Game.s.width > x && v.x * Game.s.width < x + width) {
				if (v.y * Game.s.height > y && v.y * Game.s.height < y + height) {
					// System.out.println("Clickers!" + Math.random());
					return true;
				}
			}
		}
		return false;
	}

	public static void partialHorizontalBar(Image[] i, int x, int y, double frac) {
		GUI.partialHorizontalBar(i, x, y, i[0].getWidth(null), i[0].getHeight(null), frac);
	}

	public static void partialHorizontalBar(Image[] i, int x, int y, int width, int height, double frac) {
		Game.s.g.drawImage(i[0], x, y, x + width, y + height, 0, 0, i[0].getWidth(null), i[0].getHeight(null), null);
		Game.s.g.drawImage(i[1], x, y, x + (int) (width * frac), y + height, 0, 0, (int) (i[0].getWidth(null) * frac), i[0].getHeight(null), null);
	}
}
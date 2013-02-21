package pudgewars.render;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.input.MouseButton;
import pudgewars.util.ImageHandler;
import pudgewars.util.Vector2;

public class GUI {
	public Image font = ImageHandler.get().getImage("font");

	public GUI() {
	}

	public static boolean button(Image i, int x, int y, int width, int height) {
		Game.s.g.drawImage(i, x, y, width, height, null);
		Vector2 v = Game.mouseInput.getMouseClicked(MouseButton.LEFT);
		if (v != null) {
			if (v.x * Game.s.width > x && v.x * Game.s.width < x + width) {
				if (v.y * Game.s.height > y && v.y * Game.s.height < y + height) {
					System.out.println("Clickers!");
					return true;
				}
			}
		}
		return false;
	}
}

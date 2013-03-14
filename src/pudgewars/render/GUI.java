package pudgewars.render;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.util.ImageHandler;
import pudgewars.util.Vector2;

public class GUI {
	public static Image font_black = ImageHandler.get().getImage("font_black");
	public static Image font_red = ImageHandler.get().getImage("font_red");
	public static Image font_grey = ImageHandler.get().getImage("font_grey");

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

	public static void partialVerticalBar(Image[] i, int x, int y, double frac) {
		GUI.partialVerticalBar(i, x, y, i[0].getWidth(null), i[0].getHeight(null), frac);
	}

	public static void partialVerticalBar(Image[] i, int x, int y, int width, int height, double frac) {
		Game.s.g.drawImage(i[0], x, y, x + width, y + height, 0, 0, i[0].getWidth(null), i[0].getHeight(null), null);
		if (frac < 0) {
			// Bottom Going Up
			frac *= -1;
			Game.s.g.drawImage(i[1], x, y + (int) (height * (1 - frac)), x + width, y + height, 0, (int) (i[0].getHeight(null) * (1 - frac)), (int) i[0].getWidth(null), i[0].getHeight(null), null);
		} else {
			// Top Going Down
			Game.s.g.drawImage(i[1], x, y, x + width, y + (int) (height * frac), 0, 0, (int) i[0].getWidth(null), (int) (i[0].getHeight(null) * frac), null);
		}
	}
	
	public static void showText(String text, TextSize size, TextColor color, int x, int y){
		for(int i = 0; i < text.length(); i++) {
			Image img = (color == TextColor.black) ? font_black : (color == TextColor.red) ? font_red : font_grey; 
			Game.s.g.drawImage(img, x + (6*i), y, x + (6*i) + 6, y + 8, (text.charAt(i)%16)*9, (int) (text.charAt(i)/16)*9, (text.charAt(i)%16)*9 + 6, (int) (text.charAt(i)/16)*9 + 8, null);
		}
	}
}
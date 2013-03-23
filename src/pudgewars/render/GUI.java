package pudgewars.render;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.util.ImageHandler;
import pudgewars.util.Vector2;

public class GUI {
	public static Image font_black = ImageHandler.get().getImage("font_black");
	public static Image font_red = ImageHandler.get().getImage("font_red");
	public static Image font_grey = ImageHandler.get().getImage("font_grey");

	public static Image[] numbers;
	public static Image[] leftNumbers;
	public static Image[] rightNumbers;
	public static Image leftDash;
	public static Image rightDash;
	public static Image leftBorder;
	public static Image rightBorder;
	// public static Image dash;

	public final static int BORDER_LEFTPADDING = 4;

	static {
		numbers = new Image[6];
		for (int i = 0; i < numbers.length; i++)
			numbers[i] = ImageHandler.get().getImage("numbers", i, 0, 16, 16);

		// Left Numbers
		leftDash = ImageHandler.get().getImage("score/left_dash");
		leftBorder = ImageHandler.get().getImage("score/left_back");
		leftNumbers = new Image[6];
		for (int i = 0; i < leftNumbers.length; i++)
			leftNumbers[i] = ImageHandler.get().getImage("score/left_numbers", i, 0, 16, 16);

		rightDash = ImageHandler.get().getImage("score/right_dash");
		rightBorder = ImageHandler.get().getImage("score/right_back");
		rightNumbers = new Image[6];
		for (int i = 0; i < leftNumbers.length; i++)
			rightNumbers[i] = ImageHandler.get().getImage("score/right_numbers", i, 0, 16, 16);

		// dash = ImageHandler.get().getImage("score/dash");
	}

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

	public static void showTally(int val, int x, int y, boolean left) {
		Image[] nums = left ? leftNumbers : rightNumbers;
		Image dash = left ? leftDash : rightDash;
		Image border = left ? leftBorder : rightBorder;
		Game.s.g.drawImage(border, x, y, null);

		x += BORDER_LEFTPADDING;

		for (int i = 0; val > 0; val -= 5, i++) {
			if (val >= 5) {
				Game.s.g.drawImage(nums[nums.length - 1], x + i * (nums[0].getWidth(null) + dash.getWidth(null)), y, null);
				Game.s.g.drawImage(dash, x + i * (nums[0].getWidth(null) + dash.getWidth(null)) + nums[0].getWidth(null), y, null);
			} else {
				Game.s.g.drawImage(nums[val], x + i * (nums[0].getWidth(null) + dash.getWidth(null)), y, null);
			}
		}

	}

	public static void showText(String text, TextSize size, TextColor color, int x, int y) {
		for (int i = 0; i < text.length(); i++) {
			Image img = (color == TextColor.black) ? font_black : (color == TextColor.red) ? font_red : font_grey;
			Game.s.g.drawImage(img, x + (6 * i), y, x + (6 * i) + 6, y + 8, (text.charAt(i) % 16) * 9, (int) (text.charAt(i) / 16) * 9, (text.charAt(i) % 16) * 9 + 6, (int) (text.charAt(i) / 16) * 9 + 8, null);
		}
	}
}
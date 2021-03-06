package pudgewars;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pudgewars.util.Vector2;

public class Screen {
	public int width;
	public int height;

	// Center Coordinates
	public int cx;
	public int cy;

	// The image that is drawn to.
	// -This image is drawn to the Canvas all at once.
	private BufferedImage img;

	// The Graphics2D object of [img]
	// -Not needed, but you dont have to cast img.getGraphics() to Graphics2D everytime.
	public Graphics2D g;

	public Screen(int width, int height) {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) img.getGraphics();
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		this.width = width;
		this.height = height;

		cx = width / 2;
		cy = height / 2;
	}

	/*
	 * Drawing Functions
	 */
	public void drawToGraphics(Graphics2D g) {
		g.drawImage(img, 0, 0, Game.w.actualWidth, Game.w.actualHeight, null);
	}

	public void clear() {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	/*
	 * Screen Conversions
	 */
	public Vector2 screenToWorldPoint(Vector2 v) {
		return screenToWorldPoint(v.x, v.y);
	}

	public Vector2 screenToWorldPoint(double x, double y) {
		return new Vector2(Game.focus.x - ((cx - x * width) / (double) Game.TILE_SIZE),//
				Game.focus.y - ((cy - y * height) / (double) Game.TILE_SIZE));
	}

	public Vector2 worldToScreenPoint(Vector2 v) {
		return new Vector2((int) (Window.CENTER_X - (Game.focus.x - v.x) * Game.TILE_SIZE), (int) (Window.CENTER_Y - (Game.focus.y - v.y) * Game.TILE_SIZE));
	}
}

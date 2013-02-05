package pudgewars;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Screen {
	// Center Coordinates
	private int cx;
	private int cy;

	// The image that is drawn to.
	// -This image is drawn to the Canvas all at once.
	public BufferedImage img;

	// The Graphics2D object of [img]
	// -Not needed, but you dont have to cast img.getGraphics() to Graphics2D everytime.
	private Graphics2D g;

	// The center of the screen (in game units).
	private Point2D focus;

	public Screen(int width, int height) {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		cx = width / 2;
		cy = height / 2;

		focus = new Point2D.Double(0, 0);
	}

	// -keep all images saved here somehow...
	public void draw(Image i, double x, double y) {
		g.drawImage(i, //
				(int) Math.round((x - focus.getX()) * Game.TILE_SIZE) + cx - i.getWidth(null) / 2, //
				(int) Math.round((y - focus.getY()) * Game.TILE_SIZE) + cy - i.getHeight(null) / 2, //
				null);
	}

	public void clear() {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	// Helper Function!
	// -changes the focus of the image.
	// -must be called at the beginning of
	public void setFocus(double x, double y) {
		focus.setLocation(x, y);
	}
}

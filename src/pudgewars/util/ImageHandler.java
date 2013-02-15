package pudgewars.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageHandler {
	private static ImageHandler singleton = new ImageHandler();
	private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	public static ImageHandler get() {
		return singleton;
	}

	// Method: getImage(String ref)
	// Returns: the image found at the reference
	// -Loads an image using ImageHandler.loadImage(String ref)
	public BufferedImage getImage(String ref) {
		BufferedImage i = images.get(ref);
		if (images.get(ref) != null) {
			return images.get(ref);
		}

		i = loadImage("img/" + ref + ".png");
		images.put(ref, i);

		return i;
	}

	// Method: getImage(String ref, int x, int y)
	// Returns: the tile at (x, y) from the reference
	// -Loads an image using getImage(String ref);
	// -Splits an image using ImageHandler.splitImage(BufferedImage i, int tileSize)
	public BufferedImage getImage(String ref, int x, int y, int width, int height) {
		BufferedImage i = images.get(ref + ":" + width + "x" + height + "]" + x + "," + y);
		if (i != null) return i;

		BufferedImage[][] t = splitImage(get().getImage(ref), width, height);
		for (int ty = 0; ty < t.length; ty++)
			for (int tx = 0; tx < t[0].length; tx++)
				images.put(ref + ":" + width + "x" + height + "]" + tx + "," + ty, t[ty][tx]);

		return images.get(ref + ":" + width + "x" + height + "]" + x + "," + y);
	}

	public BufferedImage addImage(String ref, BufferedImage b) {
		images.put(ref, b);
		return b;
	}

	public static BufferedImage loadImage(String ref) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(ref));
		} catch (Exception e) {
			return null;
		}
		return bimg;
	}

	public static BufferedImage[][] splitImage(BufferedImage img, int width, int height) {
		int cols = img.getWidth() / width;
		int rows = img.getHeight() / height;

		BufferedImage imgs[][] = new BufferedImage[rows][cols];
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				imgs[y][x] = new BufferedImage(width, height, img.getType());
				Graphics2D g = imgs[y][x].createGraphics();
				g.drawImage(img, 0, 0, width, height, width * x + x, height * y + y, width * x + width + x, height * y + height + y, null);
				g.dispose();
			}
		}
		return imgs;
	}
}
package pudgewars.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

		i = ImageHandler.makeColorTransparent(loadImage("img/" + ref + ".png"), null);
		// i = ImageHandler.changeHSL(i, 0.1f, 0.1f);
		images.put(ref, i);

		return i;
	}

	// Method: getImage(String ref, int x, int y)
	// Returns: the tile at (x, y) from the reference
	// -Loads an image using getImage(String ref);
	// -Splits an image using ImageHandler.splitImage(BufferedImage i, int tileSize)
	public BufferedImage getImage(String ref, int x, int y, int tileSize) {
		BufferedImage i = images.get(ref + ":" + tileSize + "]" + x + "," + y);
		if (i != null) {
			return i;
		}

		BufferedImage[][] t = splitImage(get().getImage(ref), tileSize);
		for (int ty = 0; ty < t.length; ty++) {
			for (int tx = 0; tx < t[0].length; tx++) {
				images.put(ref + ":" + tileSize + "]" + tx + "," + ty, t[ty][tx]);
			}
		}

		return images.get(ref + ":" + tileSize + "]" + x + "," + y);
	}

	// Method: getImage(String ref, int x, int y, int mult)
	// Returns: the resized tile at (x, y) from the reference
	// -Splits an image using getImage(String ref, int x, int y, int tileSize)
	// -Resizes the tile using ImageHandler.resize(BufferedImage i, multiple)
	public BufferedImage getImage(String ref, int x, int y, int tileSize, int mult) {
		BufferedImage i = images.get(ref + ":" + tileSize + "]" + x + "," + y + "x" + mult);
		if (i != null) {
			return i;
		}

		i = resize(get().getImage(ref, x, y, tileSize), mult);
		images.put(ref + ":" + tileSize + "]" + x + "," + y + "x" + mult, i);
		return i;
	}

	// Method: getImage(String ref, int x, int y, int tileSize, int trim)
	// Returns: the resized tile (with custom tile size) at (x, y) after it has been trimmed (on the right) from the reference
	// -Splits an image using getImage(String ref, int x, int y)
	public BufferedImage getImage(String ref, int x, int y, int tileSize, int mult, int trim) {
		BufferedImage i = images.get(ref + ":" + tileSize + "]" + x + "," + y + "x" + mult + "-" + trim);
		if (i != null) {
			return i;
		}

		i = ImageHandler.trimRight(get().getImage(ref, x, y, tileSize, mult), trim);
		images.put(ref + ":" + tileSize + "]" + x + "," + y + "x" + mult + "-" + trim, i);

		return i;
	}

	public BufferedImage getImage(String ref, int sizeMultiple) {
		BufferedImage i = images.get(ref + "x" + sizeMultiple);
		if (i != null) {
			return i;
		}

		i = ImageHandler.makeColorTransparent(resize(ImageHandler.get().getImage(ref), sizeMultiple), null);
		// i = ImageHandler.changeHSL(i, 0.1f, 0.1f);
		images.put(ref + "x" + sizeMultiple, i);

		return i;
	}

	public BufferedImage addImage(String ref, BufferedImage b) {
		images.put(ref, b);
		return b;
	}

	public int getSplitImageColumns(String ref, int tileSize) {
		BufferedImage t_img = get().getImage(ref);
		return t_img.getWidth() / (tileSize + 1);
	}

	public int getSplitImageHeight(String ref, int tileSize) {
		BufferedImage t_img = get().getImage(ref);
		return t_img.getHeight() / (tileSize + 1);
	}

	public static void saveImage(BufferedImage b, String out) {
		try {
			File outputfile = new File(out);
			ImageIO.write(b, "png", outputfile);
		} catch (Exception e) {
		}
	}

	public static BufferedImage loadImage(String ref) {
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(new File(ref));
		} catch (Exception e) {
			return null;
			// e.printStackTrace();
		}
		return bimg;
	}

	public static BufferedImage[] horizontalflip(BufferedImage[] img) {
		for (int i = 0; i < img.length; i++) {
			img[i] = horizontalflip(img[i]);
		}
		return img;
	}

	public static BufferedImage horizontalflip(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();
		return dimg;
	}

	public static BufferedImage[] verticalflip(BufferedImage[] img) {
		for (int i = 0; i < img.length; i++) {
			img[i] = verticalflip(img[i]);
		}
		return img;
	}

	public static BufferedImage verticalflip(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
		Graphics2D g = dimg.createGraphics();
		g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
		g.dispose();
		return dimg;
	}

	public static BufferedImage[] rotate(BufferedImage[] img, int angle) {
		for (int i = 0; i < img.length; i++) {
			rotate(img[i], angle);
		}
		return img;
	}

	public static BufferedImage rotate(BufferedImage img, int angle) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawImage(img, null, 0, 0);
		return dimg;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}

	public static BufferedImage trimRight(BufferedImage img, int pix) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w - pix, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return dimg;
	}

	public static BufferedImage resize(BufferedImage img, int mult) {
		BufferedImage dimg = new BufferedImage(img.getWidth() * mult, img.getHeight() * mult, img.getType());

		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				for (int repeatY = 0; repeatY < mult; repeatY++) {
					for (int repeatX = 0; repeatX < mult; repeatX++) {
						dimg.setRGB(x * mult + repeatX, y * mult + repeatY, img.getRGB(x, y));
					}
				}
			}
		}

		return dimg;
	}

	public static BufferedImage makeColorTransparent(BufferedImage img, Color color) {
		BufferedImage dimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

		if (color == null) {
			color = new Color(250, 100, 250);
		}

		Graphics2D g = dimg.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(img, null, 0, 0);
		g.dispose();
		for (int i = 0; i < dimg.getHeight(); i++) {
			for (int j = 0; j < dimg.getWidth(); j++) {
				if (dimg.getRGB(j, i) == color.getRGB()) {
					dimg.setRGB(j, i, 0x8F1C1C);
				}
			}
		}
		return dimg;
	}

	public static BufferedImage[][] splitImage(BufferedImage img, int size) {
		int cols = img.getWidth() / size;
		int rows = img.getHeight() / size;
		// int w = img.getWidth() / cols;
		// int h = img.getHeight() / rows;

		BufferedImage imgs[][] = new BufferedImage[rows][cols];
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				imgs[y][x] = new BufferedImage(size, size, img.getType());
				// Tell the graphics to draw only one block of the image
				Graphics2D g = imgs[y][x].createGraphics();
				g.drawImage(img, 0, 0, size, size, size * x + x, size * y + y, size * x + size + x, size * y + size + y, null);
				g.dispose();
			}
		}
		return imgs;
	}

	public static BufferedImage changeHSL(BufferedImage img, float lum, float sat) {
		BufferedImage dimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Color c;
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				c = new Color(img.getRGB(x, y), true);
				if (c.getAlpha() != 0) {
					int[] n_rgb = subtractSatLum(c.getRed(), c.getGreen(), c.getBlue(), lum, sat);
					// System.out.println("R:" + n_rgb[0] + " G:" + n_rgb[1] + " B:" + n_rgb[2] + ") " + x + ", " + y);
					c = new Color(n_rgb[0], n_rgb[1], n_rgb[2]);
					dimg.setRGB(x, y, c.getRGB());
				}
			}
		}

		return dimg;
	}

	private static int[] subtractSatLum(int r, int g, int b, float sat, float lum) {
		float[] hsl = rgb2hsl(r, g, b);
		hsl[1] = hsl[1] - (int) (hsl[1] * sat);
		hsl[2] = hsl[2] - (int) (hsl[2] * lum);
		// System.out.println("H:" + hsl[0] + " S:" + hsl[1] + " L:" + hsl[2]);
		int[] rgb = hsl2rgb(hsl[0], hsl[1], hsl[2]);
		return rgb;
	}

	// Taken From http://www.f4.fhtw-berlin.de/~barthel/ImageJ/ColorInspector//HTMLHelp/farbraumJava.htm
	private static float[] rgb2hsl(int r, int g, int b) {
		float hsl[] = new float[3];
		float var_R = (r / 255f);
		float var_G = (g / 255f);
		float var_B = (b / 255f);

		float var_Min; // Min. value of RGB
		float var_Max; // Max. value of RGB
		float del_Max; // Delta RGB value

		if (var_R > var_G) {
			var_Min = var_G;
			var_Max = var_R;
		} else {
			var_Min = var_R;
			var_Max = var_G;
		}

		if (var_B > var_Max) var_Max = var_B;
		if (var_B < var_Min) var_Min = var_B;

		del_Max = var_Max - var_Min;

		float H = 0, S, L;
		L = (var_Max + var_Min) / 2f;

		if (del_Max == 0) {
			H = 0;
			S = 0;
		} // gray
		else { // Chroma
			if (L < 0.5) S = del_Max / (var_Max + var_Min);
			else S = del_Max / (2 - var_Max - var_Min);

			float del_R = (((var_Max - var_R) / 6f) + (del_Max / 2f)) / del_Max;
			float del_G = (((var_Max - var_G) / 6f) + (del_Max / 2f)) / del_Max;
			float del_B = (((var_Max - var_B) / 6f) + (del_Max / 2f)) / del_Max;

			if (var_R == var_Max) H = del_B - del_G;
			else if (var_G == var_Max) H = (1 / 3f) + del_R - del_B;
			else if (var_B == var_Max) H = (2 / 3f) + del_G - del_R;
			if (H < 0) H += 1;
			if (H > 1) H -= 1;
		}
		hsl[0] = (int) (360 * H);
		hsl[1] = (int) (S * 100);
		hsl[2] = (int) (L * 100);

		return hsl;
	}

	private static int[] hsl2rgb(float h, float s, float l) {
		int[] rgb = new int[3];

		h = h / 60f;
		s = s / 100f;
		l = l / 100f;

		float C = (1f - Math.abs(2 * l - 1f)) * s;
		float X = C * (1f - Math.abs((h % 2) - 1f));

		float[] t_rgb = new float[3];
		if (0 <= h && h < 1) {
			t_rgb[0] = C;
			t_rgb[1] = X;
			t_rgb[2] = 0;
		} else if (1 <= h && h < 2) {
			t_rgb[0] = X;
			t_rgb[1] = C;
			t_rgb[2] = 0;
		} else if (2 <= h && h < 3) {
			t_rgb[0] = 0;
			t_rgb[1] = C;
			t_rgb[2] = X;
		} else if (3 <= h && h < 4) {
			t_rgb[0] = 0;
			t_rgb[1] = X;
			t_rgb[2] = C;
		} else if (4 <= h && h < 5) {
			t_rgb[0] = X;
			t_rgb[1] = 0;
			t_rgb[2] = C;
		} else if (5 <= h && h < 6) {
			t_rgb[0] = C;
			t_rgb[1] = 0;
			t_rgb[2] = X;
		}

		float m = l - 0.5f * C;

		rgb[0] = (int) ((t_rgb[0] + m) * 255);
		rgb[1] = (int) ((t_rgb[1] + m) * 255);
		rgb[2] = (int) ((t_rgb[2] + m) * 255);

		return rgb;
	}
	//
	// public static void main(String[] args) {
	// rgb2hsl(56,200,72);
	// hsl2rgb(126.5f, 56.25f, 50f);
	// }
}
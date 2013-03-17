package pudgewars.render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pudgewars.Game;

public class ArcImage {

	public BufferedImage empty;
	public BufferedImage full;
	public BufferedImage toRender;
	public double angle;

	public ArcImage(BufferedImage empty, BufferedImage full) {
		this.empty = empty;
		this.full = full;
		this.angle = Math.PI;

		updateImage();
	}

	public void updateImage() {
		toRender = new BufferedImage(empty.getWidth(), empty.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) toRender.getGraphics();

		g.drawImage(full, 0, 0, full.getWidth(), full.getHeight(), null);
		g.setComposite(AlphaComposite.SrcOut);
		g.setColor(new Color(255, 255, 255, 0));
		g.fillArc(-toRender.getWidth(), -toRender.getHeight(), toRender.getWidth() * 3, toRender.getHeight() * 3, 90, -(360 - (int) Math.toDegrees(angle)));
		// System.out.println("Update Angle: " + Math.toDegrees(angle));
	}

	public void renderCenteredAt(int x, int y, double angle) {
		if (angle != this.angle) {
			this.angle = angle;
			updateImage();
		}
		Game.s.g.drawImage(empty, x - empty.getWidth() / 2, y - empty.getHeight() / 2, empty.getWidth(), empty.getHeight(), null);
		Game.s.g.drawImage(toRender, x - toRender.getWidth() / 2, y - toRender.getHeight() / 2, toRender.getWidth(), toRender.getHeight(), null);
	}

	public void renderCenteredAt(int x, int y) {
		Game.s.g.drawImage(empty, x - empty.getWidth() / 2, y - empty.getHeight() / 2, empty.getWidth(), empty.getHeight(), null);
	}
}

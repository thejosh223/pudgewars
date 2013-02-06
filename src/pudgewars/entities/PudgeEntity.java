package pudgewars.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class PudgeEntity extends Entity {
	public final static double MOVEMENT_UNCERTAINTY = 0.05;
	public final static int MAX_LIFE = 20;

	protected Vector2 target;
	protected boolean hooked;
	protected int life;

	protected Image img;

	public PudgeEntity(double x, double y) {
		super(x, y);

		life = 20;

		collisionWidth = 0.8;
		collisionHeight = 0.8;

		speed = 3.8f;

		img = ImageHandler.get().getImage("pudge");
	}

	public void update() {
		transform.rotation += 0.08;

		if (target != null) {
			if (transform.position.x >= target.x - MOVEMENT_UNCERTAINTY / 2 && transform.position.x < target.x + MOVEMENT_UNCERTAINTY / 2 //
					&& transform.position.y >= target.y - MOVEMENT_UNCERTAINTY / 2 && transform.position.y < target.y + MOVEMENT_UNCERTAINTY / 2) {

				setVerticalMovement(0);
				setHorizontalMovement(0);
				target = null;
			} else {
				setDirection(target);
			}
		}

		vx += ax * Time.getTickInterval();
		vy += ay * Time.getTickInterval();

		double tx = transform.position.x + vx * Time.getTickInterval();
		double ty = transform.position.y + vy * Time.getTickInterval();

		/*
		 * TODO:
		 * Entity collisions are handled in priority to world collisions.
		 */
		Entity te = isEntityCollision(tx, ty);
		if (te != null) {
			// tx = x;
			// ty = y;
		} else {
			if (Game.map.isCollides(tx, ty, this)) {
				boolean xCol = Game.map.isCollides(tx, transform.position.y, this);
				boolean yCol = Game.map.isCollides(transform.position.x, ty, this);
				if (xCol && !yCol) {
					setHorizontalMovement(0);
					tx = transform.position.x;
				} else if (yCol && !xCol) {
					setVerticalMovement(0);
					ty = transform.position.y;
				} else {
					setHorizontalMovement(0);
					setVerticalMovement(0);

					ty = transform.position.y;
					tx = transform.position.x;
					target = null;
				}
			}
		}

		transform.position.x = tx;
		transform.position.y = ty;

		// Un-comment this to have some fun!
		removeHook();
	}

	public final static int CLICK_SIZE = Game.TILE_SIZE / 4;
	final static int LIFESTROKE_DEPTH = 3;
	final static BasicStroke LIFESTROKE = new BasicStroke(LIFESTROKE_DEPTH);

	final static int ARC_STARTANGLE = 135;
	final static int FULL_LIFE_ARC = 180;

	public void render() {
		int x = (int) (Window.CENTER_X - (Game.focus.x - this.transform.position.x) * Game.TILE_SIZE);
		int y = (int) (Window.CENTER_Y - (Game.focus.y - this.transform.position.y) * Game.TILE_SIZE);

		// Game.s.g.setColor(Color.DARK_GRAY);
		// Game.s.g.fillOval((int) (x - (Game.TILE_SIZE * collisionWidth) / 2), (int) (y - (Game.TILE_SIZE * collisionHeight) / 2), (int) (Game.TILE_SIZE * collisionWidth), (int) (Game.TILE_SIZE * collisionHeight));
		// Game.s.g.drawImage(img, (int) (x - (Game.TILE_SIZE * collisionWidth) / 2), //
		// (int) (y - (Game.TILE_SIZE * collisionHeight) / 2), //
		// (int) (Game.TILE_SIZE * collisionWidth), //
		// (int) (Game.TILE_SIZE * collisionHeight), null);

		AffineTransform a = new AffineTransform();
		a.rotate(transform.rotation, x, y);
		a.translate((int) (x - (Game.TILE_SIZE * collisionWidth) / 2), (int) (y - (Game.TILE_SIZE * collisionHeight) / 2));
		a.scale(transform.scale.x, transform.scale.y);
		Game.s.g.drawImage(img, a, null);

		if (target != null) {
			int tx = (int) (Window.CENTER_X - ((Game.focus.x - target.x) * Game.TILE_SIZE));
			int ty = (int) (Window.CENTER_Y - ((Game.focus.y - target.y) * Game.TILE_SIZE));
			Game.s.g.setColor(Color.LIGHT_GRAY);
			// g.setStroke(new BasicStroke(Game.SCALE));
			Game.s.g.fillOval((int) (tx - CLICK_SIZE / 2), (int) (ty - CLICK_SIZE / 2), CLICK_SIZE, CLICK_SIZE);
		}

		/*
		 * LIFE DRAWING
		 */

		// Dimension Definitions!
		int ellipseWidth = (int) (Game.TILE_SIZE * collisionWidth);
		int ellipseHeight = (int) (Game.TILE_SIZE * collisionHeight);
		int lifeEllipseWidth = ellipseWidth + 2 * (LIFESTROKE_DEPTH + 1);
		int lifeEllipseHeight = ellipseHeight + 2 * (LIFESTROKE_DEPTH + 1);

		Game.s.g.setStroke(LIFESTROKE);
		Game.s.g.setPaint(new GradientPaint((float) (x - lifeEllipseWidth * 0.5), (float) (y - lifeEllipseHeight * 0.5), new Color(250, 0, 0, 175), (float) (x + lifeEllipseWidth * 0.5), (float) (y - lifeEllipseHeight * 0.5), new Color(0, 0, 250, 175)));

		// Game.s.g.draw(new Arc2D.Double(x - lifeEllipseWidth * 0.5, y - lifeEllipseHeight * 0.5, lifeEllipseWidth, lifeEllipseHeight, ARC_STARTANGLE, FULL_LIFE_ARC * this.getLife() / PudgeEntity.MAX_LIFE, Arc2D.OPEN));
	}

	public void setTarget(Vector2 target) {
		this.target = target;
	}

	public void removeHook() {
		hooked = false;
	}

	public void setHook(Vector2 click) {
		if (!hooked) {
			Entity e = new HookEntity(this, click);
			Game.entities.add(e);
			hooked = true;
		}
	}

	public void try_setPosition(double tx, double ty) {
		Entity te = isEntityCollision(tx, ty);
		if (te != null) {
			if (te instanceof PudgeEntity) {
				tx = transform.position.x;
				ty = transform.position.y;
				return;
			}
		}
		transform.position.x = tx;
		transform.position.y = ty;
	}

	public void collides(Entity e) {
	}

	public void subLife(int sub) {
		life -= sub;
		if (life <= 0) {
			kill();
		}
	}

	public int getLife() {
		return life;
	}

	public void kill() {
		Game.entities.remove(this);
		System.out.println("Pudge was Killed");
	}
}

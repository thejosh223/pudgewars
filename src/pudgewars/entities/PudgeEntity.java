package pudgewars.entities;

import java.awt.BasicStroke;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import pudgewars.Game;
import pudgewars.input.MouseButton;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class PudgeEntity extends Entity {
	public final static int CLICK_SIZE = 8;
	public final static double MOVEMENT_UNCERTAINTY = 0.05;
	public final static int MAX_LIFE = 20;

	public final static double COLLISION_WIDTH = 1;
	public final static double COLLISION_HEIGHT = 1;

	public boolean controllable = false;

	protected boolean hooked;
	protected int life;

	protected Image img;

	// Target Position
	protected Image clicker;
	protected Vector2 target;
	protected double targetRotation;

	public PudgeEntity(Vector2 position) {
		super(position, new Vector2(COLLISION_WIDTH, COLLISION_HEIGHT));

		life = 20;

		rigidbody.speed = 3.8;

		img = ImageHandler.get().getImage("pudge");

		clicker = ImageHandler.get().getImage("selector");
		target = null;
	}

	public void update() {
		// Controls
		if (controllable) {
			// System.out.println("Pudge Position: " + transform.position.x + ", " + transform.position.y);

			if (Game.mouseInput.lastClicked[MouseButton.RIGHT] != null) {
				Vector2 click = Game.mouseInput.lastClicked[MouseButton.RIGHT];
				target = Game.s.screenToWorldPoint(click);

				Game.mouseInput.lastClicked[MouseButton.RIGHT] = null;
			}
			if (Game.mouseInput.lastClicked[MouseButton.LEFT] != null) {
				Vector2 click = Game.mouseInput.lastClicked[MouseButton.LEFT];
				this.setHook(Game.s.screenToWorldPoint(click));

				Game.mouseInput.lastClicked[MouseButton.LEFT] = null;
			}

			// Rotate the Clicker
			if (target != null) targetRotation += -0.1;
		}

		if (target != null) {
			if (transform.position.x >= target.x - MOVEMENT_UNCERTAINTY / 2 && transform.position.x < target.x + MOVEMENT_UNCERTAINTY / 2 //
					&& transform.position.y >= target.y - MOVEMENT_UNCERTAINTY / 2 && transform.position.y < target.y + MOVEMENT_UNCERTAINTY / 2) {

				setVerticalMovement(0);
				setHorizontalMovement(0);
				target = null;
			} else {
				rigidbody.setDirection(target);
			}
		}

		rigidbody.updateVelocity();

		double tx = transform.position.x + rigidbody.velocity.x * Time.getTickInterval();
		double ty = transform.position.y + rigidbody.velocity.y * Time.getTickInterval();

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

	final static int LIFESTROKE_DEPTH = 3;
	final static BasicStroke LIFESTROKE = new BasicStroke(LIFESTROKE_DEPTH);

	final static int ARC_STARTANGLE = 135;
	final static int FULL_LIFE_ARC = 180;

	public void render() {
		// Draw Pudge
		Game.s.g.drawImage(img, transform.getAffineTransformation(), null);

		if (target != null) {
			Vector2 targetLocation = Game.s.worldToScreenPoint(target);
			AffineTransform a = new AffineTransform();
			a.translate((int) (targetLocation.x - CLICK_SIZE / 2), (int) (targetLocation.y - CLICK_SIZE / 2));
			a.rotate(targetRotation, CLICK_SIZE / 2, CLICK_SIZE / 2);
			Game.s.g.drawImage(clicker, a, null);
		}

		/*
		 * LIFE DRAWING
		 */

		// Dimension Definitions!
		// int ellipseWidth = (int) (Game.TILE_SIZE * collisionWidth);
		// int ellipseHeight = (int) (Game.TILE_SIZE * collisionHeight);
		// int lifeEllipseWidth = ellipseWidth + 2 * (LIFESTROKE_DEPTH + 1);
		// int lifeEllipseHeight = ellipseHeight + 2 * (LIFESTROKE_DEPTH + 1);

		// Game.s.g.setStroke(LIFESTROKE);
		// Game.s.g.setPaint(new GradientPaint((float) (x - lifeEllipseWidth * 0.5), (float) (y - lifeEllipseHeight * 0.5), new Color(250, 0, 0, 175), (float) (x + lifeEllipseWidth * 0.5), (float) (y - lifeEllipseHeight * 0.5), new Color(0, 0, 250, 175)));

		// Game.s.g.draw(new Arc2D.Double(x - lifeEllipseWidth * 0.5, y - lifeEllipseHeight * 0.5, lifeEllipseWidth, lifeEllipseHeight, ARC_STARTANGLE, FULL_LIFE_ARC * this.getLife() / PudgeEntity.MAX_LIFE, Arc2D.OPEN));
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

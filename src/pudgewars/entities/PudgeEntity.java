package pudgewars.entities;

import java.awt.BasicStroke;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import pudgewars.Game;
import pudgewars.entities.hooks.HookEntity;
import pudgewars.entities.hooks.NormalHookEntity;
import pudgewars.input.MouseButton;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class PudgeEntity extends Entity {
	public final static int CLICK_SIZE = 8;
	public final static int MAX_LIFE = 20;

	public final static double COLLISION_WIDTH = 1;
	public final static double COLLISION_HEIGHT = 1;

	// Whether or not you can control this Pudge
	public boolean controllable = false;

	public boolean isHooking;
	public NormalHookEntity attachedHook;
	protected int life;

	protected Image img;

	// Target Position
	protected Image clicker;
	protected Vector2 target;
	protected double targetRotation;

	public PudgeEntity(Vector2 position) {
		super(position, new Vector2(COLLISION_WIDTH, COLLISION_HEIGHT));

		life = 20;

		rigidbody.physicsSlide = true;
		rigidbody.speed = 3.8;

		img = ImageHandler.get().getImage("pudge");

		clicker = ImageHandler.get().getImage("selector");
		target = null;
	}

	public void update() {
		// Controls
		if (controllable) {

			// Change Cursor
			if (Game.keyInput.specialHook.isDown) Game.cursor.setCursor("Special");
			else Game.cursor.setCursor("Default");

			if (Game.mouseInput.lastClicked[MouseButton.RIGHT] != null) {
				Vector2 click = Game.mouseInput.lastClicked[MouseButton.RIGHT];
				target = Game.s.screenToWorldPoint(click);

				Game.mouseInput.lastClicked[MouseButton.RIGHT] = null;
			}
			if (Game.mouseInput.lastClicked[MouseButton.LEFT] != null) {
				Vector2 click = Game.mouseInput.lastClicked[MouseButton.LEFT];

				// Activate Hook
				if (Game.keyInput.specialHook.isDown) this.setHook(Game.s.screenToWorldPoint(click), true);
				else this.setHook(Game.s.screenToWorldPoint(click), false);

				Game.mouseInput.lastClicked[MouseButton.LEFT] = null;
			}

			// Rotate the Clicker
			if (target != null) targetRotation += -0.1;
		}

		if (target != null) {
			transform.rotateTowards(target);

			double dist = transform.position.distance(target);
			if (dist < rigidbody.velocity.magnitude() * Time.getTickInterval()) {
				setVerticalMovement(0);
				setHorizontalMovement(0);
				target = null;
			} else {
				rigidbody.setDirection(target);
			}
		}

		rigidbody.updateVelocity();

		// double tx = transform.position.x + rigidbody.velocity.x *
		// Time.getTickInterval();
		// double ty = transform.position.y + rigidbody.velocity.y *
		// Time.getTickInterval();

		// Un-comment this to have some fun!
		isHooking = false;
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
		// Game.s.g.setPaint(new GradientPaint((float) (x - lifeEllipseWidth *
		// 0.5), (float) (y - lifeEllipseHeight * 0.5), new Color(250, 0, 0,
		// 175), (float) (x + lifeEllipseWidth * 0.5), (float) (y -
		// lifeEllipseHeight * 0.5), new Color(0, 0, 250, 175)));

		// Game.s.g.draw(new Arc2D.Double(x - lifeEllipseWidth * 0.5, y -
		// lifeEllipseHeight * 0.5, lifeEllipseWidth, lifeEllipseHeight,
		// ARC_STARTANGLE, FULL_LIFE_ARC * this.getLife() /
		// PudgeEntity.MAX_LIFE, Arc2D.OPEN));
	}

	public void setHook(Vector2 click, boolean specialHook) {
		if (!isHooking) {
			Entity e = new NormalHookEntity(this, click, specialHook);
			Game.entities.entities.add(e);
			isHooking = true;
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

	/*
	 * Collisions
	 */
	public boolean shouldBlock(BBOwner b) {
		if (b instanceof HookEntity) return true;
		if (b instanceof PudgeEntity) return true;
		if (b instanceof Tile) {
			if (((Tile) b).isPudgeSolid()) return true;
		}
		return false;
	}

	public void collides(Entity e, double vx, double vy) {
		if (e instanceof PudgeEntity) {
			PudgeEntity p = (PudgeEntity) e;
			if (p.attachedHook != null) {
				if (p.attachedHook.owner == this) {
					p.attachedHook.detachPudge();
					p.rigidbody.velocity = Vector2.ZERO.clone();
				}
			}
		}
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
		super.kill();
		System.out.println("Pudge was Killed");
	}
}

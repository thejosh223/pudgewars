package pudgewars.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.util.Animation;
import pudgewars.util.ImageHandler;

public class HookEntity extends Entity {
	public final static double PIECE_DISTANCE = 0.4;
	public final static float HOOK_SPEED = 6;
	public final static double MAX_TRAVEL_DISTANCE = 8;
	public final static double KILL_UNCERTAINTY = 0.1;
	public final static float HOOK_REVERSE_SPEED = 2 * HOOK_SPEED;

	private PudgeEntity owner;
	private PudgeEntity hooked;
	private Animation ani;

	private double travelled;
	private double maxTravelDistance;
	private boolean reversing;
	private HookPieceEntity hookPiece;
	private int damage;

	public HookEntity(PudgeEntity e, Point2D target) {
		super(e.getX(), e.getY());
		owner = e;
		hooked = null;

		damage = 4;

		maxTravelDistance = 12;
		ani = new Animation();
		speed = HOOK_SPEED;
		collisionWidth = 0.6;
		collisionHeight = 0.6;

		travelled = 0;

		setDirection(target);

		// Movement Animation
		// int hookMultiple = Game.SCALE - 1;
		int movementInterval = 50;
		int noOfSprites = ImageHandler.get().getSplitImageColumns("tryhook", 16);

		for (int i = 0; i < noOfSprites; i++) {
			ani.add(ImageHandler.get().getImage("tryhook", i, 0, 16), movementInterval);
			ani.startAnimation();
		}
	}

	public final static double SLOW_DOWN_VECTOR = 0.5;

	public void update(long timePassed) {
		ani.update(timePassed);

		double xDist = vx * timePassed / 1000;
		double yDist = vy * timePassed / 1000;
		double tx = x + xDist;
		double ty = y + yDist;

		Entity te = isEntityCollision(tx, ty);
		if (te != null) {
			te.collides(this);
			collides(te);
		}
		boolean[] collisions = isWorldCollision(tx, ty);
		if (collisions[0] || collisions[1]) {
			maxTravelDistance -= maxTravelDistance * SLOW_DOWN_VECTOR / 4;
			if (collisions[0] && !collisions[1]) {
				tx = x;
				vx *= -SLOW_DOWN_VECTOR;
				vy *= SLOW_DOWN_VECTOR;
			} else if (collisions[1] && !collisions[0]) {
				ty = y;
				vx *= SLOW_DOWN_VECTOR;
				vy *= -SLOW_DOWN_VECTOR;
			} else {
				ty = y;
				tx = x;

				vx *= -SLOW_DOWN_VECTOR;
				vy *= -SLOW_DOWN_VECTOR;
			}

			HookPieceEntity temp = hookPiece;
			while (temp != null) {
				temp.setSpeed((float) (temp.getSpeed() * SLOW_DOWN_VECTOR));
				temp = temp.getConnected();
			}
		}
		x = tx;
		y = ty;

		travelled += Math.sqrt(xDist * xDist + yDist * yDist);
		if (travelled >= maxTravelDistance) {
			reverse();
		}

		if (!reversing) {
			if (hookPiece == null) {
				if (Point.distance(x, y, owner.getX(), owner.getY()) >= PIECE_DISTANCE) {
					HookPieceEntity e = new HookPieceEntity(owner, vx, vy, speed);
					hookPiece = e;
				}
			} else {
				hookPiece.setDirection(new Point2D.Double(x, y));
			}
		} else {
			if (Point.distance(x, y, owner.getX(), owner.getY()) <= HookEntity.KILL_UNCERTAINTY) {
				kill();
			} else {
				setDirection(new Point2D.Double(hookPiece.getX(), hookPiece.getY()));
			}
		}

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.update(timePassed);
			temp = temp.getConnected();
		}

		/*
		 * Hooked Entity Management
		 */
		if (hooked != null) {
			hooked.try_setPosition(x, y);
		}
	}

	public boolean[] isWorldCollision(double tx, double ty) {
		if (Game.map.isCollides(tx, ty, this)) {
			boolean xCol = Game.map.isCollides(tx, y, this);
			boolean yCol = Game.map.isCollides(x, ty, this);

			return new boolean[] { xCol, yCol };
		}
		return new boolean[] { false, false };
	}

	public void draw(Graphics2D g) {
		Image img = ani.getImage();
		g.drawImage(img, (int) (Window.CENTER_X - (Game.focus.getX() - x) * Game.TILE_SIZE - img.getWidth(null) / 2), (int) (Window.CENTER_Y - (Game.focus.getY() - y) * Game.TILE_SIZE - img.getHeight(null) / 2), null);

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.draw(g);
			temp = temp.getConnected();
		}
	}

	private void reverse() {
		speed = HookEntity.HOOK_REVERSE_SPEED;
		reversing = true;

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.reverse();
			temp = temp.getConnected();
		}
	}

	public void kill() {
		owner.removeHook();
		while (hookPiece != null) {
			hookPiece = hookPiece.getConnected();
		}
		hookPiece = null;

		Game.entities.remove(this);
	}

	public void collides(Entity e) {
		if (hooked == null) {
			if (e instanceof PudgeEntity) {
				// TODO: Check if collision is with ally or enemy
				if (e != owner) {
					hooked = (PudgeEntity) e;
					hooked.subLife(damage);
					reverse();
				}
			}
		}
	}
}

package pudgewars.entities;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HookEntity extends Entity {
	public final static double PIECE_DISTANCE = 1;
	public final static double MAX_TRAVEL_DISTANCE = 8;
	public final static double KILL_UNCERTAINTY = 0.1;

	// Hook Speed
	public final static float HOOK_SPEED = 6;
	public final static float HOOK_REVERSE_SPEED = 2 * HOOK_SPEED;

	private PudgeEntity owner;
	private PudgeEntity hooked;
	// private Animation ani;
	private Image img;

	private double travelled;
	private double maxTravelDistance;
	private boolean reversing;
	private HookPieceEntity hookPiece;
	private int damage;

	public HookEntity(PudgeEntity e, Vector2 target) {
		super(e.transform.position, new Vector2(0.6, 0.6));
		owner = e;
		hooked = null;

		damage = 4;

		maxTravelDistance = 12;
		rigidbody.speed = HOOK_SPEED;

		travelled = 0;

		rigidbody.physicsSlide = false;
		rigidbody.setDirection(target);

		// Movement Animation
		// int hookMultiple = Game.SCALE - 1;
		// double movementInterval = 0.05;
		// int noOfSprites = ImageHandler.get().getSplitImageColumns("tryhook", 16);

		img = ImageHandler.get().getImage("hook");

		// for (int i = 0; i < noOfSprites; i++) {
		// ani.add(ImageHandler.get().getImage("tryhook", i, 0, 16), movementInterval);
		// ani.startAnimation();
		// }
	}

	public final static double SLOW_DOWN_VECTOR = 0.5;

	public void update() {
		// Spinning Animation
		transform.rotation -= 0.5;

		rigidbody.updateVelocity();

		double xDist = rigidbody.velocity.x * Time.getTickInterval();
		double yDist = rigidbody.velocity.y * Time.getTickInterval();

		travelled += Math.sqrt(xDist * xDist + yDist * yDist);
		if (travelled >= maxTravelDistance) {
			reverse();
		}

		if (!reversing) {
			if (hookPiece == null) {
				if (Point.distance(transform.position.x, transform.position.y, owner.getX(), owner.getY()) >= PIECE_DISTANCE) {
					HookPieceEntity e = new HookPieceEntity(owner, rigidbody.velocity.x, rigidbody.velocity.y, rigidbody.speed);
					hookPiece = e;
				}
			} else {
				hookPiece.rigidbody.setDirection(new Vector2(transform.position.x, transform.position.y));
			}
		} else {
			if (Point.distance(transform.position.x, transform.position.y, owner.getX(), owner.getY()) <= HookEntity.KILL_UNCERTAINTY) {
				kill();
			} else {
				rigidbody.setDirection(new Vector2(hookPiece.getX(), hookPiece.getY()));
			}
		}

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.update();
			temp = temp.getConnected();
		}

		/*
		 * Hooked Entity Management
		 */
		if (hooked != null) {
			hooked.try_setPosition(transform.position.x, transform.position.y);
		}
	}

	public boolean[] isWorldCollision(double tx, double ty) {
		if (Game.map.isCollides(tx, ty, this)) {
			boolean xCol = Game.map.isCollides(tx, transform.position.y, this);
			boolean yCol = Game.map.isCollides(transform.position.x, ty, this);

			return new boolean[] { xCol, yCol };
		}
		return new boolean[] { false, false };
	}

	public void render() {
		int x = (int) (Window.CENTER_X - (Game.focus.x - transform.position.x) * Game.TILE_SIZE);
		int y = (int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) * Game.TILE_SIZE);

		// Game.s.g.drawImage(img, (int) (Window.CENTER_X - (Game.focus.x - transform.position.x) * Game.TILE_SIZE - img.getWidth(null) / 2), //
		// (int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) * Game.TILE_SIZE - img.getHeight(null) / 2), null);

		AffineTransform a = new AffineTransform();
		a.rotate(transform.rotation, x, y);
		a.translate((int) (Window.CENTER_X - (Game.focus.x - transform.position.x) * Game.TILE_SIZE - img.getWidth(null) / 2), //
				(int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) * Game.TILE_SIZE - img.getHeight(null) / 2));
		a.scale(transform.scale.x, transform.scale.y);
		Game.s.g.drawImage(img, a, null);

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.render();
			temp = temp.getConnected();
		}
	}

	private void reverse() {
		rigidbody.speed = HookEntity.HOOK_REVERSE_SPEED;
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

		Game.entities.entities.remove(this);
	}

	public boolean shouldBlock(BBOwner b) {
		if (b instanceof HookEntity) return false;
		if (b instanceof PudgeEntity) {
			if (b == owner) return false;
			else return true;
		}
		if (b instanceof Tile) {
			if (((Tile) b).isHookable()) return true;
			else if (((Tile) b).isHookSolid()) return true;
			else return false;
		}
		return false;
	}

	public void collides(Tile t, double vx, double vy) {
		if (vx == 0) {
			rigidbody.velocity.y *= -1;
		} else if (vy == 0) {
			rigidbody.velocity.x *= -1;
		}
		rigidbody.velocity.scale(SLOW_DOWN_VECTOR);
		rigidbody.speed *= SLOW_DOWN_VECTOR;

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.rigidbody.speed = rigidbody.speed;
			temp = temp.getConnected();
		}
	}

	public void collides(Entity e, double vx, double vy) {
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

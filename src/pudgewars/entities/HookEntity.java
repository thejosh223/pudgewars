package pudgewars.entities;

import java.awt.Image;
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
	public final static double KILL_UNCERTAINTY = 0.1;
	public final static double SLOW_DOWN_VECTOR = 0.5;

	// Hook Speed
	public final static float HOOK_SPEED = 6;
	public final static float HOOK_REVERSE_SPEED = 2 * HOOK_SPEED;

	public PudgeEntity owner;
	public PudgeEntity hooked;

	public boolean canHook = true;
	public boolean specialHook = false;
	private double travelled = 0;
	private double maxTravelDistance;
	private boolean reversing;
	private HookPieceEntity hookPiece;
	private int damage;

	// Render
	private Image img;

	public HookEntity(PudgeEntity e, Vector2 target, boolean specialHook) {
		super(e.transform.position, new Vector2(0.6, 0.6));
		owner = e;
		hooked = null;

		this.specialHook = specialHook;

		damage = 4;

		maxTravelDistance = 14;
		rigidbody.speed = 8;

		rigidbody.physicsSlide = false;
		rigidbody.setDirection(target);

		img = ImageHandler.get().getImage("hook");
	}

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
				// if (Point.distance(transform.position, owner.getX(), owner.getY()) >= PIECE_DISTANCE) {
				if (Vector2.distance(transform.position, owner.transform.position) >= PIECE_DISTANCE) {
					HookPieceEntity e = new HookPieceEntity(owner, rigidbody.velocity.x, rigidbody.velocity.y, rigidbody.speed);
					hookPiece = e;
				}
			} else {
				hookPiece.rigidbody.setDirection(new Vector2(transform.position.x, transform.position.y));
			}
		} else {
			// if (Point.distance(transform.position.x, transform.position.y, owner.getX(), owner.getY()) <= HookEntity.KILL_UNCERTAINTY) {
			if (Vector2.distance(transform.position, owner.transform.position) <= rigidbody.velocity.magnitude() * Time.getTickInterval()) {
				kill();
			} else {
				if (hookPiece == null) rigidbody.setDirection(owner.transform.position.clone());
				else rigidbody.setDirection(hookPiece.transform.position.clone());
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
			// hooked.try_setPosition(transform.position.x, transform.position.y);
			hooked.rigidbody.velocity = rigidbody.velocity.clone();
		}
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
		super.kill();
		owner.removeHook();
		while (hookPiece != null) {
			hookPiece = hookPiece.getConnected();
		}
		hookPiece = null;
	}

	public void attachPudge(PudgeEntity e) {
		e.attachedHook = this; // Set the hookEntity as this
		e.transform.position = transform.position.clone(); // Set the pudge as this position
		// TODO: If killed, check for null (?)
		e.subLife(damage); // Do some damage
		hooked = e;
		canHook = false;
	}

	public void detachPudge() {
		hooked.attachedHook = null;
		hooked = null;
	}

	/*
	 * Collision Detection and Response
	 */
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
		if (hooked == null && canHook) {
			if (e instanceof PudgeEntity) {
				if (e != owner) {
					attachPudge((PudgeEntity) e);
					reverse();
				}
			}
		}
	}
}

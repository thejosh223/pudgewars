package pudgewars.entities.hooks;

import java.awt.Image;
import java.awt.geom.AffineTransform;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HookEntity extends Entity {
	public final static double PULL_SPEED = 6;
	public final static double PIECE_DISTANCE = 1;
	public final static double SLOW_DOWN_VECTOR = 0.5;

	// Hook Speed
	public final static float HOOK_SPEED = 8;
	public final static float HOOK_REVERSE_SPEED = 2 * HOOK_SPEED;

	public PudgeEntity owner;
	public PudgeEntity hooked;
	protected HookPieceEntity hookPiece;

	protected MovementScheme movementScheme;
	public boolean canHook = true;
	protected double travelled = 0;
	protected double maxTravelDistance;
	protected int damage;

	// Render
	private Image img;

	public HookEntity(PudgeEntity e, Vector2 target) {
		super(e.transform.position, new Vector2(0.6, 0.6));
		owner = e;
		hooked = null;

		damage = 4;

		maxTravelDistance = 14;
		rigidbody.speed = HOOK_SPEED;

		rigidbody.physicsSlide = false;
		rigidbody.setDirection(target);

		setMovementType(MovementScheme.FORWARD);

		img = ImageHandler.get().getImage("hook");
	}

	public void update() {
		// Spinning Animation
		transform.rotation -= 0.4;

		rigidbody.updateVelocity();

		double xDist = rigidbody.velocity.x * Time.getTickInterval();
		double yDist = rigidbody.velocity.y * Time.getTickInterval();

		travelled += Math.sqrt(xDist * xDist + yDist * yDist);
		if (travelled >= maxTravelDistance) {
			setMovementType(MovementScheme.REVERSE);
		}

		switch (movementScheme) {
			case FORWARD:
				if (hookPiece == null) {
					// if (Point.distance(transform.position, owner.getX(),
					// owner.getY()) >= PIECE_DISTANCE) {
					if (Vector2.distance(transform.position, owner.transform.position) >= PIECE_DISTANCE) {
						HookPieceEntity e = new HookPieceEntity(owner, this, null);
						hookPiece = e;
						Game.entities.entities.add(e);
					}
				} else {
					hookPiece.rigidbody.setDirection(new Vector2(transform.position.x, transform.position.y));
				}
				break;
			case REVERSE:
				if (Vector2.distance(transform.position, owner.transform.position) <= rigidbody.velocity.magnitude() * Time.getTickInterval()) {
					kill();
				} else {
					if (hookPiece == null) rigidbody.setDirection(owner.transform.position.clone());
					else rigidbody.setDirection(hookPiece.transform.position.clone());
				}
				break;
			case PULL_FORWARD:
				if (hookPiece == null) {
					if (transform.position.distance(owner.transform.position) < HookEntity.PULL_SPEED * Time.getBaseTickInterval()) {
						owner.rigidbody.velocity = Vector2.ZERO.clone();
						owner.canTileCollide = true;
						kill();
					} else {
						owner.rigidbody.setDirection(transform.position, HookEntity.PULL_SPEED);
					}
				}
				break;
		}

		/*
		 * Hooked Entity Management
		 */
		if (hooked != null) {
			// System.out.println("Rig: " + rigidbody.velocity.magnitude());
			hooked.rigidbody.speed = rigidbody.speed;
			hooked.rigidbody.velocity = rigidbody.velocity.clone();
		}
	}

	public void render() {
		int x = (int) (Window.CENTER_X - (Game.focus.x - transform.position.x) * Game.TILE_SIZE);
		int y = (int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) * Game.TILE_SIZE);

		// Game.s.g.drawImage(img, (int) (Window.CENTER_X - (Game.focus.x -
		// transform.position.x) * Game.TILE_SIZE - img.getWidth(null) / 2), //
		// (int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) *
		// Game.TILE_SIZE - img.getHeight(null) / 2), null);

		AffineTransform a = new AffineTransform();
		a.rotate(transform.rotation, x, y);
		a.translate((int) (Window.CENTER_X - (Game.focus.x - transform.position.x) * Game.TILE_SIZE - img.getWidth(null) / 2), //
				(int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) * Game.TILE_SIZE - img.getHeight(null) / 2));
		a.scale(transform.scale.x, transform.scale.y);
		Game.s.g.drawImage(img, a, null);
	}

	public void setMovementType(MovementScheme m) {
		this.movementScheme = m;
		switch (movementScheme) {
			case FORWARD:
				break;
			case REVERSE:
				rigidbody.speed = HookEntity.HOOK_REVERSE_SPEED;
				break;
			case STATIONARY:
				rigidbody.speed = 0;
				break;
			case PULL_FORWARD:
				break;
		}

		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.setMovementType(movementScheme);
			temp = temp.getConnected();
		}
	}

	public void kill() {
		super.kill();
		owner.isHooking = false;
		while (hookPiece != null) {
			hookPiece = hookPiece.getConnected();
		}
		hookPiece = null;
	}

	/*
	 * Basic Hook Collision Detection
	 */

	public boolean shouldBlock(BBOwner b) {
		if (b instanceof Tile) {
			if (((Tile) b).isHookSolid()) return true;
			else return false;
		}
		return false;
	}

	public void collides(Tile t, double vx, double vy) {
		// Slow down behavior
		if (vx == 0) rigidbody.velocity.y *= -1;
		else if (vy == 0) rigidbody.velocity.x *= -1;
		rigidbody.velocity.scale(SLOW_DOWN_VECTOR);
		rigidbody.speed *= SLOW_DOWN_VECTOR;

		// Slow down all the hook pieces
		HookPieceEntity temp = hookPiece;
		while (temp != null) {
			temp.rigidbody.speed = rigidbody.speed;
			temp = temp.getConnected();
		}
	}

	public void collides(Entity e, double vx, double vy) {
	}
}

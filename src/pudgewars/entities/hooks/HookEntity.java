package pudgewars.entities.hooks;

import java.awt.Image;

import pudgewars.Game;
import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.level.Tile;
import pudgewars.util.ImageHandler;
import pudgewars.util.Rotation;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HookEntity extends Entity {
	public final static double PIECE_DISTANCE = 1;
	public final static double SLOW_DOWN_VECTOR = 0.5;

	// Hook Speed
	public final static float HOOK_REVERSE_MULT = 2;

	public PudgeEntity owner;
	public PudgeEntity hooked;
	protected HookPieceEntity hookPiece;

	protected MovementScheme movementScheme;
	public boolean canHook = true;
	protected double travelled = 0;
	protected double maxTravelDistance;
	protected int damage;

	// Render
	protected Image img;
	protected boolean isRotating = true;

	public HookEntity(PudgeEntity e, Vector2 target) {
		super(e.transform.position, new Vector2(e.stats.hookSize, e.stats.hookSize));
		owner = e;
		hooked = null;

		maxTravelDistance = e.stats.hookRange;
		rigidbody.speed = e.stats.hookSpeed;
		this.damage = e.stats.hookDamage;

		rigidbody.physicsSlide = false;
		rigidbody.setDirection(target);

		setMovementType(MovementScheme.FORWARD);

		img = ImageHandler.get().getImage("hook");
	}

	public void update() {
		// Spinning Animation
		if (isRotating) transform.rotation = Rotation.clampRotation(transform.rotation - 0.4);

		// Movement
		rigidbody.updateVelocity();

		double xDist = rigidbody.velocity.x * Time.getTickInterval();
		double yDist = rigidbody.velocity.y * Time.getTickInterval();

		travelled += Math.sqrt(xDist * xDist + yDist * yDist);
		if (travelled >= maxTravelDistance) {
			setMovementType(MovementScheme.REVERSE);
			rigidbody.speed = owner.stats.hookSpeed * HookEntity.HOOK_REVERSE_MULT;
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
				if (Vector2.distance(transform.position, owner.transform.position) <= rigidbody.speed * Time.getTickInterval()) {
					kill();
				} else {
					if (hookPiece == null) rigidbody.setDirection(owner.transform.position.clone());
					else rigidbody.setDirection(hookPiece.transform.position.clone());
				}
				break;
			case PULL_FORWARD:
				if (hookPiece == null) {
					if (transform.position.distance(owner.transform.position) < owner.stats.hookSpeed * Time.getBaseTickInterval()) {
						owner.rigidbody.velocity = Vector2.ZERO.clone();
						owner.canTileCollide = true;
						owner.canMove = true;
						kill();
					} else {
						owner.rigidbody.setDirection(transform.position, owner.stats.hookSpeed);
					}
				}
				break;
		}

		/*
		 * Hooked Entity Management
		 */
		if (hooked != null) {
			hooked.rigidbody.speed = rigidbody.speed;
			hooked.rigidbody.velocity = rigidbody.velocity.clone();
		}
	}

	public void render() {
		// Game.s.g.drawImage(img, (int) (Window.CENTER_X - (Game.focus.x -
		// transform.position.x) * Game.TILE_SIZE - img.getWidth(null) / 2), //
		// (int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) *
		// Game.TILE_SIZE - img.getHeight(null) / 2), null);

		Game.s.g.drawImage(img, transform.getAffineTransformation(), null);
	}

	public void setMovementType(MovementScheme m) {
		this.movementScheme = m;
		switch (movementScheme) {
			case FORWARD:
				break;
			case REVERSE:
				rigidbody.speed = owner.stats.hookSpeed * HookEntity.HOOK_REVERSE_MULT;
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
		owner.stats.restoreDefaults();
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

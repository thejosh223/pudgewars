package pudgewars.entities.hooks;

import java.awt.Point;
import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HookPieceEntity extends Entity {
	private PudgeEntity owner;
	private MovementScheme movementScheme;

	private HookEntity hook;
	private HookPieceEntity next;
	private HookPieceEntity prev;
	private BufferedImage img;

	public HookPieceEntity(PudgeEntity e, HookEntity h, HookPieceEntity p) {
		super(e.transform.position);

		owner = e;
		hook = h;
		prev = p;
		// rigidbody.velocity.x = vx;
		// rigidbody.velocity.y = vy;
		rigidbody.speed = h.rigidbody.speed;
		// rigidbody.setDirection(target)

		// Default Movement Behaviour
		setMovementType(MovementScheme.FORWARD);

		img = ImageHandler.get().getImage("hookpiece");
	}

	public void update() {
		// Move the entity
		rigidbody.updateVelocity();

		switch (movementScheme) {
			case FORWARD:
				if (next == null) {
					if (Point.distance(transform.position.x, transform.position.y, owner.getX(), owner.getY()) >= HookEntity.PIECE_DISTANCE) {
						HookPieceEntity e = new HookPieceEntity(owner, hook, this);
						next = e;
						Game.entities.entities.add(e);
					}
				} else {
					next.rigidbody.setDirection(new Vector2(transform.position.x, transform.position.y));
				}
				break;
			case REVERSE:
				if (next == null) {
					rigidbody.setDirection(owner.transform.position);
					if (transform.position.distance(owner.transform.position) < rigidbody.speed * Time.getBaseTickInterval()) {
						kill();
					}
				} else {
					if (Vector2.distance(transform.position, owner.transform.position) <= rigidbody.speed * Time.getTickInterval()) {
						next.kill();
						next = null;
					} else {
						rigidbody.setDirection(next.transform.position);
					}
				}
				break;
			case STATIONARY:
				break;
			case PULL_FORWARD:
				if (next == null) {
					if (prev == null) {
						if (transform.position.distance(owner.transform.position) < HookEntity.PULL_SPEED * Time.getBaseTickInterval()) {
							kill();
						} else {
							owner.rigidbody.setDirection(transform.position, HookEntity.PULL_SPEED);
						}
					} else {
						if (transform.position.distance(owner.transform.position) < HookEntity.PULL_SPEED * Time.getBaseTickInterval()) {
							kill();
						} else {
							owner.rigidbody.setDirection(transform.position, HookEntity.PULL_SPEED);
						}
					}
				} else {
				}
				break;
		}
	}

	public void kill() {
		super.kill();
		if (prev != null) {
			// Set previous hookPiece.next = null
			prev.next = null;
		} else {
			// If prev == null, then prev == hook.
			// Set hook.hookPiece = null
			hook.hookPiece = null;
		}
		if (next != null) next.prev = null;
	}

	public void render() {
		Game.s.g.drawImage(img, (int) (Window.CENTER_X - (Game.focus.x - transform.position.x) * Game.TILE_SIZE - img.getWidth(null) / 2), //
				(int) (Window.CENTER_Y - (Game.focus.y - transform.position.y) * Game.TILE_SIZE - img.getHeight(null) / 2), null);
	}

	public PudgeEntity getOwner() {
		return owner;
	}

	public HookPieceEntity getConnected() {
		return next;
	}

	public void setMovementType(MovementScheme m) {
		this.movementScheme = m;
		rigidbody.speed = hook.rigidbody.speed;
		switch (movementScheme) {
			case FORWARD:
				break;
			case REVERSE:
				break;
			case STATIONARY:
				break;
			case PULL_FORWARD:
				break;
		}
	}
}

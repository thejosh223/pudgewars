package pudgewars.entities;

import java.awt.Point;
import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.entities.hooks.HookMovement;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HookPieceEntity extends Entity {
	private PudgeEntity owner;
	private int movementType;
	// private boolean reversing;

	private HookPieceEntity next;
	private BufferedImage img;

	public HookPieceEntity(PudgeEntity e, double vx, double vy, double speed) {
		super(e.transform.position);

		owner = e;
		rigidbody.velocity.x = vx;
		rigidbody.velocity.y = vy;
		rigidbody.speed = speed;

		// Default Movement Behaviour
		movementType = HookMovement.FORWARD;

		img = ImageHandler.get().getImage("hookpiece");
	}

	public void update() {
		double xDist = rigidbody.velocity.x * Time.getTickInterval();
		double yDist = rigidbody.velocity.y * Time.getTickInterval();
		double tx = transform.position.x + xDist;
		double ty = transform.position.y + yDist;
		transform.position.x = tx;
		transform.position.y = ty;

		switch (movementType) {
			case HookMovement.FORWARD:
				if (next == null) {
					if (Point.distance(transform.position.x, transform.position.y, owner.getX(), owner.getY()) >= HookEntity.PIECE_DISTANCE) {
						HookPieceEntity e = new HookPieceEntity(owner, rigidbody.velocity.x, rigidbody.velocity.y, rigidbody.speed);
						next = e;
					}
				} else {
					next.rigidbody.setDirection(new Vector2(transform.position.x, transform.position.y));
				}
				break;
			case HookMovement.REVERSE:
				if (next == null) {
					rigidbody.setDirection(owner.transform.position);
				} else {
					if (Vector2.distance(transform.position, owner.transform.position) <= rigidbody.velocity.magnitude() * Time.getTickInterval()) {
						next.kill();
						next = null;
					} else {
						rigidbody.setDirection(next.transform.position);
					}
				}
				break;
			case HookMovement.STATIONARY:
				break;
		}
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

	public void reverse() {
		rigidbody.speed = HookEntity.HOOK_REVERSE_SPEED;
		movementType = HookMovement.REVERSE;
	}
}

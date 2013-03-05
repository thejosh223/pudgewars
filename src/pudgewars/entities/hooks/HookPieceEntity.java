package pudgewars.entities.hooks;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import pudgewars.Game;
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
	private BufferedImage[] img;

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

		img = new BufferedImage[4];
		for (int i = 0; i < img.length; i++)
			img[i] = ImageHandler.get().getImage("newrope", i, 0, 4, 4);
	}

	public void update() {
		// Move the entity
		rigidbody.updateVelocity();

		switch (movementScheme) {
			case FORWARD:
				if (next == null) {
					if (transform.position.distance(owner.transform.position) >= HookEntity.PIECE_DISTANCE) {
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
						if (transform.position.distance(owner.transform.position) < owner.stats.hookSpeed.getValue() * Time.getBaseTickInterval()) {
							kill();
						} else {
							owner.rigidbody.setDirection(transform.position, owner.stats.hookSpeed.getValue());
						}
					} else {
						if (transform.position.distance(owner.transform.position) < owner.stats.hookSpeed.getValue() * Time.getBaseTickInterval()) {
							kill();
						} else {
							owner.rigidbody.setDirection(transform.position, owner.stats.hookSpeed.getValue());
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
		// Draw the links between THIS and NEXT
		if (next != null) renderBetweenLinks(next.transform.position);

		// Draw the links between THIS and HOOK
		if (prev == null) renderBetweenLinks(hook.getKnotPosition());

		// Draw this link
		double angle = 0;
		if (prev == null) angle = Math.PI - Math.atan2(transform.position.x - hook.transform.position.x, transform.position.y - hook.transform.position.y);
		else if (next != null) angle = Math.PI - Math.atan2(transform.position.x - next.transform.position.x, transform.position.y - next.transform.position.y);
		else angle = Math.PI - Math.atan2(transform.position.x - owner.transform.position.x, transform.position.y - owner.transform.position.y);

		Vector2 v = Game.s.worldToScreenPoint(transform.position);
		AffineTransform a = new AffineTransform();
		a.translate((int) (v.x - img[0].getWidth() / 2), (int) (v.y - img[0].getHeight() / 2));
		a.rotate(angle, img[0].getWidth() / 2, img[0].getHeight() / 2);
		Game.s.g.drawImage(img[0], a, null);
	}

	private void renderBetweenLinks(Vector2 target) {
		double levelDist = transform.position.distance(target);
		double screenDist = levelDist * Game.TILE_SIZE;
		int count = (int) (screenDist / img[0].getWidth()) + 1;

		Vector2 levelSlope = Vector2.subtract(transform.position, target);
		levelSlope.normalize();
		levelSlope.scale(levelDist / count);
		Vector2 pos = transform.position.clone();
		pos.add(levelSlope);

		double angle = Math.PI - Math.atan2(transform.position.x - target.x, transform.position.y - target.y);

		for (int i = 0; i < count; i++) {
			Vector2 v = Game.s.worldToScreenPoint(pos);
			AffineTransform a = new AffineTransform();
			a.translate((int) (v.x - img[0].getWidth() / 2), (int) (v.y - img[0].getHeight() / 2));
			a.rotate(angle, img[0].getWidth() / 2, img[0].getHeight() / 2);

			Game.s.g.drawImage(img[i % img.length], a, null);
			pos.add(levelSlope);
		}
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

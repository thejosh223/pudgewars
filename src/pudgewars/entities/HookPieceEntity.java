package pudgewars.entities;

import java.awt.Point;
import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.util.ImageHandler;
import pudgewars.util.Time;
import pudgewars.util.Vector2;

public class HookPieceEntity extends Entity {
	private PudgeEntity owner;
	private boolean reversing;

	private HookPieceEntity next;
	private BufferedImage img;

	public HookPieceEntity(PudgeEntity e, double vx, double vy, float speed) {
		super(e.getX(), e.getY());

		owner = e;
		this.vx = vx;
		this.vy = vy;
		this.speed = speed;

		img = ImageHandler.get().getImage("hookpiece");
	}

	public void update() {
		double xDist = vx * Time.getTickInterval();
		double yDist = vy * Time.getTickInterval();
		double tx = transform.position.x + xDist;
		double ty = transform.position.y + yDist;
		transform.position.x = tx;
		transform.position.y = ty;

		if (!reversing) {
			if (next == null) {
				if (Point.distance(transform.position.x, transform.position.y, owner.getX(), owner.getY()) >= HookEntity.PIECE_DISTANCE) {
					HookPieceEntity e = new HookPieceEntity(owner, vx, vy, speed);
					next = e;
				}
			} else {
				next.setDirection(new Vector2(transform.position.x, transform.position.y));
			}
		} else {
			if (next != null) {
				if (next.getConnected() != null) {
					if (Point.distance(next.getConnected().getX(), next.getConnected().getY(), owner.getX(), owner.getY()) <= HookEntity.KILL_UNCERTAINTY) {
						next.kill();
						next = null;
					} else {
						setDirection(new Vector2(next.getX(), next.getY()));
					}
				} else {
					setDirection(new Vector2(next.getX(), next.getY()));
				}
			} else {
				setDirection(new Vector2(owner.getX(), owner.getY()));
			}
		}
	}

	public void kill() {
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
		speed = HookEntity.HOOK_REVERSE_SPEED;
		reversing = true;
	}

	public void collides(Entity e) {
		// Do Nothing.
	}
}

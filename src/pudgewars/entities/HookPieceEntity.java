package pudgewars.entities;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.Window;
import pudgewars.util.ImageHandler;

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
	
	public void update(long timePassed) {
		double xDist = vx * timePassed / 1000;
		double yDist = vy * timePassed / 1000;		
		double tx = x + xDist;
		double ty = y + yDist;
		x = tx;
		y = ty;
		
		if (!reversing) {
			if (next == null) {
				if (Point.distance(x, y, owner.getX(), owner.getY()) >= HookEntity.PIECE_DISTANCE) {
					HookPieceEntity e = new HookPieceEntity(owner, vx, vy, speed);
					next = e;
				}
			} else {
//				if (next.getConnected() != null) {
//					next.getConnected().setDirection(new Point2D.Double(x, y));
//				} else {
//					next.setDirection(new Point2D.Double(x, y));
//				}
				next.setDirection(new Point2D.Double(x, y));
			}
		} else {
			if (next != null) {
				if (next.getConnected() != null) {
					if (Point.distance(next.getConnected().getX(), next.getConnected().getY(), owner.getX(), owner.getY()) <= HookEntity.KILL_UNCERTAINTY) {
						next.kill();
						next = null;
					} else {
//						setDirection(new Point2D.Double(next.getConnected().getX(), next.getConnected().getY()));
						setDirection(new Point2D.Double(next.getX(), next.getY()));
					}
				} else {
					setDirection(new Point2D.Double(next.getX(), next.getY()));
				}
			} else {
				setDirection(new Point2D.Double(owner.getX(), owner.getY()));
			}
		}
	}

	public void kill() {
		
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(img, (int) (Window.CENTER_X - (Game.focus.getX() - x) * Game.TILE_SIZE - img.getWidth(null) / 2), (int) (Window.CENTER_Y - (Game.focus.getY() - y) * Game.TILE_SIZE - img.getHeight(null) / 2), null);
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
		//Do Nothing.		
	}
}

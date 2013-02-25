package pudgewars.level;

import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.entities.hooks.HookEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.util.ImageHandler;

public class Tile implements BBOwner {
	public final static Tile T_Dirt1 = new Tile("grass1", false, false, false, 0, 0);
	public final static Tile T_Dirt2 = new Tile("grass3", false, false, false, 0, 0);
	public final static Tile T_Dirt3 = new Tile("grass2", false, false, false, 0, 0);
	public final static Tile T_Block = new Tile("tree", true, true, false, 0, 0);
	public final static Tile T_Mound = new Tile("mound2", true, false, false, 0, 0);
	public final static Tile T_Hookable = new Tile("hookable", false, false, true, 13, 15);

	public final static Tile T_Fountain0 = new Tile("fountain", 0, 0, false, false, false, 0, 0);
	public final static Tile T_Fountain1 = new Tile("fountain", 1, 0, false, false, false, 0, 0);
	public final static Tile T_Fountain2 = new Tile("fountain", 0, 1, false, false, false, 0, 0);
	public final static Tile T_Fountain3 = new Tile("fountain", 1, 1, false, false, false, 0, 0);

	protected String ID;
	protected BufferedImage img;
	protected BufferedImage post;
	protected boolean pudgeSolid;
	protected boolean hookSolid;
	protected boolean hookable;
	protected double lightWidth;
	protected double lightHeight;

	public Tile(String ID, int x, int y, boolean pudgeSolid, boolean hookSolid, boolean hookable, double lightWidth, double lightHeight) {
		this("fountain", pudgeSolid, hookSolid, hookable, lightWidth, lightHeight);
		img = ImageHandler.get().getImage("fountain", x, y, 16, 16);
	}

	public Tile(String ID, String post, boolean pudgeSolid, boolean hookSolid, boolean hookable, double lightWidth, double lightHeight) {
		this(ID, pudgeSolid, hookSolid, hookable, lightWidth, lightHeight);
		this.post = ImageHandler.get().getImage(post);
	}

	public Tile(String ID, boolean pudgeSolid, boolean hookSolid, boolean hookable, double lightWidth, double lightHeight) {
		this.ID = ID;
		this.pudgeSolid = pudgeSolid;
		this.hookSolid = hookSolid;
		this.hookable = hookable;
		this.lightWidth = lightWidth;
		this.lightHeight = lightHeight;
		img = ImageHandler.get().getImage(ID);
	}

	public void render(int x, int y) {
		Game.s.g.drawImage(img, x, y, null);
	}

	public void postRender(int x, int y) {
		if (post != null) Game.s.g.drawImage(post, x, y, null);
	}

	public boolean isPudgeSolid() {
		return pudgeSolid;
	}

	public boolean isHookSolid() {
		return hookSolid;
	}

	public boolean isHookable() {
		return hookable;
	}

	/*
	 * Collision Detection!
	 */
	public final boolean blocks(BBOwner b) {
		return b.shouldBlock(this) && this.shouldBlock(b);
	}

	public boolean shouldBlock(BBOwner b) {
		// Hook Collisions
		if (b instanceof HookEntity) {
			if (isHookable()) return true;
			else if (isHookSolid()) return true;
			else return false;
		}
		// Pudge Collisions
		if (b instanceof PudgeEntity) {
			if (isPudgeSolid()) return true;
			return false;
		}
		return false;
	}

	public void handleCollision(Entity e, double vx, double vy) {
		e.collides(this, vx, vy);
	}

	public void collides(Entity e, double vx, double vy) {
	}

	public void collides(Tile t, double vx, double vy) {
	}

}

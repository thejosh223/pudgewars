package pudgewars.level;

import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.entities.Entity;
import pudgewars.entities.PudgeEntity;
import pudgewars.entities.hooks.HookEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.util.ImageHandler;

public class Tile implements BBOwner {
	public final static Tile[] GRASS = //
	{ new Tile("grass-2", 0, 0, false, false, false), //
			new Tile("grass-2", 1, 0, false, false, false), //
			new Tile("grass-2", 2, 0, false, false, false), //
			new Tile("grass-2", 3, 0, false, false, false), //
			new Tile("grass-2", 4, 0, false, false, false) };

	public final static Tile[] FOUNTAIN = //
	{ new Tile("fountain", 0, 0, false, false, false), //
			new Tile("fountain", 1, 0, false, false, false), //
			new Tile("fountain", 0, 1, false, false, false), //
			new Tile("fountain", 1, 1, false, false, false) };

	public final static Tile T_Block = new Tile("tree_wall", 0, 0, true, true, false);
	public final static Tile T_Mound = new Tile("mound2", 0, 0, true, false, false);
	public final static Tile T_Hookable = new LightTile("hookable", 0, 0, false, false, true, 13, 13);

	protected String ID;
	protected BufferedImage img;
	protected BufferedImage post;
	protected boolean pudgeSolid;
	protected boolean hookSolid;
	protected boolean hookable;

	public Tile(String ID, int x1, int y1, String post, int x2, int y2, boolean pudgeSolid, boolean hookSolid, boolean hookable) {
		this(ID, x1, y1, pudgeSolid, hookSolid, hookable);
		this.post = ImageHandler.get().getImage(post, x2, y2, 16, 16);
	}

	public Tile(String ID, int x, int y, boolean pudgeSolid, boolean hookSolid, boolean hookable) {
		this.ID = ID;
		this.pudgeSolid = pudgeSolid;
		this.hookSolid = hookSolid;
		this.hookable = hookable;
		img = ImageHandler.get().getImage(ID, x, y, 16, 16);
	}

	public void render(int x, int y) {
		Game.s.g.drawImage(img, x, y, null);
	}

	public void postRender(int x, int y) {
		if (post != null) Game.s.g.drawImage(post, x, y, null);
	}

	/*
	 * Collision Detection!
	 */
	public boolean isPudgeSolid() {
		return pudgeSolid;
	}

	public boolean isHookSolid() {
		return hookSolid;
	}

	public boolean isHookable() {
		return hookable;
	}

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

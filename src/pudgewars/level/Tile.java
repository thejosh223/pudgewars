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
	{ new Tile("grass-2", 0, 1, false, false, false), //
			new Tile("grass-2", 1, 1, false, false, false), //
			new Tile("grass-2", 2, 1, false, false, false), //
			new Tile("grass-2", 3, 1, false, false, false), //
			new Tile("grass-2", 4, 1, false, false, false) };

	public final static Tile[] FOUNTAIN = //
	{ new Tile("fountain", 0, 1, false, false, false), //
			new Tile("fountain", 1, 1, false, false, false), //
			new Tile("fountain", 2, 1, false, false, false), //
			new Tile("fountain", 3, 1, false, false, false) };

	public final static Tile[] TREE = //
	{ new Tile("tree", 0, 1, true, true, false), //
			new Tile("tree", 1, 1, true, true, false), //
			new Tile("tree", 2, 1, true, true, false), //
			new Tile("tree", 3, 1, true, true, false) };

	public final static Tile WATER = new Tile("water", 0, 5, false, false, false);

	public final static Tile T_Mound = new Tile("mound2", 0, 1, true, false, false);
	public final static Tile T_Hookable = new LightTile("hookable", 0, 1, false, false, true, 13, 13);

	protected String ID;
	protected BufferedImage[] img;
	protected boolean pudgeSolid;
	protected boolean hookSolid;
	protected boolean hookable;

	public Tile(String ID, int x, int numAnim, boolean pudgeSolid, boolean hookSolid, boolean hookable) {
		this.ID = ID;
		this.pudgeSolid = pudgeSolid;
		this.hookSolid = hookSolid;
		this.hookable = hookable;

		img = new BufferedImage[numAnim];
		for (int i = 0; i < numAnim; i++)
			img[i] = ImageHandler.get().getImage(ID, x, i, 16, 16);
	}

	public void render(int x, int y, int tileData) {
		Game.s.g.drawImage(img[tileData % img.length], x, y, null);
	}

	public void postRender(int x, int y) {
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

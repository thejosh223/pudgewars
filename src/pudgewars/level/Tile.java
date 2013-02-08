package pudgewars.level;

import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.components.Rigidbody;
import pudgewars.entities.Entity;
import pudgewars.entities.HookEntity;
import pudgewars.entities.HookPieceEntity;
import pudgewars.entities.PudgeEntity;
import pudgewars.interfaces.BBOwner;
import pudgewars.util.ImageHandler;

public class Tile implements BBOwner {
	public final static Tile T_Dirt1 = new Tile("grass1", false, false, false);
	public final static Tile T_Dirt2 = new Tile("grass2", false, false, false);
	public final static Tile T_Dirt3 = new Tile("grass3", false, false, false);
	public final static Tile T_Block = new Tile("outerwall", true, true, false);
	public final static Tile T_Mound = new Tile("mound2", true, false, false);
	// public final static Tile T_Hookable = new Tile ("hookable", false, false, true);

	String ID;
	BufferedImage img;
	boolean pudgeSolid;
	boolean hookSolid;
	boolean hookable;

	public Tile(String ID, boolean pudgeSolid, boolean hookSolid, boolean hookable) {
		this.ID = ID;
		this.pudgeSolid = pudgeSolid;
		this.hookSolid = hookSolid;
		this.hookable = hookable;

		System.out.println("ID: " + ID);
		img = ImageHandler.get().getImage(ID);
	}

	public void render(int x, int y) {
		Game.s.g.drawImage(img, x, y, null);
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

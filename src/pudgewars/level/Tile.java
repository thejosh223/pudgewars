package pudgewars.level;

import java.awt.image.BufferedImage;

import pudgewars.Game;
import pudgewars.util.ImageHandler;

public class Tile {
	public final static Tile T_Dirt1 = new Tile("t_dirt", false, false, false);
	public final static Tile T_Dirt2 = new Tile("t_dirt2", false, false, false);
	public final static Tile T_Dirt3 = new Tile("t_dirt3", false, false, false);
	public final static Tile T_Cobble = new Tile("cobblestoneSD", false, false, false);
	public final static Tile T_Moss = new Tile("mossystoneSD", false, false, false);
	public final static Tile T_Block = new Tile("mound", true, false, false);
	// public final static Tile T_Hookable = new Tile ("hookable", false, false, true);

	String ID;
	BufferedImage img;
	boolean solid;
	boolean hookSolid;
	boolean hookable;

	public Tile(String ID, boolean solid, boolean hookSolid, boolean hookable) {
		this.ID = ID;
		this.solid = solid;
		this.hookSolid = hookSolid;
		this.hookable = hookable;

		System.out.println("ID: " + ID);
		img = ImageHandler.get().getImage(ID);
	}

	public void render(int x, int y) {
		Game.s.g.drawImage(img, x, y, null);
	}

	public boolean isSolid() {
		return solid;
	}

	public boolean isHookSolid() {
		return hookSolid;
	}

	public boolean isHookable() {
		return hookable;
	}
}

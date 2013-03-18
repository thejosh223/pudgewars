package pudgewars.level;

public class LightTile extends Tile {

	protected double lightWidth;
	protected double lightHeight;

	public LightTile(String ID, int x, int numAnim, double animationSpeed, boolean pudgeSolid, boolean hookSolid, boolean hookable, double lightWidth, double lightHeight) {
		super(ID, x, numAnim, animationSpeed, pudgeSolid, hookSolid, hookable);
		this.lightWidth = lightWidth;
		this.lightHeight = lightHeight;
	}

}

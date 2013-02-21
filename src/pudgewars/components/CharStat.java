package pudgewars.components;

public enum CharStat {
	moveSpeed(0), //
	life(1), //
	hookDamage(2), //
	hookSpeed(3), //
	hookRange(4), //
	hookSize(5);

	public final static int length = CharStat.values().length;

	public final int val;

	CharStat(int val) {
		this.val = val;
	}
}

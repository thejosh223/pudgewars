package pudgewars.util;

public class Rotation {
	public final static double ZERO = 0;

	public static double clampRotation(double theta) {
		while (theta > 2 * Math.PI)
			theta -= 2 * Math.PI;
		while (theta < 0)
			theta += 2 * Math.PI;
		return theta;
	}
}

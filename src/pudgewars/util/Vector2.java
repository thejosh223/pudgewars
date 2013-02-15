package pudgewars.util;

public class Vector2 {
	public final static Vector2 ZERO = new Vector2(0, 0);
	public final static Vector2 ONE = new Vector2(1, 1);

	public double x;
	public double y;

	public Vector2() {
		this(0, 0);
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void add(Vector2 v) {
		x += v.x;
		y += v.y;
	}

	public void scale(double amt) {
		x *= amt;
		y *= amt;
	}

	public void normalize() {
		double magnitude = this.magnitude();
		if (magnitude == 0) {
			x = 0;
			y = 0;
			return;
		}
		x /= magnitude;
		y /= magnitude;
	}

	public double dot(Vector2 v) {
		return v.x * x + v.y * y;
	}

	public void setMagnitude(double m) {
		normalize();
		scale(m);
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector2 clone() {
		return new Vector2(x, y);
	}

	public double distance(Vector2 v) {
		return Vector2.distance(this, v);
	}

	public void println() {
		System.out.println("V: " + x + ", " + y);
	}

	public static double distance(Vector2 v1, Vector2 v2) {
		return Math.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y));
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Vector2.distance(new Vector2(x1, y1), new Vector2(x2, y2));
	}
}

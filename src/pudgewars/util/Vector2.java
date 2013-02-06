package pudgewars.util;

public class Vector2 {
	public final static Vector2 ZERO = new Vector2(0, 0);

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

	public void scale(float amt) {
		x *= amt;
		y *= amt;
	}

	public void normalize() {
		double magnitude = (double) Math.sqrt(x * x + y * y);
		x /= magnitude;
		y /= magnitude;
	}

	public double dot(Vector2 v) {
		return v.x * x + v.y * y;
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
}

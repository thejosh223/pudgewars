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

	public Vector2(String s) {
		setNetString(s);
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

	public String getNetString() {
		return x + " " + y;
	}

	public void setNetString(String s) {
		String[] t = s.split(" ");
		x = Double.parseDouble(t[0]);
		y = Double.parseDouble(t[1]);
	}

	public static Vector2 componentMultiply(Vector2 a, Vector2 b) {
		return new Vector2(a.x * b.x, a.y * b.y);
	}

	public static double distance(Vector2 v1, Vector2 v2) {
		return Math.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y));
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Vector2.distance(new Vector2(x1, y1), new Vector2(x2, y2));
	}

	public static Vector2 add(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y);
	}

	public static Vector2 subtract(Vector2 a, Vector2 b) {
		return new Vector2(b.x - a.x, b.y - a.y);
	}

	public static Vector2 scale(Vector2 v, double s) {
		return new Vector2(v.x * s, v.y * s);
	}

}

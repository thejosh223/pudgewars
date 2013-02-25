package pudgewars.render;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashMap;

import pudgewars.util.ImageHandler;

public class CursorManager {
	private HashMap<String, Cursor> cursors;
	private Canvas c;

	public String active;

	public CursorManager(Canvas c) {
		this.c = c;
		cursors = new HashMap<String, Cursor>();

		loadCursor("cursor", "Default");
		loadCursor("cursor3", "Special");
		setCursor("Default");
	}

	private void loadCursor(String filename, String hashname) {
		Image i = ImageHandler.get().getImage(filename);
		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(i, new Point(15, 15), hashname);
		cursors.put(hashname, c);
	}

	public void setCursor(String name) {
		if (name.equals(active)) return;

		Cursor tc = cursors.get(name);
		if (tc != null) {
			c.setCursor(tc);
			active = name;
		}
	}
}

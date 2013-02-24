package pudgewars.input;

import java.util.ArrayList;
import java.util.List;

import pudgewars.util.Vector2;

public class MouseButtons {
	// public final static int LEFT = 0;
	// public final static int RIGHT = 1;
	// public final static int MIDDLE = 2;

	public final class MouseButton {
		public Vector2 nextState;
		public Vector2 prevState;
		public Vector2 currentState;

		// public boolean nextState;
		// public boolean wasDown;
		// public boolean isDown;

		public MouseButton() {
			mouseButtons.add(this);
		}

		public void tick() {
			prevState = currentState;
			currentState = nextState;
		}

		public Vector2 wasPressed() {
			if (currentState != null && prevState == null) return currentState.clone();
			return null;
		}

		public Vector2 wasReleased() {
			if (currentState == null && prevState != null) return prevState.clone();
			return null;
		}
	}

	private List<MouseButton> mouseButtons = new ArrayList<MouseButton>();

	public MouseButton left = new MouseButton();
	public MouseButton right = new MouseButton();
	public MouseButton middle = new MouseButton();

	public Vector2 mousePosition = new Vector2(0, 0);

	public void tick() {
		for (MouseButton m : mouseButtons)
			m.tick();
	}
}

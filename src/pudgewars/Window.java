package pudgewars;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pudgewars.input.InputHandler;

public class Window extends Canvas {
	private static final long serialVersionUID = 1L;

	public static int WIDTH = Game.TILE_SIZE * Game.TILE_WIDTH;
	public static int HEIGHT = Game.TILE_SIZE * Game.TILE_HEIGHT;

	public static int CENTER_X = WIDTH / 2;
	public static int CENTER_Y = HEIGHT / 2;

	// Drawing to the Window
	public static JFrame container;
	public static BufferStrategy strategy;

	public static InputHandler inputter = new InputHandler();

	public static Game g;

	public Window() {
		// The Window
		container = new JFrame("Villageville");
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = (JPanel) container.getContentPane();
		Dimension d = new Dimension(WIDTH, HEIGHT);
		panel.setPreferredSize(d);
		panel.setMaximumSize(d);
		panel.setMinimumSize(d);
		panel.setSize(d);
		panel.setLayout(null);

		// Sets the bounds of our Canvas
		setBounds(0, 0, WIDTH, HEIGHT);
		panel.add(this);

		// Makes the Window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		createBufferStrategy(2);
		strategy = getBufferStrategy();

		this.addKeyListener(inputter);
		this.addMouseListener(inputter);
		this.addMouseMotionListener(inputter);

		requestFocus();

		g = new Game();
	}

	// ----Main Method----
	public static void main(String[] args) {
		new Window();
	}
}

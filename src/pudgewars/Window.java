package pudgewars;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pudgewars.input.KeyHandler;
import pudgewars.input.Keys;
import pudgewars.input.MouseButtons;
import pudgewars.input.MouseHandler;

import pudgewars.network.MyConnection;

public class Window extends Canvas {
	private static final long serialVersionUID = 1L;

	public static int WIDTH = Game.TILE_SIZE * Game.TILE_WIDTH;
	public static int HEIGHT = Game.TILE_SIZE * Game.TILE_HEIGHT;

	public static int SCALE = 2;
	public static int ACTUAL_WIDTH = WIDTH * SCALE;
	public static int ACTUAL_HEIGHT = HEIGHT * SCALE;

	public static int LIGHTMAP_MULT = 1;

	public static int CENTER_X = WIDTH / 2;
	public static int CENTER_Y = HEIGHT / 2;

	// Drawing to the Window
	public static JFrame container;
	public static BufferStrategy strategy;

	public static Game g;

	public Window() {
		// The Window
		container = new JFrame("Pudge Wars");
		container.setBounds(50, 50, ACTUAL_WIDTH, ACTUAL_HEIGHT);
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = (JPanel) container.getContentPane();
		Dimension d = new Dimension(ACTUAL_WIDTH, ACTUAL_HEIGHT);
		panel.setPreferredSize(d);
		panel.setMaximumSize(d);
		panel.setMinimumSize(d);
		panel.setSize(d);
		panel.setLayout(null);

		// Sets the bounds of our Canvas
		setBounds(0, 0, ACTUAL_WIDTH, ACTUAL_HEIGHT);
		panel.add(this);

		// Makes the Window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		createBufferStrategy(2);
		strategy = getBufferStrategy();

		Keys k = new Keys();
		MouseButtons m = new MouseButtons();
		KeyHandler keyHandler = new KeyHandler(k);
		MouseHandler mouseHandler = new MouseHandler(m);

		this.addKeyListener(keyHandler);
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

		requestFocus();

		g = new Game(this, k, m);
		Game.w = this;
	}

	// ----Main Method----
	public void startClientGame(MyConnection conn) {
		//new Window();
		g.init(conn);
		g.gameLoop();
	}
}

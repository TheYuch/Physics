package physics;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

	static GameState state = new GameState();
	static final int WIN_WIDTH = 500;
	static final int WIN_HEIGHT = 500;

	public static void main(String args[]) throws InterruptedException {
		JFrame frame = new JFrame("Platformer");
		frame.addKeyListener(state);

		GamePanel game = new GamePanel();
		frame.add(game);

		frame.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		frame.setVisible(true);

		while (true) {
			long startTime = System.currentTimeMillis();
			
			Main.state.update();
			frame.repaint();

			long elapsedTime = System.currentTimeMillis() - startTime;

			Thread.sleep(Math.max(0, 1000 / 30 - elapsedTime));
		}
	}
}

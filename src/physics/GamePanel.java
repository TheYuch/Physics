package physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	/*
	 * Moved the update method into GameState.java
	 */

	@Override
	public void paintComponent(Graphics g) {
		/*
		 * Clear the screen
		 */
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		GameState currState = Main.state;
		
		/*
		 * draw obstacles
		 */
		g.setColor(Color.BLACK);
		for (Rectangle r : currState.obstacles)
		{
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		
		/*
		 * Draw the player
		 */
		g.setColor(Color.BLUE);
		Point2D.Double player = currState.getPlayer();
		g.fillOval((int)player.x, (int)player.y, GameState.PLAYER_SIZE, GameState.PLAYER_SIZE);
	
		/*
		 * Draw the end
		 */
		g.setColor(Color.YELLOW);
		g.fillRect(currState.end.x, currState.end.y, currState.end.width, currState.end.height);
	}
}

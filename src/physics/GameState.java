package physics;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

public class GameState extends KeyAdapter {

	static final int PLAYER_SIZE = 20;
	static final int JUMP = 15;
	static final int BLOCKS_ALLOWED = 3;

	private Point2D.Double player;
	private Point2D.Double prevplayer;
	private Point2D.Double velocity;
	private double ELASTICITY = 0.5;
	private double FRICTION = 0.9;
	public boolean grounded = false;
	private int blocks_placed = 0;
	private int remove_idx;
	
	private boolean collidedx = false;
	private boolean collidedy = false;
	
	public ArrayList<Rectangle> obstacles;
	
	public Rectangle end;
	
	HashSet<Integer> keyspressed; //stores keycodes

	public GameState() {
		player = new Point2D.Double(0, 50);
		prevplayer = new Point2D.Double(0, 50);
		velocity = new Point2D.Double(0, 0);
		keyspressed = new HashSet<Integer>();
		obstacles = new ArrayList<Rectangle>();
		obstacles.add(new Rectangle(100, 300, 100, 50));
		obstacles.add(new Rectangle(275, 200, 50, 200));
		obstacles.add(new Rectangle(350, 250, 50, 50));
		//add floor as obstacle
		obstacles.add(new Rectangle(-1, Main.WIN_HEIGHT - 100, Main.WIN_WIDTH + 2, 100));
		//set remove_idx to length of current arraylist
		remove_idx = obstacles.size();		
		//add end rectangle
		end = new Rectangle(450, 350, 25, 25);
	}

	private void addremove(ArrayList<Rectangle> obstacles, int x, int y, int size)
	{
		blocks_placed++;
		if (blocks_placed > BLOCKS_ALLOWED)
		{
			blocks_placed = BLOCKS_ALLOWED;
			obstacles.remove(remove_idx);
		}
		obstacles.add(new Rectangle(x, y, size, size));
	}
	
	private void detectcollision(Rectangle r)
	{
		//if collision, check which edge and perform designated actions
		//because player is also a box, with x, y being the ball's top 
		//left corner, left collisions and up collisions have to take in account
		//the ball's size.
		if (player.x <= r.x + r.width && player.x + PLAYER_SIZE >= r.x && player.y <= r.y + r.height && player.y + PLAYER_SIZE >= r.y) {
			if (prevplayer.y <= r.y - PLAYER_SIZE) //bottom collision
			{
				player.y = r.y - PLAYER_SIZE;
				if (!collidedy)
					velocity.y = (int)(-ELASTICITY * velocity.y); //bounce off ground
				grounded = true;
				collidedy = true;
			}
			else if (prevplayer.y >= r.y + r.height) //up collision
			{
				player.y = r.y + r.height;
				if (velocity.y < 0 && !collidedy)
					velocity.y = (int)(-ELASTICITY * velocity.y);
				collidedy = true;
			}
			else if (prevplayer.x <= r.x - PLAYER_SIZE) //left collision
			{
				player.x = r.x - PLAYER_SIZE;
				if (!collidedx)
					velocity.x = (int)(-ELASTICITY * velocity.x); //bounce off sides, switching x velocity
				collidedx = true;
			}
			else if (prevplayer.x >= r.x + r.width) //right collision
			{
				player.x = r.x + r.width;
				if (!collidedx)
					velocity.x = (int)(-ELASTICITY * velocity.x); //bounce off sides, switching x velocity
				collidedx = true;
			}
		}
	}
	
	public void update() {
		for (Integer c : keyspressed) //check keys pressed
		{
			if (c == KeyEvent.VK_W)
			{
				if (grounded) 
				{
					velocity.y = -JUMP; //do -= JUMP if you want bouncing to be add to the velocity
				}
			}
			else if (c == KeyEvent.VK_A)
			{
				velocity.x -= 1;
			}
			else if (c == KeyEvent.VK_D)
			{
				velocity.x += 1;
			}
		}
		
		//check if you've reached the end
		if (player.x <= end.x + end.width && player.x + PLAYER_SIZE >= end.x && player.y <= end.y + end.height && player.y + PLAYER_SIZE >= end.y)
		{
			System.exit(0);
		}
		velocity.y += 1;
		player.y += velocity.y;
		player.x += velocity.x;
		
		grounded = false;
		collidedx = false;
		collidedy = false;
		for (Rectangle r : obstacles)
		{
			detectcollision(r);
		}
		velocity.x *= FRICTION;

		//keep track of prev position of player for collision logic
		prevplayer.x = player.x;
		prevplayer.y = player.y;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		if (c == KeyEvent.VK_DOWN) //placing block mechanism
		{
			addremove(obstacles, (int)player.x, (int)(player.y + PLAYER_SIZE), PLAYER_SIZE);
		}
		else if (c == KeyEvent.VK_UP)
		{
			addremove(obstacles, (int)player.x, (int)(player.y - PLAYER_SIZE), PLAYER_SIZE);
		}
		else if (c == KeyEvent.VK_RIGHT)
		{
			addremove(obstacles, (int)(player.x + PLAYER_SIZE + 1), (int)player.y, PLAYER_SIZE);
		}
		else if (c == KeyEvent.VK_LEFT)
		{
			addremove(obstacles, (int)(player.x - PLAYER_SIZE), (int)player.y, PLAYER_SIZE);
		}
		else if (!keyspressed.contains(e.getKeyCode()))
			keyspressed.add(e.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		keyspressed.remove(e.getKeyCode());
	}

	Point2D.Double getPlayer() {
		return player;
	}

}

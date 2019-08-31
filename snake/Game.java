package snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 720;
	public static final int HEIGHT = 720; 
	public static final int BLOCK_SIZE = 30; //Do not change - size of the food and snake body part
											 //as well as their images
	private Thread thread;
	private boolean running;
	private Snake snake;
	private Food food;

	public Game(){
		initializeWindow();
		snake = new Snake(this);
		food = new Food();
		food.generateLocation(snake.getCopyOfEmptySpaces());
		initializeKeyAdapter();
		start();
	}

	private synchronized void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
		this.requestFocus();
	}
	
	public void run() {
		double amountOfTicks = 10d; //ticks amount per second
		double nsBetweenTicks = 1000000000 / amountOfTicks;
		double delta = 0;
		long lastTime = System.nanoTime();
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsBetweenTicks;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			render();
		}
	}
	
	public void tick() {
		if (snake.isDead()) {
			running = false;
		}
		else {
			if (isEating()) {
				food.generateLocation(snake.getCopyOfEmptySpaces());
			}
			snake.tick();
		}
	}
	
	public void render() {
		if (running) {
			BufferStrategy bs = this.getBufferStrategy();
			if (bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			Graphics g = bs.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			food.render(g);
			snake.render(g);
			if (snake.isDead()) {
				g.setColor(Color.white);
				g.setFont(new Font("Tahoma", Font.BOLD, 75));
				g.drawString("Game Over", Game.WIDTH / 2 - 200 , Game.HEIGHT / 2);
			}
			g.dispose();
			bs.show();
		}
	}
	
	public boolean isEating() {
		return snake.getHeadCoor().equals(food.getCoor());
	}
	
	private JFrame initializeWindow() {
		JFrame frame = new JFrame("Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		this.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return frame;
	}
	
	private void initializeKeyAdapter() { 
		//this is how to game gets keyboard input 
		//the controls are wasd keys
		class MyKeyAdapter extends KeyAdapter{
			private int velocity = Snake.DEFAULT_SPEED; //move a whole block at a time
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
				//after a key has been pressed we check if the snake goes the opposite way
				//if so, we ignore the press
				if (key == KeyEvent.VK_S) {
					if (snake.getVelY() != -velocity) {
						snake.setVel(0, velocity);
					}
				}
				else if (key == KeyEvent.VK_W) {
					if (snake.getVelY() != velocity) {
						snake.setVel(0, -velocity);
					}
				}
				else if (key == KeyEvent.VK_D) {
					if (snake.getVelX() != -velocity) {
						snake.setVel(velocity, 0);
					}
				}
				else if (key == KeyEvent.VK_A) {
					if (snake.getVelX() != velocity) {
						snake.setVel(-velocity, 0);
					}
				}
			}
		}
		this.addKeyListener(new MyKeyAdapter()); //adding it to the game
	}
	
	public static void main(String[] args) {
		Game g = new Game();
	}
}

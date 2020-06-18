package snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
	private KeyboardAdapter kba;

	public Game(){
		initializeWindow();
		snake = new Snake(this);
		food = new Food();
		food.generateLocation(snake.getCopyOfEmptySpaces());
		kba = new KeyboardAdapter(snake);
		this.addKeyListener(kba);
		start();
	}

	private synchronized void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
		this.requestFocus();
	}
	
	public void run() {
		double amountOfTicks = 12d; //ticks amount per second
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
		kba.resetHasBeenPressed();
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
	
	public static void main(String[] args) {
		Game g = new Game();
	}
}

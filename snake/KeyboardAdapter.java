package snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//this is how to game gets keyboard input 
//the controls are wasd keys
public class KeyboardAdapter extends KeyAdapter{
	private Snake snake;
	private boolean hasBeenPressed = false;
	private int velocity = Snake.DEFAULT_SPEED; //move a whole block at a time
	
	KeyboardAdapter(Snake snake){
		this.snake = snake;
	}
	
	public void resetHasBeenPressed() {
		this.hasBeenPressed = false;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		//after a key has been pressed we check if the snake goes the opposite way
		//if so, we ignore the press
		if (!hasBeenPressed) {
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
			hasBeenPressed = true;
		}
	}
}

package snake;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.ImageIcon;

public class Snake {
	public static final int DEFAULT_SPEED = Game.BLOCK_SIZE;
	private Game game;
	private int velX;
	private int velY;
	private LinkedList<Coor> body; //snake's body
	private Set<Coor> emptySpaces; //valid spots for food- spots without snake parts
	private boolean dead;
	private Image img; //img of snake body parts
	
	/*
	 * @pre: Game.HEIGHT / Game.BLOCK_SIZE == 0 && Game.WIDTH / Game.BLOCK_SIZE == 0
	 * @pre: Game.HEIGHT % 2 == 0
	 * @pre: Game.WIDTH > 3 * Game.BLOCK_SIZE
	 * @post: the snake starts at the middle of the screen
	*/
	Snake(Game game){
		this.game = game;
		body = new LinkedList<Coor>();
		//starting snake
		int halfScreenHeight = Game.HEIGHT / 2;
		body.add(new Coor(2 * Game.BLOCK_SIZE, halfScreenHeight)); //head block
		body.add(new Coor(Game.BLOCK_SIZE, halfScreenHeight)); //middle block
		body.add(new Coor(0, halfScreenHeight)); //last block
		velX = DEFAULT_SPEED;
		initializeEmptySpaces();
		initializeImage();
	}
	
	public void tick() { //updating the body and checking for death
		
		/* Updating body:
		 * Explanation: the Coor of the n-th body part is the Coor of the head n ticks ago
		 * Execution: adding the current head Coor to the body, and pushing all other
		 * Coors one place. If the snake hasn't eat this turn than we will remove 
		 * the last Coor in the body. Oterwise, it has eat and needs to grow,
		 * in that case we'll keep it
		 * Result: the body will be: [Coor now, before 1 tick, before 2 ticks, ...] 
		*/
		int prevHeadX = body.getFirst().getX();
		int prevHeadY = body.getFirst().getY();
		body.push(new Coor(prevHeadX + velX, prevHeadY + velY)); //new head Coor
		if (!game.isEating()) {
			Coor lastCoor = body.getLast();
			body.removeLast();
			emptySpaces.add(lastCoor); //now there is no body part on it 
		}
		emptySpaces.remove(getHeadCoor()); 
		
		checkDeath();
	}
	
	public void render(Graphics g) {
		
		for (Coor curr : body) {
			g.drawImage(img, curr.getX(), curr.getY(), null);
		}
	}

	private void checkDeath() {
		Coor h = getHeadCoor();
		if (h.getX() < 0 || h.getX() > Game.WIDTH - Game.BLOCK_SIZE) { //invalid X
			dead = true;
		}
		else if (h.getY() < 0 || h.getY() > Game.HEIGHT - Game.BLOCK_SIZE) { //invalid Y
			dead = true;
		}
		else {
			dead = false;	
			for (int i = 1; i < body.size(); i++) { //compare every non-head body part's coor with head's corr
				if (getHeadCoor().equals(body.get(i))) { //head touched a body part
					dead = true;
				}
			}
		}
	}
	
	public void setVel(int velX, int velY) {
		this.velX = velX;
		this.velY = velY;
	}

	public int getVelX() {
		return velX;
	}
	
	public int getVelY() {
		return velY;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public Set<Coor> getCopyOfEmptySpaces() {
		return new HashSet<Coor>(emptySpaces);
	}
	
	private void initializeEmptySpaces() {
		emptySpaces = new HashSet<Coor>();
		for (int i = 0; i * Game.BLOCK_SIZE < Game.WIDTH; i++) {
			for (int j = 0; j * Game.BLOCK_SIZE < Game.HEIGHT; j++) {
				emptySpaces.add(new Coor(i * Game.BLOCK_SIZE, j * Game.BLOCK_SIZE));
			}
		}
		emptySpaces.removeAll(body); //remove the starting snake parts
	}
	
	private void initializeImage() {
		ImageIcon icon = new ImageIcon("res/snake.png");
		img = icon.getImage();
	}
	
	public Coor getHeadCoor() {
		return body.getFirst();
	}
}

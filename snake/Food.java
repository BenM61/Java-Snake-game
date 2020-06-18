package snake;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;


public class Food {
	private Image img;
	private Coor coor;
	
	Food(){
		initializeImages();
	}
	
	public void render(Graphics g) {
		g.drawImage(img, coor.getX(), coor.getY(), null);
	}
	
	public void generateLocation(Set<Coor> set) { //picking a random coordinate for the food
		int size = set.size();
		Random rnd = new Random();
		int rndPick = rnd.nextInt(size);
		Iterator<Coor> iter = set.iterator();
		for (int i = 0; i < rndPick; i++) {
			iter.next();
		}
		Coor chosenCoor = iter.next();
		coor = chosenCoor;
	}
	
	private void initializeImages() {
		ImageIcon icon = new ImageIcon("res/food.png");
		img = icon.getImage();
	}
	
	public Coor getCoor() {
		return coor;
	}
}

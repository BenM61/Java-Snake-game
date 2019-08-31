package snake;

public class Coor { //coordinates
	//we divide the screen to rows and columns, distance
	//between two rows or two columns is Game.BLOCK_SIZE
	private int x;
	private int y;
	
	Coor(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() { 
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	@Override
	public int hashCode() {
		return x * Game.WIDTH + y;
	}
	
	@Override
	public boolean equals(Object o) {
		Coor c = (Coor) o;
		if (x == c.getX() && y == c.getY()) {
			return true;
		}
		return false;
	}
}

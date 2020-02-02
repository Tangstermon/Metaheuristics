
public class Point {
	
	int CoorX;
	int CoorY;
	
	//Creates a point object 
	public Point(int x, int y){
		CoorX = x;
		CoorY = y;
	}
	

	public int getX(){
		return CoorX;
	}
	
	public int getY(){
		return CoorY;
	}
	
	public String toString(){
		return "" +CoorX + "," + CoorY;
	}
}

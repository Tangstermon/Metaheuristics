
public class ComparePoint {

	public ComparePoint(){
		
	}
	
	public static boolean compare(Point p1, Point p2){
		if(p1.getX() == p2.getX() && p1.getY() == p2.getY()){
			return true;
		}
		else if(p1.getX() == p2.getY() && p1.getY() == p2.getX()){
			return true;
		}
		return false;
		
	}
}

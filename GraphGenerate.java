import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
/**
 * Related graph methods 
 * @author Anthony
 *
 */
public class GraphGenerate {

	int size;
	int graphArr[][];
	RandomNumber r = new RandomNumber(100, 0);
	/**
	 * Initialises the size of the graph
	 * @param size
	 */
	public GraphGenerate(int size){
		this.size= size;
		graphArr = new int[size][size];
	}
	
	/**
	 * Returns the graph 
	 * @return graphArr
	 */
	public int[][] getGraph(){
		return graphArr;
	}
	
	/**
	 * Populates the graph with weights
	 * @param g
	 */
	public void setGraph(int[][] g){
		for(int i =0; i< size; i++){
			for(int j=i; j< size; j++){
				if(i == j){
					g[i][j] = 0;
				}
				else{
					g[i][j] = r.randWeights();
				}
			}
		}
	}
	/**
	 * Prints the graph
	 * @param g graph to be printed
	 */
	public void printGraph(int[][] g){
		System.out.print("Node  ");
		for(int a = 0; a<size; a++){
			if(a<10){
				System.out.print(a + "  ");
			}
			else{
				System.out.print(a + " ");
			}
		}
		System.out.println();
		for(int i=0; i< size; i++){
			if(i<10){
				System.out.print("   " + i+ "  ");
			}
			else{
				System.out.print("   " + i+ " ");
			}
			for(int j=0; j<size; j++){
				if(g[i][j] < 10){
					System.out.print(+g[i][j]+ "  " );
				}
				else{
					System.out.print(+g[i][j]+ " " );
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * Generates a single random edge
	 * @param graph allows access to the graph
	 * @return initial the new edge created
	 */
	public ArrayList<Point> initialSolution(int[][] graph){
		RandomNumber r = new RandomNumber(graph.length-1, 0);
		int x=r.randInitial();
		int y=r.randInitial();
		while(graph[x][y] == 0 ){
			x = r.randInitial();
			y = r.randInitial();
		}
		ArrayList<Point> initial = new ArrayList<Point>();
		initial.add(new Point(x,y));
		return initial;
	}
	
	/**
	 * Generates the solution based on how many the user has input
	 * @param g graph object
	 * @param arr edge list
	 * @param index A recursive function so it finds neighbours based its index
	 * @param edges the number of edges to be present in the solution
	 * @return
	 */
	public ArrayList<Point> initial(int[][] g, ArrayList<Point> arr,int index, int edges){
		ArrayList<Point> neighbourCoor = new ArrayList<Point>();
		//Locates neighbours from the arr list
		for(int i =0; i<g.length; i++){
			Point p1 = new Point(arr.get(index).getX(), i);
				if(g[p1.getX()][p1.getY()] != 0 ){
					neighbourCoor.add(p1);
				}
				else if(g[p1.getY()][p1.getX()] !=0 ){
					neighbourCoor.add(p1 = new Point(i, arr.get(index).getX()));
				}
				
			}
		//Checks for missed neighbours
		for(int j=0; j<g.length; j++){
			Point p2 = new Point(arr.get(index).getY(), j);

				if(g[p2.getX()][p2.getY()] != 0 ){
					neighbourCoor.add(p2 = new Point(arr.get(index).getY(), j));
					
				}
				else if(g[p2.getY()][p2.getX()] !=0 ){
					neighbourCoor.add(p2 = new Point(j, arr.get(index).getY()));
				}
				
			}
		
		
		
		//randomly selects a neighbour and adds to the arr list
		RandomNumber ran = new RandomNumber(neighbourCoor.size()-1, 0);
		int nIndex = ran.randInitial();
		for(int i = 0; i< arr.size(); i++){
			while(ComparePoint.compare(neighbourCoor.get(nIndex), arr.get(i))){
				nIndex = ran.randInitial();
			}
		}
		arr.add(neighbourCoor.get(nIndex));
		
		//Continues until arr is the size of the number of edges meaning a valid solution is created
		if(arr.size()-1  == edges){
			return arr;
		}else{
			initial(g, arr, index+1, edges);

		}
		
		return arr;
	}
	
	/**
	 * Generates neieghbours for every edge in the solution
	 * @param g access graph
	 * @param arr the current solution
	 * @return neighbourCoor The generated list of neighbours
	 */
	public ArrayList<Point> ha(int[][] g, ArrayList<Point> arr){
		ArrayList<Point> neighbourCoor = new ArrayList<Point>();
		for(Point p : arr){
			//Iterates through the graph for any connected vertex
			for(int i =0; i<g.length; i++){
				Point p1 = new Point(p.getX(), i);
				//does not allow edges currently in the solution to be considered
				if(ComparePoint.compare(p1, p)== false){
					if(g[p1.getX()][p1.getY()] != 0){
						neighbourCoor.add(p1 = new Point(p.getX(), i));
					}
					else if(g[p1.getY()][p1.getX()] !=0){
						neighbourCoor.add(p1 = new Point(i, p.getX()));
					}
				}
			}
		}
		for(Point a: arr){	
			for(int j=0; j<g.length; j++){
				Point p2 = new Point(a.getY(), j);
				if(ComparePoint.compare(p2, a)== false){
					if(g[p2.getX()][p2.getY()] != 0){
						neighbourCoor.add(p2 = new Point(a.getY(), j));
					}
					else if(g[p2.getY()][p2.getX()] !=0){
						neighbourCoor.add(p2 = new Point(j, a.getY()));
					}
				}
			}
		}
		
		
		
		for(int i =0; i<arr.size(); i++){
			for(int j =0; j<neighbourCoor.size(); j++){
				if(ComparePoint.compare(arr.get(i), neighbourCoor.get(j))){
					neighbourCoor.remove(j);
				}
			}
		}
		
		//System.out.println(neighbourCoor);
		return neighbourCoor;
	}
	
	/**
	 * Checks if the solution is a tree or not
	 * @param current checks if this is connected
	 * @return boolean
	 */
	public boolean connected(ArrayList<Point> current){

		ArrayList<Point> visited = new ArrayList<Point>();
		ArrayList<Point> temp = new ArrayList<Point>();
		temp.addAll(current);

		visited.add(current.get(0));
		boolean connected = true;
		
		//Iterates through the arraylist and the visited edges are added to another list 
		for(int i = 0; i < visited.size(); i++) {
			for(int j =0; j < temp.size(); j++){
				int visitedxCoor =  visited.get(i).getX();
				int visitedyCoor = visited.get(i).getY();
				int tempxCoor = temp.get(j).getX();
				int tempyCoor = temp.get(j).getY();

				if(visitedxCoor == tempxCoor || visitedxCoor == tempyCoor || visitedyCoor == tempxCoor 
						|| visitedyCoor == tempyCoor){
					visited.add(temp.get(j));
					temp.remove(j);
					
				}
			}
		}
		
		//If the list is not empty then the solution is disconnected
		if(temp.size() != 0){
			connected = false;
			return connected;
		}

		return connected;

	}
}




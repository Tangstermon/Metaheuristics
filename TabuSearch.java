import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * Tabu search implementation class
 * @author Anthony
 *
 */
public class TabuSearch {

	int maxTabuListSize;
	static HashMap<Point, Integer> tabuList = new HashMap<Point, Integer>();

	public TabuSearch(){

	}
	
	/**
	 * Checks the size of the tabu list
	 * @param tabuList Forbidden moves are checked 
	 * @param neighbourCoor Checks whether the neighbours are in the tabu list
	 * @param index the position of the neighbour in the arraylist
	 * @return boolean 
	 */
	public static boolean checkTabuList(HashMap<Point, Integer> tabuList, ArrayList<Point> neighbourCoor, int index){
		if(tabuList.size()==0){
			return false;
		}
		for(Point p: tabuList.keySet() ){
			if(compare(p,(neighbourCoor.get(index)))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * compares if two points are the same
	 * @param p one point
	 * @param point another point 
	 * @return boolean
	 */
	private static boolean compare(Point p, Point point) {
		if(p.getX() == point.getX() && p.getY() == point.getY()){
			return true;
		}
			
		return false;
	}

	/**
	 * Checks if the tabu list if full
	 * @return boolean
	 */
	public boolean checkTabuListFull(){
		if(tabuList.size() < maxTabuListSize){
			return false;
		}
		return true;
	}
	
	/**
	 * Termination criteria set by the user
	 * @param iterations number of iterations till termination
	 * @return iterations 
	 */
	public static int terminationCriteria(int iterations){
		return iterations;
	}
	
	
	
	/**
	 * The implementation of Tabu search
	 * @param g GraphGenerate object containing methods which access the graph
	 * @param edges number of edges in the solution
	 * @param iterations number of loops to run for
	 * @return best An arraylist<Point> storing the best k-tree solution
	 */
	public ArrayList<Point> TS(GraphGenerate g, int edges, int iterations){
		int counter = 0;
		ArrayList<Point> currentSolution = new ArrayList<Point>();
		
		if(edges <2){
			currentSolution = g.initialSolution(g.getGraph());
		}
		else{
			
			int newEdges = edges-1;
			ArrayList<Point> initialSol = g.initialSolution(g.getGraph());
			currentSolution = g.initial(g.getGraph(), initialSol, 0, newEdges);
			
		}
		
		//Sets the best solution to the current solution
		ArrayList<Point> best = new ArrayList<Point>();
		for(Point p : currentSolution){
			best.add(p);
		}

		System.out.println(best);
		ArrayList<Point> neighbours = new ArrayList<Point>();
		//Moves in to the main loop
		while(counter<terminationCriteria(iterations)){
			ArrayList<Point> candidateSolutions = new ArrayList<Point>();
			
			
			System.out.println("TabuList before: " + tabuList);
			removeTabuOrNot(tabuList);
			decrement(tabuList);
			//Generates a list of adjacent edges for the current solution
			neighbours = g.ha(g.getGraph(), currentSolution);

			System.out.println("Neighbours: " + neighbours);

			//Adding neighbours to candidate list if not in tabu list
			for(int start = 0; start< neighbours.size(); start++){
				if(checkTabuList(tabuList, neighbours, start) == false){
					candidateSolutions.add(neighbours.get(start));	
				}
			}
			System.out.println("Candidate: " +candidateSolutions);
			
			//Gets the best candidate to add to current solution and 
			//gets cost of the worst edge in the current solution
			Point bestCandidate = getMin(candidateSolutions,g);
			int indexOfCurrentMax = getMax(currentSolution, g);
			
			System.out.println("Current Solution: " + currentSolution  + " "+  getCost(currentSolution, g));
			System.out.println("Best candidate: "  + ""+g.getGraph()[bestCandidate.getX()][bestCandidate.getY()]);
			
			
			//If candidate solution is better then swap
			if(g.getGraph()[currentSolution.get(indexOfCurrentMax).getX()][currentSolution.get(indexOfCurrentMax).getY()] >
			g.getGraph()[bestCandidate.getX()][bestCandidate.getY()]){
				
				//Add to the arraylist first
				currentSolution.add(bestCandidate);
				
				//Check every edge and remove to see if connected or not
				for(int i  =0; i< currentSolution.size(); i++){
					Point p = currentSolution.remove(i);
					if(g.connected(currentSolution)){
						break;
					}
					else{
						currentSolution.add(i, p);
					}
				}
				
				//Add new edge to tabu list and update best solution
				addTabu(tabuList, bestCandidate);
				if(getCost(best, g) > getCost(currentSolution,g)){
					best.clear();
					for(Point p : currentSolution){
						best.add(p);
					}
					System.out.println("All time best:" +best);
				}

			}
			
			//If there are no better moves then swap
			else if (g.getGraph()[currentSolution.get(indexOfCurrentMax).getX()][currentSolution.get(indexOfCurrentMax).getY()] <
					g.getGraph()[bestCandidate.getX()][bestCandidate.getY()]){
				
				//Add to the arraylist first
				currentSolution.add(bestCandidate);
				
				//Check every edge and remove to see if connected or not
				for(int i  =0; i< currentSolution.size(); i++){
					Point p = currentSolution.remove(i);
					System.out.println(p + " " + g.connected(currentSolution));
					if(g.connected(currentSolution)){
						break;
					}
					else{
						currentSolution.add(i, p);
					}
				}
				addTabu(tabuList, bestCandidate);
			}
			System.out.println();
			counter++;
			
		}
		int bestCost = getCost(best, g);
		System.out.println("Best solution:" +best + " " + "weight " + bestCost);
		return best;
	}
	
	/**
	 * Retrieves the edge with the least cost in the potential edges list 
	 * @param candidateCoor the list of possible edges to be added to the current
	 * @param g GraphGenerate object for accesing the graph
	 * @return p The edge that has the least cost
	 */
	public static Point getMin(ArrayList<Point> candidateCoor, GraphGenerate g){
		int min = g.getGraph()[candidateCoor.get(0).getX()][candidateCoor.get(0).getY()];
		int index = 0;
		for(int i = 0; i< candidateCoor.size(); i++){
			if(g.getGraph()[candidateCoor.get(i).getX()][candidateCoor.get(i).getY()] < min){
				min = g.getGraph()[candidateCoor.get(i).getX()][candidateCoor.get(i).getY()];
				index = i;
			}
		}
		//ArrayList<Point> temp = new ArrayList<Point>();
		Point p = candidateCoor.get(index);
		candidateCoor.remove(index);
		System.out.println(candidateCoor);
		return p;
	}
	
	/**
	 * Retrieves the maximum edge cost from a list
	 * @param candidateCoor contains list of edges
	 * @param g GraphGenerate object to access graph
	 * @return index the position of the edge
	 */
	public static int getMax(ArrayList<Point> candidateCoor, GraphGenerate g){
		int max = g.getGraph()[candidateCoor.get(0).getX()][candidateCoor.get(0).getY()];
		int index = 0;
		for(int i = 0; i< candidateCoor.size(); i++){
			if(g.getGraph()[candidateCoor.get(i).getX()][candidateCoor.get(i).getY()] > max){
				max = g.getGraph()[candidateCoor.get(i).getX()][candidateCoor.get(i).getY()];
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Checks if the tabu tenure has reached 0 and removes it
	 * @param tabuList list to be checked
	 */
	public static void removeTabuOrNot(HashMap<Point, Integer> tabuList){
		Iterator<HashMap.Entry<Point, Integer>> it = tabuList.entrySet().iterator();
		while(it.hasNext()){	
			HashMap.Entry<Point, Integer> hm = it.next();
			if(hm.getValue()==0){
				it.remove();
			}
		}
	}
	
	/**
	 * Adds an edge to the list and it stays in the list for 4 iterations at default
	 * @param tabuList list to be added towards
	 * @param p edge to be added
	 */
	public static void addTabu(HashMap<Point, Integer> tabuList, Point p){
		tabuList.put(p, 4);
	}
	
	/**
	 * After each iteration the time of the edges in the tabu list also decreases
	 * @param tabuList list to be iterated over
	 */
	public static void decrement(HashMap<Point, Integer> tabuList){
		Iterator<Map.Entry<Point, Integer>> it = tabuList.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Point, Integer> entry = it.next();
			tabuList.put(entry.getKey(), entry.getValue()-1);
		}
	}
	/**
	 * Gets the cost of the solution 
	 * @param CurrentSolution the solution that is being calculated
	 * @param g Access the graph for the cost
	 * @return sum Total value of the cost
	 */
	public int getCost(ArrayList<Point> CurrentSolution, GraphGenerate g){
		int sum = 0;
		for(Point p: CurrentSolution){
			sum = sum + g.getGraph()[p.getX()][p.getY()];
		}
		return sum;
	}
	



	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter one number for size of matrix e.g 10 for 10x10 matrix: ");
		int size = scan.nextInt();
		
		GraphGenerate g = new GraphGenerate(size);
		g.setGraph(g.getGraph());
		g.printGraph(g.getGraph());
		TabuSearch ts = new TabuSearch();
		ts.TS(g, 3, 10);
		
	
	

	}
}

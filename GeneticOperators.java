import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;


public class GeneticOperators {
	/**
	 * Constructor
	 */
	public GeneticOperators(){
		
	}
	/**
	 * Get minimum value of the candidates
	 * @param candidateCoor the list of potential candidates
	 * @param g graph object
	 * @return p the edge with minimum cost in the list
	 */
	public Point getMin(ArrayList<Point> candidateCoor, GraphGenerate g){
		int min = g.getGraph()[candidateCoor.get(0).getX()][candidateCoor.get(0).getY()];
		int index = 0;
		for(int i = 0; i< candidateCoor.size(); i++){
			if(g.getGraph()[candidateCoor.get(i).getX()][candidateCoor.get(i).getY()] < min){
				min = g.getGraph()[candidateCoor.get(i).getX()][candidateCoor.get(i).getY()];
				index = i;
			}
		}
		Point p = candidateCoor.get(index);
		candidateCoor.remove(index);
		return p;
	}
	
	/**
	 * Gets the max an edge
	 * @param candidateCoor list of edges
	 * @param g graph object
	 * @return index the position of where the edge is
	 */
	public int getMax(ArrayList<Point> candidateCoor, GraphGenerate g){
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
	 * The mutation operator 
	 * @param a solution is be mutated
	 * @param g graph object 
	 * @param mutateProb probability that mutation succeeds
	 * @return
	 */
	public ArrayList<Point> mutate(ArrayList<Point> a, GraphGenerate g, int mutateProb){
		
		//Chooses a random position 
		Random ran = new Random();
		int n = ran.nextInt(a.size());
		int prob = ran.nextInt(mutateProb);
		
		int length = a.size();
		int graphLength = g.getGraph().length;
		
		//If mutation happens it removes the edge at the random position
		//checks for valid edges and adds when it has found one
		if(prob == 0){
			Point p = a.remove(n);
			for(int i = 0; i < graphLength; i++){
				Point p2 = new Point(p.getX(), i);
				if(g.getGraph()[p.getX()][i] != 0){
					for(int j = 0; j< length; j++){
						if(length == a.size()){
							break;
						}
						if(!ComparePoint.compare(a.get(j), p2)){
							a.add(p2);
						}
					}
				}
			}
		}
		return a;
	}
	
	/**
	 * Fixes a solution if it somehow became not valid
	 * @param sol solution to be fixed
	 * @param g graph object to access the graph
	 * @return sol the newly fixed solution
	 */
	public ArrayList<Point> fixSolution(ArrayList<Point> sol, GraphGenerate g){
		ArrayList<Point> neighbourCoor = new ArrayList<Point>();
		
		//Generates neighbours on current solution
		neighbourCoor = g.ha(g.getGraph(), sol);
		int neighbourSize =  neighbourCoor.size();
		int solSize = sol.size();
	
		int indexI = 0;
		int indexJ = 0;
		
		//Removes duplicate edges if present
		loop:
		for(int i = 0; i < solSize; i++){
			for(int j = 1; j< solSize; j++){
				if(ComparePoint.compare(sol.get(i), sol.get(j))){
					indexI = i;
					indexJ = j;
					sol.remove(indexI);
					break loop;
				}
			}
		}
		
		solSize = sol.size();
		
		//Connects the remaining edges with an edge in the neighbour pool
		outerLoop:
		for(int i = 0; i< neighbourSize; i++){
			for(int j = 0; j < solSize; j++){
				for(int k = j +1; k< solSize; k++){
					if((neighbourCoor.get(i).getX() == sol.get(j).getX() && neighbourCoor.get(i).getY() == sol.get(k).getX())
							|| (neighbourCoor.get(i).getX()== sol.get(j).getY() && neighbourCoor.get(i).getY() == sol.get(k).getX())
							|| (neighbourCoor.get(i).getX()== sol.get(j).getX() && neighbourCoor.get(i).getY() == sol.get(k).getY())
							|| (neighbourCoor.get(i).getX()== sol.get(j).getY() && neighbourCoor.get(i).getY() == sol.get(k).getY())
							|| (neighbourCoor.get(i).getX()== sol.get(k).getX() && neighbourCoor.get(i).getY() == sol.get(j).getY())
							|| (neighbourCoor.get(i).getX()== sol.get(k).getX() && neighbourCoor.get(i).getY() == sol.get(j).getX())){
							sol.add(neighbourCoor.get(i));
							break outerLoop;
					}
				}
			}
		}
		
		return sol;
		
	}
	
	/**
	 * Check duplicate edges in the solution
	 * @param sol solution to be checked
	 * @return duplicate true if duplicate present false otherwise
	 */
	public boolean checkDuplicate(ArrayList<Point> sol){
		int solSize = sol.size();
		
		boolean duplicate = false;
	
		for(int i = 0; i < solSize; i++){
			for(int j = 1; j< solSize; j++){
				if(ComparePoint.compare(sol.get(i), sol.get(j)) && i != j){
					duplicate = true;
					return duplicate;
				}
			}
		}
		return duplicate;
	}
	
	/**
	 * Check connectivity of solutions
	 * @param current
	 * @return
	 */
	public boolean connected(ArrayList<Point> current){

		ArrayList<Point> visited = new ArrayList<Point>();
		ArrayList<Point> temp = new ArrayList<Point>();
		temp.addAll(current);

		visited.add(current.get(0));
		boolean connected = true;
		
		//Adds vertices to the visited list and if any vertices is left after this in the temp
		//then it is disconnected
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

		if(temp.size() != 0){
			connected = false;
			return connected;
		}

		return connected;

	}


	/**
	 * Computes the fitness for the genetic and memetic algorithm
	 * @param solution calculates the fitness from the edges
	 * @param g looks up the graph
	 * @return total the fitness result
	 */
	public int computeFitness(ArrayList<Point> solution, GraphGenerate g){

		int total = 0;
		int solutionLength = solution.size();
		
		//Gets the value in the coordinates and stores in a HashMap
		for(int i=0; i < solutionLength; i++){
			total = total + g.getGraph()[solution.get(i).getX()][solution.get(i).getY()];
		}
	
		return total;
	}
	
	/**
	 * Returns fitness from the hashmap
	 * @param hm map to be iterated through
	 * @return fitnessValues stores the fitness values from the hash map here
	 */
	public ArrayList<Integer> getFitness(HashMap hm){
		ArrayList<Integer> fitnessValues = new ArrayList<Integer>();
		Set<Entry<Integer, ArrayList<Point>>> set = hm.entrySet();
		Iterator<Map.Entry<Integer, ArrayList<Point>>> iterator = set.iterator();
		while(iterator.hasNext()) {
			Entry<Integer,ArrayList<Point>> entry = iterator.next();
			fitnessValues.add(entry.getKey());
		}
		
		return fitnessValues;
		
	}
	
	
	/**
	 * Cross over phase by combing edges with two parents to create two offspring
	 * @param parentOne first parent
	 * @param parentTwo second parent
	 * @param edges number of edges in the solution
	 * @return
	 */
	public ArrayList<ArrayList<Point>> crossover(ArrayList<Point> parentOne, ArrayList<Point> parentTwo, int edges){
		
		
		ArrayList<ArrayList<Point>> Offspring = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> childOne = new ArrayList<Point>();
		ArrayList<Point> childTwo = new ArrayList<Point>();
		//Randomly selects crossover site
		Random ran = new Random();
		int n = ran.nextInt(parentOne.size());
		
		int length = parentTwo.size();
		//Length checking
		if(length > edges){
			length= length -1;
		}
		//Gets the edges from the begining
		for(int crossPoint = n; crossPoint < length; crossPoint++ ){
			childOne.add(parentOne.get(crossPoint));
			childTwo.add(parentTwo.get(crossPoint));
			
		}
		//Gets the edges from the end
		for(int j = n-1; j >= 0; j--){
			childOne.add(parentTwo.get(j));
			childTwo.add(parentOne.get(j));
		}
		
		Offspring.add(childOne);
		Offspring.add(childTwo);
		
		return Offspring;
	}
}

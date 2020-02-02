import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
/**
 * Memetic algorithm implementation
 * @author Anthony
 *
 */
public class MemeticAlg {
	
	/**
	 * The local search algorithm
	 * @param sol solution where hill climbing is used on
	 * @param graph graph object
	 * @param go genetic operator object
	 * @return sol The solution after hill climbing has been executed
	 */
	public static ArrayList<Point> hillClimbing(ArrayList<Point> sol, GraphGenerate graph, GeneticOperators go){
		ArrayList<Point> neighbours = new ArrayList<Point>();
		Point bestMin = null;
		int indexOfMax =0;
		int solSize = sol.size();
		neighbours = graph.ha(graph.getGraph(),sol);
		bestMin = go.getMin(neighbours, graph);
		indexOfMax = go.getMax(sol, graph);
		
		
		if(graph.getGraph()[sol.get(indexOfMax).getX()][sol.get(indexOfMax).getY()] >
		graph.getGraph()[bestMin.getX()][bestMin.getY()]){
			
			sol.add(bestMin);
			
			for(int k = 0; k< solSize; k++){
				Point p = sol.remove(k);
				if(go.connected(sol) || go.checkDuplicate(sol)){
					break;
				}
				else{
					sol.add(k, p);
				}
			}
				
		}
		return sol;
	}
	
	/**
	 * Actual implementation of the algorithm
	 * @param populationSize number of individuals
	 * @param generations number of iterations
	 * @param mutateProb probability to succeed mutation
	 * @param graph graph object
	 * @param edges number of edges to include in the solution
	 * @param go allows access to the genetic operator methods
	 * @return ArrayList<Point> solution at the end the algorithm
	 */
	public ArrayList<Point> memetic(int populationSize, int generations, int mutateProb, GraphGenerate graph, int edges, GeneticOperators go){
	
		ArrayList<Point> currentSolution = new ArrayList<Point>();
		ArrayList<ArrayList<Point>> pop = new ArrayList<ArrayList<Point>>();
		
		ArrayList<Integer> fitnessValues = new ArrayList<Integer>();
		int best = 0;
		HashMap<Integer, ArrayList<Point>> map = new HashMap<Integer, ArrayList<Point>>();
		if(edges <2){
			for(int i= 0; i< populationSize; i++){
				currentSolution = graph.initialSolution(graph.getGraph());
				pop.add(currentSolution);
			}
		}
		else{
			//Population initialisation
			for(int i =0; i< populationSize; i++){
				int newEdges = edges -1;
				ArrayList<Point> initialSol = graph.initialSolution(graph.getGraph());
				currentSolution = graph.initial(graph.getGraph(), initialSol, 0, newEdges);
				
				map.put(go.computeFitness(currentSolution,graph), currentSolution);
				fitnessValues = go.getFitness(map);
				
			}
			
			System.out.println(currentSolution);
			System.out.println(map);
			
			int fitnessValueSize = fitnessValues.size();
			best = fitnessValues.get(0);
			
			//Generations 
			for(int g = 0; g < generations; g++){
				
				System.out.println("Gen: "  + g);
				Collections.sort(fitnessValues);
				System.out.println(fitnessValues);
				
				int crossOverPop = Math.round(fitnessValues.size()/2);

				ArrayList<Integer> temp = new ArrayList<Integer>(populationSize);
				
				//Randomly choose in top fitness values which should be the parents 
				Random ran = new Random();
				int parentOne = ran.nextInt(crossOverPop);
				int parentTwo = ran.nextInt(crossOverPop);
				while(parentTwo == parentOne){
					parentTwo = ran.nextInt(crossOverPop);
				}
				
				//selects candidates to be possible parents
				for(int i =0; i < crossOverPop; i++){
					temp.add(fitnessValues.get(i));
				}
				
				
				//Crossover and mutation phase
				ArrayList<ArrayList<Point>> Offspring = new ArrayList<ArrayList<Point>>();
				Offspring = go.crossover(map.get(temp.get(parentOne)), map.get(temp.get(parentTwo)), edges);
				int offspringSize = Offspring.size();
				ArrayList<Point> tempOffspring = new ArrayList<Point>();
				for(int i = 0; i < offspringSize; i++){
					tempOffspring = go.mutate(Offspring.get(i), graph, mutateProb);
					Offspring.remove(i);
					Offspring.add(i, tempOffspring);
				}
				
				
				//Applies hill climbing and if result is not a tree then it is sent to be fixed
				for(int i = 0; i< offspringSize; i++){
					tempOffspring = hillClimbing(Offspring.get(i), graph, go);
					if(go.checkDuplicate(tempOffspring) || !go.connected(tempOffspring)){
						ArrayList<Point> fixed  = go.fixSolution(tempOffspring, graph);
						if(fixed.size()< 3){
							fixed = graph.initial(graph.getGraph(), fixed, 0, 2);
						}
						
						Offspring.remove(i);
						Offspring.add(i,fixed);
					}
				}
				
				//Computes fitness of the offspring and puts it in the list of solutions
				for(int i = 0; i < offspringSize; i++){
					
					
					int offspringfitness = go.computeFitness(Offspring.get(i), graph);
					//System.out.println("aaa" + Offspring.contains(offspringfitness));
					if(!fitnessValues.contains(offspringfitness)){
						map.remove(fitnessValues.get(fitnessValueSize-1));
						fitnessValues.remove(fitnessValueSize-1);
						map.put(offspringfitness, Offspring.get(i));
						fitnessValues.add(offspringfitness);
					}
					
					//Sorts fitness in ascending order
					Collections.sort(fitnessValues);
				
				}
				
				System.out.println(map);
				
				//Updates the best solution if there is a better one available
				if(best > fitnessValues.get(0) && go.connected(map.get(fitnessValues.get(0)))){
					best = fitnessValues.get(0);
					System.out.println("Value " + best + " THE BEST "  + map.get(best));
				}
				
				System.out.println();
			}
			
		}
		System.out.println("End map " +map);
		System.out.println("Best solution found:" + map.get(best) + " weight:" + best);
		return map.get(best);
		
		}
	}
	



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
/**
 * Class containing the implementation of the genetic algorithm
 * @author Anthony
 *
 */
public class GeneticAlg {

	/**
	 * Constructor
	 */
	public GeneticAlg(){
	}

	/**
	 * Implementation of the genetic algorithm
	 * @param populationSize amount of individuals in one population
	 * @param generations the amount of iterations to run through the algorithm
	 * @param mutateProb the probability an individual will mutate
	 * @param graph object used to access the graph containing weights
	 * @param edges number of edges in the solution
	 * @param go the object to access the genetic methods
	 * @return map.get(best) the solution to the problem
	 */
	public ArrayList<Point> genetic(int populationSize, int generations, int mutateProb, GraphGenerate graph, int edges, GeneticOperators go){
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
				
				//Adds the fitness and solutions to the map and also a corresponding fitness list
				//for easier searching of edges in the map
				map.put(go.computeFitness(currentSolution,graph), currentSolution);
				fitnessValues = go.getFitness(map);
				
			}
			System.out.println(currentSolution);
			System.out.println(map);
			
		
			int fitnessValueSize = fitnessValues.size();
			best = fitnessValues.get(0);
			
			//Generations 
			for(int g = 0; g < generations; g++){
				//Sort the fitness in ascending order to determine fittest individuals
				System.out.println("Gen: "  + g);
				Collections.sort(fitnessValues);
				System.out.println(fitnessValues);
				
				//The position where it selects the potential parents
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
				ArrayList<Point> mutatedOffspring = new ArrayList<Point>();
				for(int i = 0; i < offspringSize; i++){
					mutatedOffspring = go.mutate(Offspring.get(i), graph, mutateProb);
					Offspring.remove(i);
					Offspring.add(i, mutatedOffspring);
				}
				
				//If the resulted offspring is not a tree then it fixes it
				for(int i = 0; i< offspringSize; i++){
					if(go.checkDuplicate(Offspring.get(i)) || !go.connected(Offspring.get(i))){
						ArrayList<Point> fixed  = go.fixSolution(Offspring.get(i), graph);
						if(fixed.size()< 3){
							fixed = graph.initial(graph.getGraph(), fixed, 0, 2);
						}
						
						Offspring.remove(i);
						Offspring.add(i,fixed);
					}
				}
				
				//Computes fitness of the offspring and puts it in the list of solutions
				System.out.println(offspringSize);
				for(int i = 0; i < offspringSize; i++){
					int offspringfitness = go.computeFitness(Offspring.get(i), graph);
					if(!fitnessValues.contains(offspringfitness)){
						System.out.println("Offspring " + Offspring.get(i) + " " + "Offspring fitness:"+offspringfitness);
						System.out.println("Remove from map:"+map.remove(fitnessValues.get(fitnessValueSize-1)));
						System.out.println("List " + fitnessValues + " " + "Remove from list:" +fitnessValues.remove(fitnessValueSize-1));
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
	
	public static void main(String[] args){
		GeneticAlg ga = new GeneticAlg();
		GeneticOperators go = new GeneticOperators();
		Scanner scan = new Scanner(System.in);
		System.out.println("Input size n of graph e.g n * n: ");
		int graphSize = scan.nextInt();
		GraphGenerate graph = new GraphGenerate(graphSize);
		graph.setGraph(graph.getGraph());
		graph.printGraph(graph.getGraph());
		
		
		System.out.print("Input number of edges for solution:");
		int edges = scan.nextInt();
		System.out.print("Input size of population:");
		int populationSize = scan.nextInt();
		System.out.print("Input number of generations to run for:");
		int generations = scan.nextInt();
		System.out.print("Input mutatation probability 1/x: ");
		int mutateProb = scan.nextInt();
		
		MemeticAlg ma = new MemeticAlg();
		ma.memetic(10, 10, 100, graph, 3, go);
		
		scan.close();
	
	}
}

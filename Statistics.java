import java.util.Arrays;
import java.util.Collections;
/**
 * Experiment for performance
 * @author Anthony
 *
 */
public class Statistics {

	static int sum;
	
	/**
	 * Constructor
	 */
	public Statistics(){
			
	}
	
	/**
	 * Returns a list of the scores generated from each run of the algorithm
	 * @param tb Tabu search object to access the algorithm
	 * @param ga genetic algorithm object
	 * @param ma memetic algorithm object
	 * @param graph GraphGenerate object 
	 * @param go genetic operator object
	 * @param a indicates which algorithm to run
	 * @param numberOfRuns the number of times o run the algorithm
	 * @param pop number of individuals in the population
	 * @param gen number of iterations for evolutionary algorithm
	 * @return
	 */
	public int[] score(TabuSearch tb, GeneticAlg ga, MemeticAlg ma, GraphGenerate graph,
			GeneticOperators go, int a, int numberOfRuns, int pop, int gen){
		
		
		int geneticSum = 0;
		
		//Gets the cost of the results and stores in an array
		int[] score = new int[numberOfRuns];
		for(int i = 0; i < numberOfRuns; i++){
			if(a ==1){
				score[i] = tb.getCost(tb.TS(graph, 3, 10), graph);
			}
			else if(a ==2){
				score[i] = go.computeFitness(ga.genetic(pop, gen, 100, graph, 3, go), graph);
			}
			else if(a == 3){
				score[i] = go.computeFitness(ma.memetic(pop, gen, 100, graph, 3, go), graph);
			}
			
			
		}
		for(int i = 0; i< numberOfRuns; i++){
			geneticSum = geneticSum + score[i];
		}
		
		return score;
		
		
	}
	
	/**
	 * Calculates median average
	 * @param scores the list of scores from the runs
	 * @return medianValue the resulting median 
	 */
	public static double median(int[] scores)	{
		double medianValue =0;
		int length = scores.length;
		 if (length%2 == 0) {
             double index = (length / 2.0) - 1;
             double nextIndex = index + 1;
             medianValue = (scores[(int) index] + scores[(int)nextIndex] / 2.0);
         } else {
             int index = (int) Math.floor((double) length / 2.0);
             medianValue = scores[index];
         }
		return medianValue;
		
	}
	
	/**
	 * Calculates the mean
	 * @param scores list of scores
	 * @return sum/length the mean
	 */
	public static double mean(int[] scores){
		int sum = 0;
		int length = scores.length;
		for(int i = 0; i< length; i++ ){
			sum = sum + scores[i];
		}
		return sum/length;
	}
	
	/**
	 * Contains the experiment set up
	 * @param args
	 */
	public static void main(String[] args){
		TabuSearch tb = new TabuSearch();
		GeneticAlg ga = new GeneticAlg();
		MemeticAlg ma = new MemeticAlg();
		GraphGenerate graph = new GraphGenerate(20);
		graph.setGraph(graph.getGraph());
		GeneticOperators go = new GeneticOperators();
		Statistics stats = new Statistics();
		
		int[] tbScores;
		int[] gaScores;
		int[] maScores;
		int numberOfRuns = 20;
		int pop = 10;
		int gen = 10;
		
		//Each of the algorithm is measured for its execution time
		final long tbStart = System.currentTimeMillis();
		tbScores = stats.score(tb, ga, ma, graph, go, 1, numberOfRuns, pop, gen);
		final long tbEnd = System.currentTimeMillis();
		
		final long gaStart = System.currentTimeMillis();
		gaScores = stats.score(tb, ga, ma, graph, go, 2, numberOfRuns, pop, gen);
		final long gaEnd = System.currentTimeMillis();
		
		final long maStart = System.currentTimeMillis();
		maScores = stats.score(tb, ga, ma, graph, go, 3, numberOfRuns, pop, gen);
		final long maEnd = System.currentTimeMillis();
		
		final long tbTime = tbEnd - tbStart;
		final long gaTime = gaEnd - gaStart;
		final long maTime = maEnd - maStart;
		
		//Sorts the scores
		Arrays.sort(tbScores);
		Arrays.sort(gaScores);
		Arrays.sort(maScores);
		
		//Prints the median, mean, time and best value of the experiment
		System.out.println("      Tabu search | genetic | memetic ");
		System.out.println("Mean:   " + mean(tbScores) + "         " + mean(gaScores) + "     " + mean(maScores));
		System.out.println("Median: " + median(tbScores) + "         " + median(gaScores) + "      " + median(maScores));
		System.out.println("Time:   " + tbTime + "          " + gaTime + "        " + maTime);
		System.out.println("Best:   "  + tbScores[0] + "           " + gaScores[0] + "          " + maScores[0]);
		
		
		//ga.genetic(10, 10, 100, graph, 3, go);
		
	}
	
}

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class GeneticAlgTest {

	GeneticAlg ga = new GeneticAlg();
	GraphGenerate graph = new GraphGenerate(5);
	TabuSearch tb = new TabuSearch();
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		graph.setGraph(graph.getGraph());
		graph.printGraph(graph.getGraph());
		ArrayList<Point> current = new ArrayList<Point>();
		current.add(new Point(1,2));
		current.add(new Point(3,2));
		current.add(new Point(4,0));
		System.out.println(current);
		//tb.checkConnected(current, graph);
	}

}

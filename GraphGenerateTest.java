

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class GraphGenerateTest {
	GraphGenerate g =  new GraphGenerate(10);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void returnGraph() {
		int[][] graph  = g.getGraph();
		int[][] temp = new int [10][10];
		//assertEquals(graph, temp, 0);
		assertEquals(graph.length, temp.length);
	}
	
	@Test
	public void correctInitial(){
		g.setGraph(g.getGraph());
		
		ArrayList<Point> a = new ArrayList<Point>();
		ArrayList<Point> b = new ArrayList<Point>();

		a.add(new Point(1,2));
		//3 edges
		int size = 3;
		b = g.initial(g.getGraph(), a, 0, 2);
		assertEquals(b.size(), size);
		
		size = 5;
		b = g.initial(g.getGraph(), a, 0, 4);
		assertEquals(b.size(), size);
		
		size = 7;
		b = g.initial(g.getGraph(), a, 0, 6);
		assertEquals(b.size(), size);
	}
	
	

}

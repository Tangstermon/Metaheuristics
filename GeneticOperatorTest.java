import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneticOperatorTest {
	GeneticOperators go = new GeneticOperators();
	GraphGenerate g = new GraphGenerate(10);
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void crossoverTest() {
		
		ArrayList<Point> a  = new ArrayList<Point>();
		a.add(new Point(1,2));
		a.add(new Point(2,3));
		a.add(new Point(3,4));
		ArrayList<Point> b  = new ArrayList<Point>();
		b.add(new Point(5,2));
		b.add(new Point(6,3));
		b.add(new Point(2,3));
		
		//checks size of the resulting offspring
		ArrayList<ArrayList<Point>> axb = new ArrayList<ArrayList<Point>>();
		axb = go.crossover(a,b,3);
		assertEquals(axb.size(), 2);
		
		//Tests if one is longer than the other 
		//will it still produce correct size offspring
		b.add(new Point(1,2));
		axb = go.crossover(a, b, 3);
		System.out.println(axb);
		assertEquals(axb.size(),2);
		for(int i =0; i< axb.size(); i++){
			assertEquals(axb.get(i).size(), 3);
		}
	}
	
	@Test
	public void fixingTest(){
		g.setGraph(g.getGraph());
		ArrayList<Point> a  = new ArrayList<Point>();
		a.add(new Point(1,0));
		a.add(new Point(2,3));
		a.add(new Point(3,4));
		
		Assert.assertTrue(go.connected(go.fixSolution(a, g)));
		
		ArrayList<Point> b  = new ArrayList<Point>();
		b.add(new Point(2,0));
		b.add(new Point(2,0));
		b.add(new Point(3,0));
	//	if(go.checkDuplicate(sol))
		Assert.assertTrue(go.connected(go.fixSolution(b, g)));	
	}
	
	@Test
	public void mutateTest(){
		g.setGraph(g.getGraph());
		ArrayList<Point> a  = new ArrayList<Point>();
		a.add(new Point(1,0));
		a.add(new Point(2,3));
		a.add(new Point(3,4));
		//System.out.println(go.mutate(a, g, 1));
		assertEquals(a,go.mutate(a, g, 1 ));
	}
	
}

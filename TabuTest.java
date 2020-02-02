import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TabuTest {
	TabuSearch tb = new TabuSearch();
	@Before
	public void setUp() throws Exception {
	}
	
	//Tests if tabu moves are added
	@Test
	public void testTabuList() {
		HashMap<Point, Integer> hs = new HashMap<Point, Integer>();
		Point p = new Point(1,2);
		ArrayList<Point> temp = new ArrayList<Point>();
		temp.add(new Point(1,2));
		tb.addTabu(hs, p);
		
		for(Point k : hs.keySet()){
			boolean test = ComparePoint.compare(k, p);
			if(test){
				assertEquals(test, true);
			}
		}
	}
	
	//Tests the decrement function
	@Test
	public void testDecrement(){
		HashMap<Point, Integer> hs = new HashMap<Point, Integer>();
		Point p = new Point(1,2);
		tb.addTabu(hs, p);
		tb.decrement(hs);
		Iterator<Map.Entry<Point, Integer>> it = hs.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Point, Integer> entry = it.next();
			hs.put(entry.getKey(), entry.getValue()-1);		
		
	}
		for(Entry<Point, Integer> a: hs.entrySet()){
			System.out.println(a.getValue());
			int number = a.getValue();
			assertEquals(number, 2);
		}
	}
	//Tests if all the solutions are valid
	@Test
	public void connectivty(){
		GraphGenerate graph = new GraphGenerate(10);
		graph.setGraph(graph.getGraph());
		ArrayList<Point> outcome = new ArrayList<Point>();
		//Test 2 edge
		outcome = tb.TS(graph, 2, 20);
		Assert.assertTrue(graph.connected(outcome));
		
		outcome = tb.TS(graph, 3, 20);
		Assert.assertTrue(graph.connected(outcome));
		
		outcome = tb.TS(graph, 4, 20);
		Assert.assertTrue(graph.connected(outcome));
		for(int i = 0; i< 20; i++){
			outcome = tb.TS(graph, 3, 20);
			Assert.assertTrue(graph.connected(outcome));
		}
		
	}
}

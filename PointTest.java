


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PointTest {
	
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testPoint() {
		Point p = new Point(1,4);
		int x = (int) p.getX();
		int y = (int) p.getY();
		assertEquals(x, 1);
		assertEquals(y, 4);
	}
	
	@Test
	public void testCompare(){
		
		Point p1 = new Point(1,2);
		Point p2 = new Point(1,2);
		Point p3 = new Point(3,4);
		
		ComparePoint cp = new ComparePoint();
		assertEquals(cp.compare(p1, p2), true);
 	}

}

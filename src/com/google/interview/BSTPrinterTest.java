package com.google.interview;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BSTPrinterTest {
	static private Map<Node, String> dataset;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataset = new HashMap<Node, String>();
		dataset.put(new Node(1, new Node(-2, new Node(3), null), null), "3,-2,1,");
		dataset.put(new Node(8, new Node(5), new Node(20, new Node(7), new Node(10))), "5,8,7,20,10,");
		dataset.put(null, "");
		dataset.put(new Node(4, new Node(2, 1, 3), new Node(6, 5, 7)), "1,2,3,4,5,6,7,");
		dataset.put(new Node(4, null, new Node(6, 5, 7)), "4,5,6,7,");
		dataset.put(
				new Node(1, null,
						new Node(2, null,
								new Node(3, null,
										new Node(4, new Node(5, new Node(6, new Node(7), null), null), null)))),
				"1,2,3,7,6,5,4,");
	}

	// (1,x,(2,x,(3,x,(4,(5,(6,(7),x),x),x))))

	@Test
	public void testWithRecursion() {
		for (Node node : dataset.keySet()) {
			Assert.assertEquals(dataset.get(node), BSTPrinter.printWithRecursion(node));
		}
	}

	@Test
	public void testWithStack() {
		for (Node node : dataset.keySet()) {
			Assert.assertEquals(dataset.get(node), BSTPrinter.printWithStack(node));
		}
	}

}

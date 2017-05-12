package com.vrsn.interview;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReverseTest {

	static private String[][] data = { { "hello", "olleh" }, { "1234", "4321" }, { " 12", "21 " }, { "x", "x" },
			{ "", "" }, { "if and when", "nehw dna fi" } };

	@Test
	public void test() {
		for (String[] test : data) {
			test(test[0], test[1]);
		}
	}

	public void test(String input, String expected) {
		assertEquals(expected, Reverse.execute(input));
		System.out.format("[%s] <=> [%s]%n", expected, Reverse.execute(input));
	}

}
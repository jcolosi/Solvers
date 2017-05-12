package com.vrsn.interview;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class NumericTest {

	@Test
	public void testAlpha() {
		Assert.assertTrue(Numeric.alpha(0) == 0);
		Assert.assertTrue(Numeric.alpha(1) == 1);
		Assert.assertTrue(Numeric.alpha(2) == 2);
		Assert.assertTrue(Numeric.alpha(3) == 2);
		Assert.assertTrue(Numeric.alpha(4) == 4);
		Assert.assertTrue(Numeric.alpha(5) == 4);
		Assert.assertTrue(Numeric.alpha(6) == 4);
		Assert.assertTrue(Numeric.alpha(7) == 4);
		Assert.assertTrue(Numeric.alpha(8) == 8);
		Assert.assertTrue(Numeric.alpha(9) == 8);
		Assert.assertTrue(Numeric.alpha(10) == 8);
		Assert.assertTrue(Numeric.alpha(20) == 16);
		Assert.assertTrue(Numeric.alpha(40) == 32);
		Assert.assertTrue(Numeric.alpha(100) == 64);
		Assert.assertTrue(Numeric.alpha(40000) == 32768);
		Assert.assertTrue(Numeric.alpha(65534) == 32768);
		Assert.assertTrue(Numeric.alpha(65535) == 32768);
		Assert.assertTrue(Numeric.alpha(65536) == 65536);
		Assert.assertTrue(Numeric.alpha(65537) == 65536);
		Assert.assertTrue(Numeric.alpha(-1) == 0);
	}

	@Test
	public void testBeta() {
		Assert.assertTrue(Arrays.equals(Numeric.beta(new int[] { 3, 7, 3 }), new int[] { 3, 3, 7 }));
		Assert.assertTrue(Arrays.equals(Numeric.beta(new int[] { 9, 6, 1, 3, 4 }), new int[] { 1, 3, 4, 6, 9 }));
	}
}

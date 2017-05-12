package com.vrsn.interview;

public class Numeric {

	static int alpha(int x) {
		x = x | (x >> 1);
		x = x | (x >> 2);
		x = x | (x >> 4);
		x = x | (x >> 8);
		x = x | (x >> 16);
		return x - (x >> 1);
	}

	static int[] beta(int[] array) {
		int i, j, tmp;
		int length = array.length;

		for (i = 0; i < length; i++) {
			tmp = array[i];
			j = i - 1;
			while (j >= 0 && array[j] > tmp) {
				array[j + 1] = array[j];
				j = j - 1;
			}
			array[j + 1] = tmp;
		}
		return array;
	}

}

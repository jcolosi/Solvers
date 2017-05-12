package com.vrsn.interview;

public class Reverse {

	/**
	 * ??? TODO: Cleanup/document this code
	 * 
	 * @param input
	 * @return
	 */
	static public String execute(final String input) {
		char[] working = input.toCharArray();

		int len = working.length;
		int mid = working.length / 2;

		for (int i = 0; i < len / 2; i++) {
			char tmp = working[i];
			working[i] = working[len - i - 1];
			working[len - i - 1] = tmp;
		}

		char[] output = new char[len];
		System.arraycopy(working, 0, output, 0, len);

		return new String(output);
	}

	static public String executeGood(String input) {
		char[] working = input.toCharArray();

		int len = working.length;
		int mid = len / 2;

		for (int i = 0; i < mid; i++) {
			char tmp = working[i];
			working[i] = working[len - i - 1];
			working[len - i - 1] = tmp;
		}

		return new String(working);
	}

	static public String simple(String input) {
		StringBuilder output = new StringBuilder(input).reverse();
		return output.toString();
	}

}

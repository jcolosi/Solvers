package com.vrsn.interview;

/**
 * Given the input string arrays, print out to the console the sentence, in the
 * order given by the index value of each word.
 *
 * Each array element is of the format “<index>:<word>”.
 *
 * The output should read:<br>
 * “the quick brown fox jumps over the lazy dog this morning”
 */
public class QuickBrownFox {

	static final String[] part1 = { "   9: dog    ", "  2  :quick   ", "4:       fox", " 7:  the   ", "5:  jumped" };
	static final String[] part2 = { "3  :brown", " 1 :  the", " 6:over", "8   :lazy", "10:  this", "  11  :  morning" };
	static String[] sentence;

	public static void addArray(String[] part) {
		for (String item : part) {
			String[] tuple = item.split(":");
			int index = Integer.parseInt(tuple[0].trim());
			String word = tuple[1].trim();
			sentence[index - 1] = word;
		}
	}

	public static void main(String[] args) {
		sentence = new String[part1.length + part2.length];

		addArray(part1);
		addArray(part2);

		StringBuilder build = new StringBuilder();
		for (String word : sentence) {
			build.append(" " + word);
		}

		String output = build.toString().substring(1);
		System.out.println(output);
	}

}
package jjc.research;

import java.util.Map;
import java.util.TreeMap;

/**
 * A fair die bearing the numbers 1, 2, 3, 4, 5, 6 is repeatedly thrown until
 * the running total first exceeds 12. What’s the most likely total that will be
 * obtained?
 * 
 * @author jcolosi
 */
public class DiceGame {

	private Map<Integer, Integer> map;
	private int max;

	public DiceGame(int max) {
		this.map = new TreeMap<Integer, Integer>();
		this.max = max;
	}

	public void roll() {
		roll("", 0);
	}

	public void roll(String history, int total) {
		if (total >= max)
			tally(total);
		else {
			for (int i = 1; i <= 6; i++) {
				roll(history + "," + i, total + i);
			}
		}
	}

	public void tally(int total) {
		if (map.containsKey(total)) {
			int count = map.get(total);
			map.put(total, count + 1);
		} else {
			map.put(total, 1);
		}
	}

	public String toString() {
		StringBuffer out = new StringBuffer();
		for (int total : map.keySet()) {
			out.append(total + " : " + map.get(total) + "\n");
		}
		return out.toString();
	}

	static public void main(String[] args) {
		DiceGame dg = new DiceGame(13);
		dg.roll();
		System.out.println(dg);
	}

}

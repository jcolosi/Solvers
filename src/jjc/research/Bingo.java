package jjc.research;

/**
 * What is the probability of a winning Bingo Card given the likelihood of a hit
 * on a cell
 * 
 * @author jcolosi
 */

public class Bingo {
	static final int TRIALS = 1000000;

	public static void main(String[] args) {
		// debugAll(new BingoCard(6));
		// debugWin(new ChainCard(6, 4));
		// testProbabilities(new BingoCard(6), 0.01, .60, TRIALS);
		testProbabilities(new ChainCard(6, 4), 0.01, .60, TRIALS);
		// debugPatterns(new ChainCard(6, 4));
	}

	static public void debugPattern(Card card, String pattern) {
		card.fill(pattern);
		if (card.isWinner()) System.out.println(card.toString());
	}

	static public void debugPatterns(Card card) {
		debugPattern(card, "............X......X......X......X..");
		debugPattern(card, "......X......X......X......X........");
		debugPattern(card, ".............X......X......X......X.");
		debugPattern(card, ".X......X......X......X.............");
		debugPattern(card, "........X......X......X......X......");
		debugPattern(card, "..X......X......X......X............");

		debugPattern(card, "...X....X....X....X.................");
		debugPattern(card, ".........X....X....X....X...........");
		debugPattern(card, "....X....X....X....X................");
		debugPattern(card, "................X....X....X....X....");
		debugPattern(card, "...........X....X....X....X.........");
		debugPattern(card, ".................X....X....X....X...");
	}

	static public void debugWin(Card card) {
		for (int i = 0; i < 40; i++) {
			card.fill(.11);
			if (card.isWinner()) System.out.println(card.toString());
		}
	}

	static public void debugAll(Card card) {
		for (int i = 0; i < 100; i++) {
			card.fill(.5);
			System.out.println(card.toString());
		}
	}

	static public double testProbability(Card card, double p, int trials) {
		int count = 0;
		for (int i = 0; i < trials; i++) {
			card.fill(p);
			if (card.isWinner()) count++;
		}

		return ((double) count) / trials;
	}

	static public void testProbabilities(Card card, double pMin, double pMax, int trials) {
		for (double p = pMin; p <= pMax; p += .01) {
			double out = testProbability(card, p, TRIALS);
			System.out.format("%f\t%f\n", p, out);
		}
	}
}

abstract class Card {
	int size;
	int length;
	boolean[] data;

	void fill(double probabilityOfHit) {
		for (int i = 0; i < length; i++) {
			data[i] = Math.random() < probabilityOfHit;
		}
	}

	void fill(String input) {
		for (int i = 0; i < length; i++) {
			data[i] = input.charAt(i) == 'X';
		}
	}

	abstract boolean isWinner();

	public String toString() {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i % size == 0) out.append("\n");
			out.append(data[i] ? "X" : "O");
		}
		out.append("\n" + (isWinner() ? "Win" : ""));
		return out.toString();
	}

	public boolean get(int row, int col) {
		return data[(row * size) + col];
	}
}

class BingoCard extends Card {

	public BingoCard(int size) {
		this.size = size;
		this.length = size * size;
		data = new boolean[length];
	}

	boolean isWinner() {
		boolean win;

		// Rows
		for (int row = 0; row < size; row++) {
			win = true;
			for (int col = 0; col < size; col++) {
				win &= data[(row * size) + col];
			}
			if (win) return true;
		}

		// Cols
		for (int col = 0; col < size; col++) {
			win = true;
			for (int row = 0; row < size; row++) {
				win &= data[(row * size) + col];
			}
			if (win) return true;
		}

		// Diags
		win = true;
		for (int x = 0; x < size; x++) {
			win &= data[(x * size) + x];
		}
		if (win) return true;

		for (int x = 0; x < size; x++) {
			win &= data[(length - 1) - ((x * size) + x)];
		}
		if (win) return true;

		return false;
	}

}

class ChainCard extends Card {
	int mark;

	public ChainCard(int size, int mark) {
		this.size = size;
		this.length = size * size;
		this.mark = mark;
		data = new boolean[length];
	}

	boolean isWinnerWrong() {
		int count;

		// Rows
		for (int row = 0; row < size; row++) {
			count = 0;
			for (int col = 0; col < size; col++) {
				count = data[(row * size) + col] ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		// Cols
		for (int col = 0; col < size; col++) {
			count = 0;
			for (int row = 0; row < size; row++) {
				count = data[(row * size) + col] ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		// Diags
		count = 0;
		for (int x = 0; x < size; x++) {
			count = data[(x * size) + x] ? count + 1 : 0;
			if (count >= mark) return true;
		}

		// count = 0;
		for (int x = 0; x < size; x++) {
			count = data[(length - 1) - ((x * size) + x)] ? count + 1 : 0;
			if (count >= mark) return true;
		}
		return false;
	}

	boolean isWinner() {
		int count;

		// Rows
		for (int row = 0; row < size; row++) {
			count = 0;
			for (int col = 0; col < size; col++) {
				count = data[(row * size) + col] ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		// Cols
		for (int col = 0; col < size; col++) {
			count = 0;
			for (int row = 0; row < size; row++) {
				count = data[(row * size) + col] ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		// Diags \ (down)
		for (int row = 0; row <= (size - mark); row++) {
			count = 0;
			for (int x = 0; x < size - row; x++) {
				count = get(row + x, x) ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		for (int col = 1; col <= (size - mark); col++) {
			count = 0;
			for (int x = 0; x < size - col; x++) {
				count = get(x, col + x) ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		// Diags / (up)
		for (int row = mark - 1; row < size; row++) {
			count = 0;
			for (int x = 0; x <= row; x++) {
				count = get(row - x, x) ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		for (int col = 1; col <= (size - mark); col++) {
			count = 0;
			for (int x = 0; x < size - col; x++) {
				count = get(size - 1 - x, col + x) ? count + 1 : 0;
				if (count >= mark) return true;
			}
		}

		return false;
	}
}

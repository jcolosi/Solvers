package jjc.research;

import java.util.Random;

import org.junit.Test;

/**
 * Two envelopes contain unequal sums of money (for simplicity, assume the two
 * amounts are positive integers). The probability distributions are unknown.
 * You choose an envelope at random, open it, and see that it contains x
 * dollars. Now you must predict whether the total in the other envelope is more
 * or less than x.
 * 
 * Since we know nothing about the other envelope, it would seem we have a 50
 * percent chance of guessing correctly. But, El Camino College mathematician
 * Leonard Wapner writes, “Unexpectedly, there is something you can do, short of
 * opening the other envelope, to give yourself a better than even chance of
 * getting it right.”
 * 
 * Choose a random positive integer, d, by any means at all. (If d = x then
 * choose again until this isn’t the case.) Now if d > x, guess more, and if d <
 * x, guess less. You’ll guess correctly more than 50 percent of the time.
 * 
 * How is this possible? The random number is chosen independently of the
 * envelopes. How can it point in the direction of the unknown y most of the
 * time? “Think of it this way,” writes Wapner. “If d falls between x and y then
 * your prediction (as indicated by d) is guaranteed to be correct. Assume this
 * occurs with probability p. If d falls less than both x and y, then your
 * prediction will be correct only in the event your chosen number x is the
 * larger of the two. There is a 50 percent chance of this. Similarly, if d is
 * greater than both numbers, your prediction will be correct only if your
 * chosen number is the smaller of the two. This occurs with a 50 percent
 * probability as well.”
 * 
 * On balance, your overall probability of being correct is (1/2) + (p/2)
 * 
 * That’s greater than 1/2, so the odds are in favor of your making a correct
 * prediction.
 * 
 * This example is based on a principle identified by Stanford statistician
 * David Blackwell. “It’s unexpected and ironic that an unrelated random
 * variable can be used to predict that which appears to be completely
 * unpredictable.”
 * 
 * -----
 * 
 * The program below, incredibly, upholds the conjecture. You can pick a random
 * number and do better at guessing. It's really a trick of the case where your
 * guess number is between a and y. You always win in that scenario. So if
 * you're trying to guess a number in the middle of the range, you'll have less
 * advantage. But if you're trying to guess a number at the edge of the range
 * you'll have a great advantage. I implemented with a handful of data types
 * because at first I thought the size of the range mattered. But I think it
 * really only matters where you fall in that range. (x=5 on a range to 10
 * should be equivalent to x=5,000 on a range to 10,000)
 * 
 * Guessing at 189278238<br>
 * Guess Rate: 0.499951<br>
 * Trick Rate: 0.503831<br>
 * 
 * Guessing at 0.744720<br>
 * Guess Rate: 0.500012<br>
 * Trick Rate: 0.619880<br>
 * 
 * Guessing at 7<br>
 * Guess Rate: 0.495098<br>
 * Trick Rate: 0.852009<br>
 * 
 * This is really kind of an incredible result. I'm guessing at something that
 * seems random, but I can do better than average. It's like finding out that
 * there's a trick to guessing a coin flip. This is indeed a disturbing
 * universe.
 * 
 * People are paid to make guesses. How can you monetize this?
 * 
 * @author jcolosi
 *
 */
public class BlackwellsBetTest {

	static private final int ITERATIONS = 100000000;
	static private final Random rand = new Random();

	@Test
	public void testDouble() {
		int guessSuccess = 0;
		int trickSuccess = 0;
		double x = rand.nextDouble();
		System.out.format("\nGuessing at %f\n", x);
		for (int i = 0; i < ITERATIONS; i++) {
			if (testGuess(x)) guessSuccess++;
			if (testTrick(x)) trickSuccess++;
		}

		double guessRate = ((double) guessSuccess) / ITERATIONS;
		System.out.format("Guess Rate: %f\n", guessRate);
		double trickRate = ((double) trickSuccess) / ITERATIONS;
		System.out.format("Trick Rate: %f\n", trickRate);
	}

	@Test
	public void testInt() {
		int guessSuccess = 0;
		int trickSuccess = 0;
		int x = rand.nextInt();
		System.out.format("\nGuessing at %d\n", x);
		for (int i = 0; i < ITERATIONS; i++) {
			if (testGuess(x)) guessSuccess++;
			if (testTrick(x)) trickSuccess++;
		}

		double guessRate = ((double) guessSuccess) / ITERATIONS;
		System.out.format("Guess Rate: %f\n", guessRate);
		double trickRate = ((double) trickSuccess) / ITERATIONS;
		System.out.format("Trick Rate: %f\n", trickRate);
	}

	@Test
	public void testIntConstrained() {
		testIntConstrained(1000);
		testIntConstrained(1000000);
		testIntConstrained(1000000000);
	}

	private void testIntConstrained(int max) {
		int guessSuccess = 0;
		int trickSuccess = 0;
		int x = rand.nextInt(max);
		System.out.format("\nGuessing at %d (out of %d)\n", x, max);
		for (int i = 0; i < ITERATIONS; i++) {
			if (testGuess(x, max)) guessSuccess++;
			if (testTrick(x, max)) trickSuccess++;
		}

		double guessRate = ((double) guessSuccess) / ITERATIONS;
		System.out.format("Guess Rate: %f\n", guessRate);
		double trickRate = ((double) trickSuccess) / ITERATIONS;
		System.out.format("Trick Rate: %f\n", trickRate);
	}

	private boolean testGuess(double x) {
		boolean guessUp = rand.nextBoolean();
		double y = rand.nextDouble();
		return (y > x && guessUp) || (y < x && !guessUp);
	}

	private boolean testTrick(double x) {
		double q = rand.nextDouble();
		boolean guessUp = (q > x);
		double y = rand.nextDouble();
		return (y > x && guessUp) || (y < x && !guessUp);
	}

	private boolean testGuess(int x) {
		boolean guessUp = rand.nextBoolean();
		double y = rand.nextInt();
		return (y > x && guessUp) || (y < x && !guessUp);
	}

	private boolean testTrick(int x) {
		double q = rand.nextInt();
		boolean guessUp = (q > x);
		double y = rand.nextInt();
		return (y > x && guessUp) || (y < x && !guessUp);
	}

	private boolean testGuess(int x, int constraint) {
		boolean guessUp = rand.nextBoolean();
		double y = rand.nextInt(constraint);
		return (y > x && guessUp) || (y < x && !guessUp);
	}

	private boolean testTrick(int x, int constraint) {
		double q = rand.nextInt(constraint);
		boolean guessUp = (q > x);
		double y = rand.nextInt(constraint);
		return (y > x && guessUp) || (y < x && !guessUp);
	}

}

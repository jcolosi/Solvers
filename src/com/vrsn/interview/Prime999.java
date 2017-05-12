package com.vrsn.interview;

public class Prime999 {
	static public void main(String[] args) {
		Prime999 prime = new Prime999();
		prime.execute();
		// for (int i = 0; i < 1000; i++) {
		// System.out.format("%d: %s%n", i, prime.isPrime(i));
		// }
	}

	public void execute() {
		for (int i = 0; i < 500; i++) {
			if (isPrime(i) && isPrime(999 - i)) {
				System.out.format("%d + %d = 999%n", i, (999 - i));
			}
		}
	}

	Boolean[] primes = new Boolean[1000];

	boolean isPrime(int x) {
		if (primes[x] == null) {
			int stop = x / 2;
			for (int i = 2; i < stop; i++) {
				int q = x / i;
				if (x == (q * i)) return (primes[x] = false);
			}
			return (primes[x] = true);
		} else {
			return primes[x];
		}
	}
}
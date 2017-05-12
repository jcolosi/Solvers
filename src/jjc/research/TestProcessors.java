package jjc.research;

import java.util.Arrays;

/**
 * I'm trying to see if I can make use of a 6 core processor. This code shows
 * that the work of even 1 thread is spread across processors. More threads
 * taxes the processors more, and with 10 threads I can get 100% CPU load on all
 * processors. I'm not sure what this shows. I think Eclipse can effectively
 * task multiple processors and it's safe to consider the full computational
 * power of a CPU instead of focusing on single-core computation.
 * 
 * @author jcolosi
 *
 */
public class TestProcessors {

	static private final int WORKERS = 8;

	public static void main(String[] args) {
		for (int i = 0; i < WORKERS; i++) {
			new Thread(new Worker()).start();
		}
	}
}

class Worker implements Runnable {

	static private final int SIZE = 10000000;
	static private int ID = 0;

	private final double[] data = new double[SIZE];

	private synchronized int getId() {
		ID++;
		System.out.format("Provisioning worker %d\n", ID);
		return ID;
	}

	private final int id = getId();

	private void reset() {
		for (int i = 0; i < SIZE; i++) {
			data[i] = Math.random();
		}
	}

	private void work() {
		Arrays.sort(data);
	}

	@Override
	public void run() {
		long time; // yes
		while (true) {
			// Do reset
			time = System.currentTimeMillis();
			reset();
			System.out.format("%d: reset complete [%d]\n", id, System.currentTimeMillis() - time);

			// Do work
			time = System.currentTimeMillis();
			work();
			System.out.format("%d: work complete [%d]\n", id, System.currentTimeMillis() - time);
		}
	}
}
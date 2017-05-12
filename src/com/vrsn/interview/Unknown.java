package com.vrsn.interview;

import java.util.HashMap;
import java.util.Map;

public class Unknown {

	static public void main(String[] args) {
		Unknown u = new Unknown();
		for (int i = 0; i < 20; i++) {
			System.out.format("[%d] = %d%n", i, u.execute(i));
		}
	}

	private Map<Integer, Long> map;

	public Unknown() {
		this.map = new HashMap<Integer, Long>();
	}

	public long execute(int index) {
		if (index < 2) return 1;
		else if (map.containsKey(index)) {
			return map.get(index);
		} else {
			long value = execute(index - 1) + execute(index - 2);
			map.put(index, value);
			return value;
		}
	}

}
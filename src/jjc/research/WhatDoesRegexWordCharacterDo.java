package jjc.research;

import java.util.ArrayList;
import java.util.List;

public class WhatDoesRegexWordCharacterDo {

	static public void main(String[] args) {
		String s;
		List<Character> list = new ArrayList<Character>();
		char c = 0x0000;
		while (true) {
			s = new String("" + c);
			if (s.matches("\\w")) list.add(c);
			if (++c == 0x0000) break;
		}
		for (char cx : list) {
			System.out.format("%4x %s%n", (int) cx, cx);
		}
		for (char cx : list) {
			System.out.format("%s", cx);
		}
	}

	static public void log(String operation, char c, char cPrime) {
		System.out.format("%s(U+%04x) -> %s%n", operation, (int) c, "" + cPrime);
	}

	static public boolean isDnsCompatible(char aChar) {
		return aChar == 0x002D || aChar >= 0x0030 && aChar <= 0x0039 || aChar >= 0x0041 && aChar <= 0x005A
				|| aChar >= 0x0061 && aChar <= 0x007A;
	}

}
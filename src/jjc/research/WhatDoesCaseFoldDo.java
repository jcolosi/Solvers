package jjc.research;

public class WhatDoesCaseFoldDo {

	static public void main(String[] args) {
		char c = 0x0000;
		char cPrime;
		while (true) {
			if (!isDnsCompatible(c)) {
				cPrime = Character.toUpperCase(c);
				if (isDnsCompatible(cPrime)) log("toUpperCase", c, cPrime);

				cPrime = Character.toLowerCase(c);
				if (isDnsCompatible(cPrime)) log("toLowerCase", c, cPrime);
			}
			if (++c == 0x0000) break;
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
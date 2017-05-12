package com.vrsn.interview;

import java.awt.Point;

public class PointShift {

	static public void main(String[] args) {

		// Initialize two parameters
		Point myPoint = new Point(2, 3);
		int myInt = 4;

		// Pass these parameters to a method
		change(myPoint, myInt);

		// Determine the values in the calling program
		System.out.println("What is my point?  " + myPoint);
		System.out.println("What is my int?  " + myInt);
	}

	static void change(Point aObject, int aPrimitive) {
		aObject.x /= 2;
		aObject = new Point(1, 4);
		aObject.y /= 2;

		aPrimitive++;
		aPrimitive = 3;
		aPrimitive--;
	}

}
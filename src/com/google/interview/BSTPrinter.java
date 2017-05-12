package com.google.interview;

import java.util.Stack;

/**
 * Traverse a BST and print nodes left-to-right
 * 
 * @author jcolosi
 */
public class BSTPrinter {

	/**
	 * Use recursion to leverage the execution stack as a data structure.
	 * 
	 * @param node
	 *            - The root of the tree to be traversed.
	 * @return - A String containing the node values in the correct order.
	 */
	static public String printWithRecursion(Node node) {
		if (node == null) return "";
		return printWithRecursion(node.left) + node.val + "," + printWithRecursion(node.right);
	}

	/**
	 * Leverage a local Stack variable to track Nodes to be processed instead of
	 * recursion.
	 * <p>
	 * NOTE: In order to accomplish the goal, I'm including the current Node
	 * again as a leaf node in the final else branch! This isn't ideal, but this
	 * is the nature of the solution I was pursuing on the board, so I stuck
	 * with it. Here I'm using the Stack to store Nodes to process. It might be
	 * more efficient to store Nodes to return to as this is how the execution
	 * stack works. Again, I'm only finishing what I started yesterday.
	 * 
	 * @param node
	 *            - The root of the tree to be traversed.
	 * @return - A String containing the node values in the correct order.
	 */
	static public String printWithStack(Node root) {
		Stack<Node> stack = new Stack<Node>();
		StringBuilder out = new StringBuilder();

		// Add the initial root
		stack.add(root);

		// Continue until all Nodes are processed.
		while (!stack.isEmpty()) {

			// Look at the top Node on the Stack
			Node node = stack.pop();

			// Continue if there's nothing to do
			if (node == null) continue;

			// If the node is a leaf, then print and continue.
			if (node.left == null && node.right == null) {
				out.append(node.val + ",");
			}

			// If the node has 1 or more children, then enter them on the stack.
			else {
				if (node.right != null) stack.add(node.right);
				stack.add(new Node(node.val)); // See NOTE above.
				if (node.left != null) stack.add(node.left);
			}
		}
		return out.toString();
	}

}

/**
 * A Node in a Binary Search Tree, along with some convenience constructors.
 * 
 * @author jcolosi
 */
class Node {
	Node left;
	int val;
	Node right;

	public Node(int val, Node left, Node right) {
		this.left = left;
		this.val = val;
		this.right = right;
	}

	public Node(int val) {
		this.left = null;
		this.val = val;
		this.right = null;
	}

	public Node(int val, int left, int right) {
		this.left = new Node(left);
		this.val = val;
		this.right = new Node(right);
	}
}

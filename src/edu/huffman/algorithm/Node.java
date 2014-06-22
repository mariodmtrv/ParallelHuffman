package edu.huffman.algorithm;


public class Node implements Comparable<Node> {
	private char character;
	private int frequency;
	private Node left;
	private Node right;
	public static final Character NOT_CHARACTER = '$';
	public static final Character OPEN_TREE = '{';
	public static final Character CLOSE_TREE = '}';
	public static final String EMPTY_NODE = OPEN_TREE.toString() + CLOSE_TREE;

	/**
	 * For node containing character
	 * */
	Node(char character, int frequency) {
		this.character = character;
		this.frequency = frequency;
		this.left = null;
		this.right = null;
	}

	/**
	 * For node joining subtrees
	 * */
	Node(int frequency, Node left, Node right) {
		this.character = NOT_CHARACTER;
		this.frequency = frequency;
		this.left = left;
		this.right = right;
	}

	/**
	 * General purpose constructor
	 * */
	Node(char character, int frequency, Node left, Node right) {
		this.character = character;
		this.frequency = frequency;
		this.left = left;
		this.right = right;
	}

	// / is the node a leaf node
	public boolean isLeaf() {
		assert (left == null && right == null)
				|| (left != null && right != null);
		return (left == null && right == null);
	}

	// / compare, based on frequency
	public int compareTo(Node other) {
		if (this.frequency == other.frequency) {

			return this.character - other.character;
		}
		return this.frequency - other.frequency;
	}

	public String toString() {
		return stringify(this);
	}

	public int getFrequency() {
		return this.frequency;
	}

	public Character getCharacter() {
		return this.character;
	}

	public Node getLeft() {
		return this.left;
	}

	public Node getRight() {
		return this.right;
	}

	private String stringify(Node node) {
		if (node == null)
			return EMPTY_NODE;
		StringBuilder sb = new StringBuilder();
		sb.append(OPEN_TREE);

		sb.append("[" + node.character + "," + node.frequency + "]");

		sb.append(stringify(node.left));

		sb.append("$");

		sb.append(stringify(node.right));

		sb.append(CLOSE_TREE);
		return sb.toString();
	}
}
package edu.huffman.algorithm;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Tree {
	private Node root;
	private HashMap<Character, String> characterCodeMap;

	public Tree() {
		this.root = null;
		this.characterCodeMap = null;
	}

	/**
	 * Use this to rebuild the Huffman tree from file
	 * 
	 * @param serializedTree
	 *            - a String representation of the tree
	 * */
	public void deserialize(String serializedTree) {
		root = deserializeTree(serializedTree);
	}

	public Node deserializeTree(String serializedTree) {

		Node node = new Node('\0', 0, null, null);
		return node;
	}

	private void generateCharacterCodeMap() {
		this.characterCodeMap = new HashMap<>();
		StringBuilder code = new StringBuilder();
		addCharCodeToTable(root, code);
	}

	private void addCharCodeToTable(Node node, StringBuilder characterCode) {

		if (node.isLeaf()) {
			characterCodeMap.put(node.getCharacter(), characterCode.toString());
			return;
		}
		Node leftTree = node.getLeft();
		Node rightTree = node.getRight();

		if (leftTree != null) {
			StringBuilder currentLeft = new StringBuilder(characterCode);
			currentLeft.append("0");
			addCharCodeToTable(leftTree, currentLeft);
		}
		if (rightTree != null) {
			StringBuilder currentRight = new StringBuilder(characterCode);
			currentRight.append("1");
			addCharCodeToTable(rightTree, currentRight);
		}
	}

	public String getCharCode(Character character) {
		if (characterCodeMap == null) {
			generateCharacterCodeMap();
		}
		if (characterCodeMap.containsKey(character)) {
			return characterCodeMap.get(character);
		}
		return null;
	}

	/**
	 * Use this to buid the Huffman tree from the frequency map of a file
	 * 
	 * @param frequencyMap
	 *            - the frequency of all characters, used to create the initial
	 *            nodes of the tree
	 * */
	public void buildTree(int[] frequencyMap) {
		PriorityQueue<Node> availableNodes = buildInitialTrees(frequencyMap);
		Node tree = joinAllTreeNodes(availableNodes);
		this.root = tree;
	}

	public String toString() {
		return root.toString();
	}

	/**
	 * @param frequencyMap
	 *            - the frequency of all characters, used to create the initial
	 *            nodes of the tree
	 * @return availableNodes - a priority queue containing single-node trees
	 * */
	private PriorityQueue<Node> buildInitialTrees(int[] frequencyMap) {
		PriorityQueue<Node> availableNodes = new PriorityQueue<>();
		for (int charIndex = 0; charIndex < frequencyMap.length; charIndex++) {
			if (frequencyMap[charIndex] > 0) {
				Node availableNode = new Node((char) charIndex,
						frequencyMap[charIndex]);
				availableNodes.add(availableNode);
			}
		}
		return availableNodes;
	}

	/**
	 * 
	 * The left node contains the bigger value The last node in the priorty
	 * queue holds the whole tree
	 * */
	private Node joinAllTreeNodes(PriorityQueue<Node> availableNodes) {
		while (availableNodes.size() > 1) {
			Node right = availableNodes.poll();
			Node left = availableNodes.poll();
			Node newRoot = new Node(left.getFrequency() + right.getFrequency(),
					left, right);
			availableNodes.add(newRoot);
		}
		return availableNodes.poll();
	}

}

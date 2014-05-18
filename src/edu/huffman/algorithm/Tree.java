package edu.huffman.algorithm;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tree {
	public static final Boolean leftDirection = false;
	public static final Boolean rightDirection = true;
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
		if (serializedTree.contentEquals(Node.EMPTY_NODE)) {
			return null;
		}
		// a tree starts with a letter and its occurrence count
		String regex = "^\\{\\[(.),(\\d+)\\]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(serializedTree);
		Character letter = null;
		Integer occurrenceCount = null;
		if (matcher.find()) {
			// only one character is guaranteed
			letter = matcher.group(1).charAt(0);
			occurrenceCount = Integer.parseInt(matcher.group(2));
		}
		int skip = 4 + matcher.group(1).length() + matcher.group(2).length();
		String leftTreeSerialized = getTree(serializedTree, skip,
				serializedTree.length() - 1);
		String rightTreeSerialized = getTree(serializedTree, skip
				+ leftTreeSerialized.length() + 1, serializedTree.length() - 1);

		Node leftTree = deserializeTree(leftTreeSerialized);
		Node rightTree = deserializeTree(rightTreeSerialized);

		Node node = new Node(letter, occurrenceCount, leftTree, rightTree);
		return node;
	}

	private String getTree(String string, int startPos, int maxEnd) {
		int currentPos = startPos;
		if (string.charAt(startPos) == Node.OPEN_TREE) {
			int bracketsCount = 1;
			while (bracketsCount > 0) {
				currentPos++;
				if (currentPos > maxEnd) {
					break;
				}
				if (string.charAt(currentPos) == Node.OPEN_TREE) {
					bracketsCount++;
				} else if (string.charAt(currentPos) == Node.CLOSE_TREE) {
					bracketsCount--;
				}

			}
			return (String) string.subSequence(startPos, currentPos + 1);
		}
		return null;
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
			currentLeft.append(Tree.leftDirection);
			addCharCodeToTable(leftTree, currentLeft);
		}
		if (rightTree != null) {
			StringBuilder currentRight = new StringBuilder(characterCode);
			currentRight.append(Tree.rightDirection);
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

	public Node getInnerTree() {
		return root;
	}
}

package edu.huffman;

import edu.huffman.algorithm.Node;
import edu.huffman.algorithm.Tree;

public class Decoder {
	private Node huffmanTree;
	private Node currentNode;

	public Decoder(Node huffmanTree) {
		this.huffmanTree = huffmanTree;
		this.currentNode = huffmanTree;
	}

	/**
	 * @return <code>null</code> if no character has been recognized yet.
	 *         Continue feeding. Character - if a character has been recognized
	 * */
	private Character digest(Character direction) {
		if (direction == Tree.leftDirection) {
			currentNode = currentNode.getLeft();
		} else if (direction == Tree.rightDirection) {
			currentNode = currentNode.getRight();
		}

		if (currentNode.isLeaf()) {
			// A character has been reached. Return it and go back to the
			// beginning
			Character decodedCharacter = currentNode.getCharacter();
			refresh();
			return decodedCharacter;
		}
		return null;

	}

	private void refresh() {
		this.currentNode = huffmanTree;
	}

	/**
	 * Decodes the given string back to normal
	 * */
	public String decode(String encodedString) {

		StringBuilder decodedString = new StringBuilder();
		for (Character character : encodedString.toCharArray()) {
			Character decoded = digest(character);
			if (decoded != null) {
				decodedString.append(decoded);
			}
		}
		return decodedString.toString();
	}
}

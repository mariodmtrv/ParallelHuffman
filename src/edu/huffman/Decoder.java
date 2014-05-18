package edu.huffman;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;

import edu.huffman.algorithm.Node;
import edu.huffman.algorithm.Tree;

public class Decoder implements Runnable {
	private Node huffmanTree;
	private Node currentNode;
	private String filePath;
	private BitSet dataSet;

	public Decoder(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return <code>null</code> if no character has been recognized yet.
	 *         Continue feeding. Character - if a character has been recognized
	 * */
	private Character digest(Boolean direction) {
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
	public String decode() {

		StringBuilder decodedString = new StringBuilder();

		for (int i = 0; i < dataSet.size(); i++) {

			Character decoded = digest(dataSet.get(i));
			if (decoded != null) {
				decodedString.append(decoded);
			}

		}
		return decodedString.toString();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		loadData();
		decode();
	}

	private void loadData() {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath
					+ ".tree.txt"));
			Tree tree = new Tree();
			tree.deserialize(reader.readLine());
			huffmanTree = tree.getInnerTree();
			this.currentNode = huffmanTree;
			File file = new File(filePath + ".data");
			byte[] fileData = new byte[(int) file.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			dis.close();
			dis.readFully(fileData);
			dataSet = BitSet.valueOf(fileData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

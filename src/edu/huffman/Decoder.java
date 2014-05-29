package edu.huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.logging.Logger;

import edu.huffman.algorithm.Node;
import edu.huffman.algorithm.Tree;

public class Decoder implements Runnable {
	private Node huffmanTree;
	private Node currentNode;
	private String filePath;
	private BitSet dataSet;
	private Integer partIndex;
	final static Logger logger = Logger.getLogger(Decoder.class.getName());

	public Decoder(String filePath, Integer partIndex) {
		this.filePath = filePath;
		this.partIndex = partIndex;
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
		logger.info(Thread.currentThread().getName() + " started decompressing.");
		StringBuilder decodedString = new StringBuilder();

		for (int i = 0; i < dataSet.size(); i++) {

			Character decoded = digest(dataSet.get(i));
			if (decoded != null) {
				decodedString.append(decoded);
			}

		}
		logger.info(Thread.currentThread().getName() + " finished decompressing.");
		return decodedString.toString();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		loadData();
		String decoded = decode();
		writeToFile(decoded);
	}

	private void loadData() {
		logger.info(Thread.currentThread().getName() + " started collecting tree and file data.");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					String.format(filePath + Huffman.treeFileExtension,
							this.partIndex)));
			Tree tree = new Tree();
			tree.deserialize(reader.readLine());
			huffmanTree = tree.getInnerTree();
			this.currentNode = huffmanTree;
			File file = new File(String.format(filePath
					+ Huffman.compressedFileExtension, this.partIndex));
			byte[] fileData = new byte[(int) file.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			dis.close();
			dis.readFully(fileData);
			dataSet = BitSet.valueOf(fileData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(Thread.currentThread().getName() + " finished collecting data.");

	}

	private void writeToFile(String decoded) {
		logger.info(Thread.currentThread().getName() + " will flush.");
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(
					String.format(filePath + Huffman.decompressedFileExtension,
							this.partIndex))));
			out.println(decoded); // output result
			logger.info(Thread.currentThread().getName() + " finished flushing.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}

package edu.huffman;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import edu.huffman.algorithm.Tree;

public class Encoder{
	private Tree huffmanTree;
	public Encoder() {
		this.huffmanTree = new Tree();
	}

	/**
	 * Builds a new Huffman tree
	 * 
	 * @return the encoded String
	 * */
	public String encode(ArrayList<String> data) {
		// builds the frequency map
		int[] frequencyMap = buildFrequencyMap(data);
		// builds the huffman tree
		huffmanTree.buildTree(frequencyMap);
		// compresses the data
		String compressed = compressData(data);
		return compressed;

	}

	public Tree getTree() {
		return huffmanTree;
	}

	private int[] addFrequenciesFromBuffer(char[] buffer, int[] frequencyMap) {
		for (char character : buffer) {
			frequencyMap[(int) character]++;
		}
		return frequencyMap;
	}

	private int[] buildFrequencyMap(ArrayList<String> data) {
		int[] frequencyMap = new int[Huffman.MAX_DIFFERENT_CHARACTERS];
		for (String buffer : data) {
			frequencyMap = addFrequenciesFromBuffer(buffer.toCharArray(),
					frequencyMap);
		}
		return frequencyMap;

	}

	private String compressData(ArrayList<String> data) {
		StringBuilder compressed = new StringBuilder();
		for (String buffer : data) {
			compressed.append(compressString(buffer));
		}
		return compressed.toString();
	}

	/**
	 * Produces the compressed text, given a huffman tree
	 * */
	private String compressString(String rawString) {
		StringBuilder encodedString = new StringBuilder();
		for (char character : rawString.toCharArray()) {
			encodedString.append(huffmanTree.getCharCode(character));
		}
		return encodedString.toString();
	}

}

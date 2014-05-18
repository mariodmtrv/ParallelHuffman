package edu.huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import edu.huffman.algorithm.Tree;

public class Encoder implements Runnable {
	private Tree huffmanTree;
	private ArrayList<String> rawData;
	private String encodedResult;
	private String resultFilepath;

	public Encoder(ArrayList<String> rawData, String resultFilepath) {
		this.rawData = rawData;
		this.huffmanTree = new Tree();
		this.resultFilepath = resultFilepath;
	}

	/**
	 * Builds a new Huffman tree
	 * 
	 * @return the encoded String
	 * */
	public String encode() {
		// builds the frequency map
		int[] frequencyMap = buildFrequencyMap(rawData);
		// builds the huffman tree
		huffmanTree.buildTree(frequencyMap);
		// compresses the data
		String compressed = compressData(rawData);
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

	@Override
	public void run() {
		encodedResult = encode();
		generateEncodedFileContent();
		flushToFile();

	}

	public String getEncodedResult() {
		return encodedResult;
	}

	private String generateEncodedFileContent() {

		StringBuilder result = new StringBuilder();
		String encodedData = getEncodedResult();
		String tree = getTree().toString();
		result.append(tree);
		result.append('\n');
		result.append(encodedData);
		return result.toString();
	}

	private void flushToFile() {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(resultFilepath)));
			out.println(generateEncodedFileContent()); // output result
			out.close();
		} catch (IOException e) {
			System.err.println("Thread failed to flush to file");
		}

	}

}

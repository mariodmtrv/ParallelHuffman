package edu.huffman;

import edu.huffman.algorithm.Tree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Encoder implements Runnable {
	final static Logger logger = Logger
			.getLogger(Encoder.class.getName());
	private Tree huffmanTree;
	private ArrayList<String> rawData;
	private String resultFilepath;
	private Integer partIndex;
	private static volatile long frequencyTableConstructionTime = 0;

	public Encoder(ArrayList<String> rawData, String resultFilepath,
			Integer partIndex) {

		this.rawData = rawData;
		this.huffmanTree = new Tree();
		this.resultFilepath = resultFilepath;
		this.partIndex = partIndex;
	}

	/**
	 * Builds a new Huffman tree
	 * 
	 * @return the encoded String
	 * */
	public String encode() {
		logger.info(Thread.currentThread().getName()
				+ " started generating tree data");

		// builds the frequency map
		long startTime = System.currentTimeMillis();
		int[] frequencyMap = buildFrequencyMap(rawData);
		// builds the huffman tree
		huffmanTree.buildTree(frequencyMap);
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		frequencyTableConstructionTime += executionTime;
		// compresses the data
		String compressed = compressData();
		
		logger.info(Thread.currentThread().getName() + " generated tree");
		// raw data is not needed anymore
		rawData.clear();
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
		int[] frequencyMap = new int[HuffmanInterface.MAX_DIFFERENT_CHARACTERS];
		for (String buffer : data) {
			frequencyMap = addFrequenciesFromBuffer(buffer.toCharArray(),
					frequencyMap);
		}
		return frequencyMap;

	}

	private String compressData() {
		logger.info(Thread.currentThread().getName() + " started compressing.");
		StringBuilder compressed = new StringBuilder();
		for (String buffer : rawData) {
			compressed.append(compressString(buffer));
		}
		logger.info(Thread.currentThread().getName() + " completed compression");
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
		String encodedResult = encode();
		logger.info(Thread.currentThread().getName() + " will flush");
		flushContentsToFile(encodedResult);

	}

	private String generateFileTree() {

		StringBuilder result = new StringBuilder();
		String tree = getTree().toString();
		result.append(tree);
		result.append('\n');
		return result.toString();
	}

	private void flushContentsToFile(String encodedResult) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(String.format(resultFilepath
							+ HuffmanInterface.treeFileExtension,
							this.partIndex))));
			out.println(generateFileTree()); // output result
			out.close();

			FileOutputStream fos = new FileOutputStream(new File(String.format(
					resultFilepath + HuffmanInterface.compressedFileExtension,
					this.partIndex)));
			ByteArrayOutputStream binaryOutputStream = new ByteArrayOutputStream();
			byte[] fileData = ByteConverter.toByteArray(encodedResult);
			binaryOutputStream.write(fileData);
			// Put data
			binaryOutputStream.writeTo(fos);
			fos.close();
		} catch (IOException e) {
			System.err.println("Thread failed to flush to file");
		}
		logger.info(Thread.currentThread().getName() + "finished flushing");
	}

	public static long getFrequencyTableConstructionTime() {
		return frequencyTableConstructionTime;
	}
}

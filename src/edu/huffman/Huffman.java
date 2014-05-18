package edu.huffman;

import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.text.ParseException;

import edu.huffman.algorithm.Tree;

public class Huffman {
	/**
	 * The maximum number of threads that the program may use
	 * */
	Integer maxTasksCount;
	/**
	 * Path to the file that will be compressed
	 * */
	String filePath;
	/**
	 * Indicates quiet mode
	 * */
	Boolean isQuiet;
	static final Integer MAX_DIFFERENT_CHARACTERS = 256;
/**
 * TODO : Set Buffer_size to 64
 * */
	static final Integer BUFFER_SIZE = 4;
	/**
	 * The huffman tree of the algorithm
	 * */
	Tree huffmanTree;

	public Huffman(String filepath, Integer maxTasksCount, Boolean isQuiet) {
		this.filePath = filepath;
		this.maxTasksCount = maxTasksCount;
		this.isQuiet = isQuiet;
	}

	public void encode() {

	}

	public void decode() {

	}
	/*
	 * public static void main(String[] args) throws Exception { String[] args1
	 * = { "-f", "hello.bat", "-t", "535" }; filePath = getFilePath(args1);
	 * maxTasksCount = getMaxTasksCount(args1); isQuiet = getIsQuiet(args1); }
	 */

}

package edu.huffman;

import edu.huffman.algorithm.Node;
import  edu.huffman.algorithm.Tree;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.BitSet;
import java.util.logging.Logger;

public class Decoder implements Runnable{
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
					String.format(filePath + HuffmanInterface.treeFileExtension,
							this.partIndex)));
			Tree tree = new Tree();
			tree.deserialize(reader.readLine());
			huffmanTree = tree.getInnerTree();
			this.currentNode = huffmanTree;
			File file = new File(String.format(filePath
					+ HuffmanInterface.compressedFileExtension, this.partIndex));
		/*	byte[] fileData = new byte[(int) file.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			
			dis.readFully(fileData);
			dis.close();*/
			String fileName = String.format(filePath
					+ HuffmanInterface.compressedFileExtension, this.partIndex);
			byte[] fileData = read(fileName);
			dataSet = ByteConverter.fromByteArray(fileData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(Thread.currentThread().getName() + " finished collecting data.");

	}
	/** Read the given binary file, and return its contents as a byte array.*/ 
	  byte[] read(String aInputFileName){
	   
	    File file = new File(aInputFileName);
	   // log("File size: " + file.length());
	    byte[] result = new byte[(int)file.length()];
	    try {
	      InputStream input = null;
	      try {
	        int totalBytesRead = 0;
	        input = new BufferedInputStream(new FileInputStream(file));
	        while(totalBytesRead < result.length){
	          int bytesRemaining = result.length - totalBytesRead;
	          //input.read() returns -1, 0, or more :
	          int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
	          if (bytesRead > 0){
	            totalBytesRead = totalBytesRead + bytesRead;
	          }
	        }
	      //  log("Num bytes read: " + totalBytesRead);
	      }
	      finally {
	      //  log("Closing input stream.");
	        input.close();
	      }
	    }
	    catch (FileNotFoundException ex) {
	   //   log("File not found.");
	    }
	    catch (IOException ex) {
	     // log(ex);
	    }
	    return result;
	  }

	private void writeToFile(String decoded) {
		logger.info(Thread.currentThread().getName() + " will flush.");
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(
					String.format(filePath + HuffmanInterface.decompressedFileExtension,
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

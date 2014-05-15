package edu.huffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ThreadedEncoder extends Huffman {
	/**
	 * The characters in the input string are ASCII encoded
	 * */

	private Integer bytesPerThread;

	private void setBytesPerThread(int fileSize, int threadsCount) {
		bytesPerThread = fileSize / ((threadsCount - 1) * BUFFER_SIZE);
	}

	/**
	 * The output destination of the produced compressed files
	 * */
	private String outputDestination;

	public void setOutputDestination(String destination) {
		this.outputDestination = destination;
	}

	public ThreadedEncoder(String filepath, Integer maxTasksCount,
			Boolean isQuiet) {
		super(filepath, maxTasksCount, isQuiet);
		this.outputDestination = filepath + ".compressed";
	}

	private ArrayList<String> readFilePart(long seekToByte,
			long requiredBytesCount) throws IOException {

		ArrayList<String> filePart = new ArrayList<>();

		File sourceFile = new File(filePath);

		long startAtByte = 0;

		try {

			RandomAccessFile rand = new RandomAccessFile(sourceFile, "r");
			rand.seek(seekToByte);
			startAtByte = rand.getFilePointer();
			rand.close();

		} catch (IOException e) {
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(sourceFile));
			reader.skip(startAtByte);
			String line;
			long totalRead = 0;
			char[] buffer = new char[BUFFER_SIZE];
			while (totalRead < requiredBytesCount
					&& (-1 != reader.read(buffer, 0, BUFFER_SIZE))) {
				filePart.add(new String(buffer));
				totalRead += BUFFER_SIZE;

			}
		} catch (Exception e) {

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ignore) {
				}
			}
		}
		return filePart;
	}

	public String generateEncodedFilePart(ArrayList<String> data) {
		StringBuilder result = new StringBuilder();
		Encoder encoder = new Encoder();
		String encodedData = encoder.encode(data);
		String tree = encoder.getTree().toString();
		result.append(tree);
		result.append('\n');
		result.append(encodedData);
		return result.toString();
	}
	
}

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

	private Long bytesPerThread;
	private Thread jobs[];

	private void initializeThreadParameters(long fileSize, int threadsCount) {

		bytesPerThread = (fileSize / ((threadsCount - 1) * BUFFER_SIZE))
				* BUFFER_SIZE;
		jobs = new Thread[threadsCount];
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
		File file = new File(filePath);
		initializeThreadParameters(file.length(), maxTasksCount);
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
					&& (-1 != reader.read(buffer, 0,
							(int) Math.min(BUFFER_SIZE, requiredBytesCount)))) {
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

	private void createThread(long seekToByte, long requiredBytesCount,
			int threadIndex) {
		ArrayList<String> rawData;
		try {
			rawData = readFilePart(seekToByte, requiredBytesCount);
			Encoder r = new Encoder(rawData, outputDestination + ".part"
					+ threadIndex);
			Thread t = new Thread(r);
			jobs[threadIndex] = t;
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void runThreads() throws InterruptedException {

		System.out.printf("%s", Thread.currentThread().getName());
		// / initialize and feed threads
		int seekToByte = 0;
		long requiredBytesCount = bytesPerThread;
		logger.info("Started reading file to compress");
		for (int index = 0; index < (maxTasksCount - 1); index++) {
			createThread(seekToByte, requiredBytesCount, index);
			seekToByte += bytesPerThread;
		}

		File file = new File(filePath);
		long lastThreadPosition = bytesPerThread * (maxTasksCount - 1);
		if (file.length() - lastThreadPosition > 0) {
			createThread(lastThreadPosition,
					file.length() - lastThreadPosition, maxTasksCount - 1);
		} else {
		// one thread is unneeded
			maxTasksCount--;
		}
		// / run threads
		for (int i = 0; i < maxTasksCount; i++) {
			String logMessage = "Compressing thread "+ (i+1) +" started";
			logger.info(logMessage);
			jobs[i].start();
		}
		for (int i = 0; i < maxTasksCount; i++) {
			jobs[i].join();
		}
	}

}

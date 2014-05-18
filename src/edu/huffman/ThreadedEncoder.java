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

	private int buffersPerThread;
	private Thread jobs[];
	private ArrayList<String> fileData;

	public ThreadedEncoder(String filepath, Integer maxTasksCount,
			Boolean isQuiet) {
		super(filepath, maxTasksCount, isQuiet);
		this.outputDestination = filepath + ".compressed";
		File file = new File(filePath);
		fileData = new ArrayList<>();
		initializeThreadParameters((int) file.length(), maxTasksCount);
	}

	private void initializeThreadParameters(int fileSize, int threadsCount) {
		if (threadsCount > 1) {
			buffersPerThread = (fileSize / ((threadsCount - 1) * BUFFER_SIZE));
		} else {
			buffersPerThread = 0;
		}
		jobs = new Thread[threadsCount];
		try {
			readFile();
		} catch (IOException e) {
			System.err.println("Reading file failed.");
		}
	}

	/**
	 * The output destination of the produced compressed files
	 * */
	private String outputDestination;

	public void setOutputDestination(String destination) {
		this.outputDestination = destination;
	}

	private void readFile() throws IOException {

		File sourceFile = new File(filePath);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(sourceFile));
			String line;
			int totalRead = 0;
			char[] buffer = new char[BUFFER_SIZE];
			while (totalRead < sourceFile.length()
					&& (-1 != reader.read(
							buffer,
							0,
							(int) Math.min(BUFFER_SIZE, sourceFile.length()
									- totalRead)))) {
				fileData.add(new String(buffer));
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

	}

	private ArrayList<String> readFilePart(int seekToBuffer,
			int requiredBuffersCount) {
		ArrayList<String> result = new ArrayList<>();
		for (int bufferIndex = seekToBuffer; bufferIndex < seekToBuffer
				+ requiredBuffersCount; bufferIndex++) {
			result.add(fileData.get(bufferIndex));
		}
		return result;
	}

	private void createThread(int seekToBuffer, int requiredBuffersCount,
			int threadIndex) {
		ArrayList<String> rawData;
		rawData = readFilePart(seekToBuffer, requiredBuffersCount);

		Encoder r = new Encoder(rawData, outputDestination + ".part"
				+ threadIndex);
		Thread t = new Thread(r);
		jobs[threadIndex] = t;
	}

	public void runThreads() throws InterruptedException {

		System.out.printf("%s", Thread.currentThread().getName());
		// / initialize and feed threads
		int seekToBuffer = 0;
		int requiredBuffersCount = buffersPerThread;
		logger.info("Started reading file to compress");

		for (int index = 0; index < (maxTasksCount - 1); index++) {
			createThread(seekToBuffer, requiredBuffersCount, index);
			String logMessage = "Compressing thread " + (index + 1)
					+ " started";
			logger.info(logMessage);
			jobs[index].start();
			seekToBuffer += buffersPerThread;
		}

		File file = new File(filePath);
		int lastThreadPosition = buffersPerThread * (maxTasksCount - 1);
		if (file.length() - lastThreadPosition > 0) {
			createThread(lastThreadPosition,
					((int) file.length() / BUFFER_SIZE + 1)
							- lastThreadPosition, maxTasksCount - 1);
			String logMessage = "Compressing thread " + (maxTasksCount)
					+ " started";
			logger.info(logMessage);
			jobs[maxTasksCount - 1].start();
		} else {
			// one thread is unneeded
			maxTasksCount--;
		}
		fileData.clear();
		// / run threads
		for (int i = 0; i < maxTasksCount; i++) {
			jobs[i].join();
		}
	}

}

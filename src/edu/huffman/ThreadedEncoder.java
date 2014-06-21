package edu.huffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ThreadedEncoder extends HuffmanInterface {
	/**
	 * The characters in the input string are ASCII encoded
	 * */

	private int buffersPerThread;
	private Thread jobs[];
	private ArrayList<String> fileData;

	public ThreadedEncoder(String filepath, Integer maxTasksCount,
			Boolean isQuiet) {
		super(filepath, maxTasksCount, isQuiet);
		this.outputDestination = filepath;
		//File file = new File(filePath);
		fileData = new ArrayList<>();
		try {
			readFile();
		} catch (IOException e) {
			System.err.println("Reading file failed.");
		}
	}

	private void initializeThreadParameters(int fileLines, int threadsCount) {
		if (threadsCount > 1) {
			buffersPerThread = (fileLines / ((threadsCount - 1)));
		} else {
			buffersPerThread = 0;
		}
		jobs = new Thread[threadsCount];

	}

	/**
	 * The output destination of the produced compressed files
	 * */
	private String outputDestination;

	public void setOutputDestination(String destination) {
		this.outputDestination = destination;
	}

	private void readFile() throws IOException {
		logger.info("Started reading the file.");
		File sourceFile = new File(filePath);

		BufferedReader reader = null;
		try (BufferedReader fileReader = new BufferedReader(new FileReader(
				sourceFile))) {

			String sCurrentLine;

			while ((sCurrentLine = fileReader.readLine()) != null) {
				fileData.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeThreadParameters(fileData.size(), maxTasksCount);
		logger.info("Finished reading the file.");

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
		Encoder r = new Encoder(rawData, outputDestination, threadIndex);
		Thread t = new Thread(r);
		jobs[threadIndex] = t;
	}

	public void runThreads() throws InterruptedException { // / initialize and
															// feed threads
		int seekToBuffer = 0;
		int requiredBuffersCount = buffersPerThread;
		logger.info("Started reading file to compress");

		for (int index = 0; index < (maxTasksCount - 1); index++) {
			createThread(seekToBuffer, requiredBuffersCount, index);
			String logMessage = "Compressing thread " + (index + 1)
					+ " started";
			logger.info(logMessage);
			
			seekToBuffer += buffersPerThread;
		}
		long startTime = System.currentTimeMillis();
for(int index = 0; index<(maxTasksCount-1);index++){
	jobs[index].start();
}

		File file = new File(filePath);
		int lastThreadPosition = buffersPerThread * (maxTasksCount - 1);
		if (fileData.size() - lastThreadPosition > 0) {
			createThread(lastThreadPosition,
					fileData.size() - lastThreadPosition,
					maxTasksCount - 1);
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
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		System.out.println("Total construction time :" + executionTime);
		 //+Encoder.getFrequencyTableConstructionTime());
	}

}

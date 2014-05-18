package edu.huffman;

public class ThreadedDecoder extends Huffman {
	/**
	 * The output destination of the produced compressed files
	 * */
	private String outputDestination;
	private Thread jobs[];

	public ThreadedDecoder(String filepath, Integer maxTasksCount,
			Boolean isQuiet) {
		super(filepath, maxTasksCount, isQuiet);
		this.outputDestination = filepath + ".decompressed";

	}

	public void setOutputDestination(String destination) {
		this.outputDestination = destination;
	}

	public void runThreads() {
		jobs = new Thread[maxTasksCount];
		for (int threadIndex = 0; threadIndex < maxTasksCount; threadIndex++) {
			Decoder decoder = new Decoder("path");
			Thread t = new Thread(decoder);
			jobs[threadIndex] = t;

		}
	}
}

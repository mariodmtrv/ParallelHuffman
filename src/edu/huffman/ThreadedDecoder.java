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
		for (int threadIndex = 0; threadIndex < maxTasksCount - 1; threadIndex++) {
			Decoder decoder = new Decoder(filePath, threadIndex);
			Thread t = new Thread(decoder);
			jobs[threadIndex] = t;
			jobs[threadIndex].run();
		}
		// check if last thread exists
		for (int threadIndex = 0; threadIndex < maxTasksCount - 1; threadIndex++) {
			try {
				jobs[threadIndex].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

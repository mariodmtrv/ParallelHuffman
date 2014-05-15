public class ThreadedDecoder extends Huffman {
	/**
	 * The output destination of the produced compressed files
	 * */
	private String outputDestination;

	public ThreadedDecoder(String filepath, Integer maxTasksCount,
			Boolean isQuiet) {
		super(filepath, maxTasksCount, isQuiet);
		this.outputDestination = filepath + ".decompressed";

	}

	public void setOutputDestination(String destination) {
		this.outputDestination = destination;
	}
}

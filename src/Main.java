import edu.huffman.ThreadedEncoder;

public class Main {
	public static void main(String[] args) throws Exception {
		String filepath = CLITool.getFilePath(args);
		Integer maxTasksCount = CLITool.getMaxTasksCount(args);
		Boolean isQuiet = CLITool.getIsQuiet(args);
		ThreadedEncoder encoder = new ThreadedEncoder(filepath, maxTasksCount,
				isQuiet);
		encoder.runThreads();
	}
}

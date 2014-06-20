import edu.huffman.ThreadedEncoder;

public class Main {
	public static void main(String[] args) throws Exception {
		/*
		 * Encoder ftg = new Encoder("test.txt"); int[] buffer=
		 * ftg.generateFrequencyMap(0, 24); for(int ind=0;
		 * ind<buffer.length;ind++){ if(buffer[ind]>0){
		 * System.out.println(buffer[ind]); } }
		 */
		String filepath = CLITool.getFilePath(args);
		Integer maxTasksCount = CLITool.getMaxTasksCount(args);
		Boolean isQuiet = CLITool.getIsQuiet(args);
		ThreadedEncoder encoder = new ThreadedEncoder(filepath, maxTasksCount,
				isQuiet);
		encoder.runThreads();
	}
}

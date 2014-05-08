import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.text.ParseException;

public class Huffman {
	/**
	 * The maximum number of threads that the program may use
	 * */
	private static Integer maxTasksCount;
	/**
	 * Path to the file that will be compressed
	 * */
	private static String filePath;
	/**
	 * Indicates quiet mode
	 * */
	private static Boolean isQuiet;
	/**
	 * Indicator that max tasks count will follow
	 * */
	private static final String[] MAX_TASKS_COUNT_INDICATORS = { "-t", "-tasks" };
	/**
	 * Indicator that max tasks count will follow
	 * */
	private static final String[] FILE_PATH_INDICATORS = { "-f", "-file" };

	/**
	 * Indicator that quiet should be run. Quiet mode is ...
	 * */
	private static final String[] QUIET_MODE_INDICATORS = { "-q", "-quiet" };

	private static String getFilePath(String[] args) throws Exception {
		for (String filePathIndicator : FILE_PATH_INDICATORS) {
			if (args[0] == filePathIndicator) {
				return args[1];
			}
		}
		throw new Exception("No file path indicated");
	}

	private static Integer getMaxTasksCount(String[] args) throws Exception {
		for (String tasksIndicator : MAX_TASKS_COUNT_INDICATORS) {
			if (args[2] == tasksIndicator) {
				try {
					Integer maxTasksCount = Integer.parseInt(args[3]);
					if (maxTasksCount < 1) {
						throw new NumberFormatException(
								"Thread count must be >= 1");
					}
					return maxTasksCount;
				} catch (NumberFormatException p) {
					System.err.println("Tasks count not appropriate");
				}
			}
		}
		throw new Exception("Max tasks count reading failed");
	}

	private static Boolean getIsQuiet(String[] args) {
		if (args.length == 5) {
			for (String isQuietIndicator : QUIET_MODE_INDICATORS) {
				if (args[4] == isQuietIndicator) {
					return true;
				}
			}
		}
		return false;
	}
/*
	public static void main(String[] args) throws Exception {
		String[] args1 = { "-f", "hello.bat", "-t", "535" };
		filePath = getFilePath(args1);
		maxTasksCount = getMaxTasksCount(args1);
		isQuiet = getIsQuiet(args1);
	}
	*/
}

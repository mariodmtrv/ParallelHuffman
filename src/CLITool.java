public class CLITool {
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

	public static String getFilePath(String[] args) throws Exception {
		for (String filePathIndicator : FILE_PATH_INDICATORS) {

			if (args[0].contains(filePathIndicator)) {
				
				return args[1];
			}
		}
		throw new Exception("No file path indicated");
	}

	public static Integer getMaxTasksCount(String[] args) throws Exception {
		for (String tasksIndicator : MAX_TASKS_COUNT_INDICATORS) {
			System.out.println(args[2]);
			if (args[2].contains(tasksIndicator)) {
				try {
					System.out.println(args[3]);
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

	public static Boolean getIsQuiet(String[] args) {
		if (args.length == 5) {
			for (String isQuietIndicator : QUIET_MODE_INDICATORS) {
				if (args[4] == isQuietIndicator) {
					return true;
				}
			}
		}
		return false;
	}

}

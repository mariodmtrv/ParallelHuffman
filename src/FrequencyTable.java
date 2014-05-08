import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class FrequencyTable {
	/**
	 * The characters in the input string are ASCII encoded
	 * */
	private static final Integer MAX_DIFFERENT_CHARACTERS = 256;

	private static final Integer BUFFER_SIZE = 64;

	private Integer bytesPerThread;

	private String filePath;

	public FrequencyTable(String filePath) {
		this.filePath = filePath;
	}

	private void setBytesPerThread(int fileSize, int threadsCount) {
		bytesPerThread = fileSize / ((threadsCount - 1) * BUFFER_SIZE);
	}

	private int[] addFrequenciesFromBuffer(char[] buffer, int[] frequencyMap) {
		for (char character : buffer) {
			frequencyMap[(int) character]++;
		}
		return frequencyMap;
	}

	int[] generateFrequencyMap(long seekToByte, long requiredBytesCount)
			throws IOException {
		int[] frequencyMap = new int[MAX_DIFFERENT_CHARACTERS];

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
					&& (-1 != reader.read(buffer, 0, BUFFER_SIZE))) {
				System.out.println(new String(buffer));
				totalRead += BUFFER_SIZE;
				frequencyMap = addFrequenciesFromBuffer(buffer, frequencyMap);
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

		return frequencyMap;
	}

}

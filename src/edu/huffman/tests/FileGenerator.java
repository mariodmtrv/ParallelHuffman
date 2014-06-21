package edu.huffman.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.junit.Test;

public class FileGenerator {

	@Test
	public void createFile() throws IOException {
		Random randomGenerator = new Random();

		Charset charset = Charset.forName("US-ASCII");
		Path filePath = FileSystems.getDefault().getPath("tests",
				"testfile256M.txt");
		// 2^26 = 64 MB
		// 2^16 = 1 MB
		// 2^22 = 64 MB
		// 2^24 = 256 MB
		try (BufferedWriter writer = Files.newBufferedWriter(filePath,
				charset)) {
			for (int i = 0; i < 1 << 24; i++) {

				char c[] = new char[16];

				for (int j = 0; j < 15; j++) {
					c[j] = (char) (randomGenerator.nextInt() % 30 + 64);

				}
				c[15]='\n';
				String s = new String(c);
				writer.write(s, 0, s.length());
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

	}
}

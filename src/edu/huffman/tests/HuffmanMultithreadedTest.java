package edu.huffman.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.huffman.Huffman;
import edu.huffman.ThreadedEncoder;

public class HuffmanMultithreadedTest {

	@Test
	public void test() {
		ThreadedEncoder encoder = new ThreadedEncoder("test.txt", 2, false);
		encoder.runThreads();
	}

}

package edu.huffman.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.junit.Test;
import edu.huffman.HuffmanInterface;
import edu.huffman.ThreadedEncoder;

public class HuffmanMultithreadedTest {

	@Test
	public void test16M() throws InterruptedException {
		ThreadedEncoder encoder = new ThreadedEncoder("tests/testfile16.txt",1, false);
		encoder.runThreads();
		}

}

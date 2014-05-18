package edu.huffman.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.junit.Test;

import edu.BinaryCodec;
import edu.huffman.Huffman;
import edu.huffman.ThreadedEncoder;

public class HuffmanMultithreadedTest {

	@Test
	public void test() throws InterruptedException {
		ThreadedEncoder encoder = new ThreadedEncoder("testfile.txt", 32, false);
		encoder.runThreads();
		}
//@Test
public void createFile() throws IOException{
	Random randomGenerator = new Random();
	PrintWriter out = new PrintWriter(new BufferedWriter(
			new FileWriter("testfile.txt")));
	for(int i =0; i<200000000;i++){
		out.print( (char)(randomGenerator.nextInt()%256));
	}
	out.close();
}
}

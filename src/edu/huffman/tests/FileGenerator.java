package edu.huffman.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.junit.Test;

public class FileGenerator {

	@Test
public void createFile() throws IOException{
	Random randomGenerator = new Random();
	PrintWriter out = new PrintWriter(new BufferedWriter(
			new FileWriter("tests/testfile16.txt")));
	//2^26 = 64 MB
	//2^24 = 16 MB
	//2^27 = 128 MB
	//2^29 = 512 MB
	for(int i =0; i<1<<24;i++){
		out.print( (char)(randomGenerator.nextInt()%256));
	}
	out.close();
}
}

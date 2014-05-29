package edu.huffman.tests;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

import edu.huffman.ByteConverter;

public class ByteConverterTest {

	@Test
	public void test() {
		byte[] x = ByteConverter.toByteArray("101110000"); 
				System.out.println(x[0] + " " + x[1]);
	}

}

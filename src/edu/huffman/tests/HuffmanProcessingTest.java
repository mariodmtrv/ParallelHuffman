package edu.huffman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.huffman.Decoder;
import edu.huffman.Encoder;
import edu.huffman.ThreadedEncoder;

public class HuffmanProcessingTest {

	private ArrayList<String> generateData() {

		ArrayList<String> data = new ArrayList<>();
		data.add("ABRACADABRA");
		return data;

	}

	@Test
	public void testEncoding_WithExpectedEncodedLength() {
		Encoder encoder = new Encoder();
		String encoded = encoder.encode(generateData());
		int expectedLength = 23;
		int actualLength = encoded.length();
		assertEquals(expectedLength, actualLength);
	}

	@Test
	public void test_EncodeDecodeLoop() {
		Encoder encoder = new Encoder();
		String encoded = encoder.encode(generateData());
		Decoder decoder = new Decoder(encoder.getTree().getInnerTree());
		String result = decoder.decode(encoded);
		assertEquals("ABRACADABRA", result);
	}
	@Test 
	public void generate(){
		ThreadedEncoder enc= new ThreadedEncoder("", 1, true);
		System.out.println(enc.generateEncodedFilePart(generateData()));
	}
}

package edu.huffman;

import java.util.BitSet;

public class ByteConverter {
/**
 * Converts the given 0-1 string to a less memory consuming byte array 
 * */
	public static byte[] toByteArray(String str) {
		BitSet b = new BitSet();
		char[] charArray = str.toCharArray();
		for (int bitIndex = 0; bitIndex < charArray.length; bitIndex++) {
			if (charArray[charArray.length- bitIndex-1] == '1') {
				b.set(bitIndex);
			}
		}
		byte[] converted = b.toByteArray();
		return converted;
	}

}

package edu.huffman.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.huffman.algorithm.Tree;

public class HuffmanEncodingTest {

	@Before
	private Tree generateTree() {
		int[] frequencyMap = new int[256];
		frequencyMap['A'] = 5;
		frequencyMap['B'] = 3;
		frequencyMap['C'] = 1;
		frequencyMap['D'] = 1;
		frequencyMap['R'] = 2;
		Tree tree = new Tree();
		tree.buildTree(frequencyMap);
		return tree;
	}

	@Test
	public void testTreeConstruction() {
		Tree tree = generateTree();
		assertEquals(tree.getCharCode('A'), "1");
		assertEquals(tree.getCharCode('B'), "01");
		assertEquals(tree.getCharCode('C'), "0011");
		assertEquals(tree.getCharCode('D'), "0010");
		assertEquals(tree.getCharCode('R'), "000");

	}

	@Test
	public void testTreeSerialization() {
		Tree tree = generateTree();
		String expected = "";
		String actual = tree.toString();
		fail("Not yet implemented");
		assertEquals(expected, actual);
	}

}

package edu.huffman.tests;

import static org.junit.Assert.*;
import edu.huffman.algorithm.Tree;

import org.junit.Before;
import org.junit.Test;

public class HuffmanTreeTest {
/*
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
		String expected = "{[$,12]{[$,7]{[$,4]{[R,2]{}${}}${[$,2]"
				+ "{[D,1]{}${}}${[C,1]{}${}}}}${[B,3]{}${}}}${[A,5]{}${}}}";
		String actual = tree.toString();
		assertEquals(expected, actual);
	}

	@Test
	public void testDeserialization() {
		String serializedTree = "{[$,12]{[b,40]{}${}}${[a,6]{}${}}}";
		Tree tree = new Tree();
		tree.deserialize(serializedTree);
		assertEquals(tree.toString(), serializedTree);
	}
*/
}

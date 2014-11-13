package taojava.test;

import org.junit.Before;

import taojava.util.SortedLinkedList;

/**
 * Tests of sorted array lists.
 *
 * @author Samuel A. Rebelsky
 */
public class SortedLinkedListTest
extends SortedListTest
{
  @Before
  public void setup()
  {
    this.ints = new SortedLinkedList<Integer>();
    this.strings = new SortedLinkedList<String>();
  } // setup
} // SortedLinkedListTest

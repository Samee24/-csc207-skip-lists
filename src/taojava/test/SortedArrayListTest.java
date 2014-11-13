package taojava.test;

import org.junit.Before;

import taojava.util.SortedArrayList;

/**
 * Tests of sorted array lists.
 *
 * @author Samuel A. Rebelsky
 */
public class SortedArrayListTest
extends SortedListTest
{
  @Before
  public void setup()
  {
    this.ints = new SortedArrayList<Integer>();
    this.strings = new SortedArrayList<String>();
  } // setup
} // SortedArrayListTest

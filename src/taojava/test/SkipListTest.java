package taojava.test;

import org.junit.Before;

import taojava.util.SkipList;

/**
 * Tests of sorted array lists.
 *
 * @author Samuel A. Rebelsky
 */
public class SkipListTest
extends SortedListTest
{
  @Before
  public void setup()
  {
    this.ints = new SkipList<Integer>();
    this.strings = new SkipList<String>();
  } // setup
} // SkipListTest

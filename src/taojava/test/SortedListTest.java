package taojava.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.*;

import org.junit.Test;

import taojava.util.SortedList;

/**
 * Generic tests of sorted lists.
 *
 * To test a particular implementation of sorted lists, subclass this
 * class and add an appropriate @Before clause to fill in strings and
 * ints.
 * 
 * @author Samuel A. Rebelsky
 */
public class SortedListTest
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * A sorted list of strings for tests.  (Gets set by the subclasses.)
   */
  SortedList<String> strings;

  /**
   * A sorted list of integers for tests.  (Gets set by the subclasses.)
   */
  SortedList<Integer> ints;

  /**
   * A random number generator for the randomized tests.
   */
  Random random = new Random();

  // +---------+---------------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Dump a SortedList to stderr.
   */
  static <T extends Comparable<T>> void dump(SortedList<T> slist)
  {
    System.err.print("[");
    for (T val : slist)
      {
        System.err.print(val + " ");
      } // for
    System.err.println("]");
  } // dump

  /**
   * Determine if an iterator only returns values in non-decreasing
   * order.
   */
  static <T extends Comparable<T>> boolean inOrder(Iterator<T> it)
  {
    // Simple case: The empty iterator is in order.
    if (!it.hasNext())
      return true;
    // Otherwise, we need to compare neighboring elements, so
    // grab the first element.
    T current = it.next();
    // Step through the remaining elements
    while (it.hasNext())
      {
        // Get the next element
        T next = it.next();
        // Verify that the current node <= next
        if (current.compareTo(next) > 0)
          {
            return false;
          } // if (current > next)
        // Update the current node
        current = next;
      } // while
    // If we've made it this far, everything is in order
    return true;
  } // inOrder(Iterator<T> it)

  // +-------------+-----------------------------------------------------
  // | Basic Tests |
  // +-------------+

  /**
   * A really simple test.  Add an element and make sure that it's there.
   */
  @Test
  public void simpleTest()
  {
    strings.add("hello");
    assertTrue(strings.contains("hello"));
    assertFalse(strings.contains("goodbye"));
  } // simpleTest()

  /**
   * Another simple test.  The list should not contain anything when
   * we start out.
   */
  @Test
  public void emptyTest()
  {
    assertFalse(strings.contains("hello"));
  } // emptyTest()

  // +-----------------+-------------------------------------------------
  // | RandomizedTests |
  // +-----------------+

  /**
   * Verify that a randomly created list is sorted.
   */
  @Test
  public void testOrdered()
  {
    // For reporting errors: an array of operations
    ArrayList<String> operations = new ArrayList<String>();
    // Add a bunch of values
    for (int i = 0; i < 100; i++)
      {
        int rand = random.nextInt(1000);
        ints.add(rand);
        operations.add("ints.add(" + rand + ")");
      } // for
    if (!inOrder(ints.iterator()))
      {
        System.err.println("inOrder() failed");
        for (String op : operations)
          System.err.println(op + ";");
        dump(ints);
        fail("The instructions did not produce a sorted list.");
      } // if the elements are not in order.
  } // testOrdered()

  /**
   * Verify that a randomly created list contains all the values
   * we added to the list.
   */
  @Test
  public void testContainsOnlyAdd()
  {
    ArrayList<String> operations = new ArrayList<String>();
    ArrayList<Integer> vals = new ArrayList<Integer>();
    // Add a bunch of values
    for (int i = 0; i < 10; i++)
      {
        int rand = random.nextInt(200);
        vals.add(rand);
        operations.add("ints.add(" + rand + ")");
        ints.add(rand);
      } // for i
    // Make sure that they are all there.
    for (Integer val : vals)
      {
        if (!ints.contains(val))
          {
            System.err.println("contains(" + val + ") failed");
            for (String op : operations)
              System.err.println(op + ";");
            dump(ints);
            fail(val + " is not in the sortedlist");
          } // if (!ints.contains(val))
      } // for val
  } // testContainsOnlyAdd()

  /**
   * An extensive randomized test.
   */
  @Test
  public void randomTest()
  {
    // Set up a list of all the operations we performed.  (That way,
    // we can replay a failed test.)
    ArrayList<String> operations = new ArrayList<String>();
    // Keep track of the values that are currently in the sorted list.
    ArrayList<Integer> vals = new ArrayList<Integer>();

    // Add a bunch of values
    for (int i = 0; i < 1000; i++)
      {
        boolean ok = true;
        int rand = random.nextInt(2000);
        // Half the time we add
        if (random.nextBoolean())
          {
            if (!ints.contains(rand))
              vals.add(rand);
            operations.add("ints.add(" + rand + ")");
            ints.add(rand);
            if (!ints.contains(rand))
              {
                System.err.println("After adding " + rand + " contains fails");
                ok = false;
              } // if (!ints.contains(rand))
          } // if we add
        // Half the time we remove
        else
          {
            operations.add("ints.remove(" + rand + ")");
            ints.remove(rand);
            vals.remove((Integer) rand);
            if (ints.contains(rand))
              {
                System.err.println("After removing " + rand
                                   + " contains succeeds");
                ok = false;
              } // if ints.contains(rand)
          } // if we remove
        // See if all of the appropriate elements are still there
        for (Integer val : vals)
          {
            if (!ints.contains(val))
              {
                System.err.println("ints no longer contains " + val);
                ok = false;
                break;
              } // if the value is no longer contained
          } // for each value
        // Dump the instructions if we've encountered an error
        if (!ok)
          {
            for (String op : operations)
              System.err.println(op + ";");
            dump(ints);
            fail("Operations failed");
          } // if (!ok)
      } // for i
  } // randomTest()

  // +-------------------+----------------------------------------------
  // | Predictable Tests |
  // +-------------------+

  /**
   * Verify that a list that contains repeated elements works
   * as expected
   */
  @Test
  public void testRepeatedElements()
  {
    // Add a bunch of values (two of each)
    for (int i = 0; i < 10; i++)
      {
        ints.add(i);
        ints.add(i);
      } // for
    // Make sure that they are all there.
    for (int i = 0; i < 10; i++)
      {
        if (!ints.contains(i))
          fail(i + " was not found in the sorted list");
        ints.remove(i);
        if (ints.contains(i))
          fail(i + " wasfound in the sorted list");
      }//for
  } // testRepeatedElements()

  /**
   * Test that our sorted Lists work with negative values
   */
  @Test
  public void testNegativeElements()
  {
    // Add a bunch of negative values
    for (int i = -10; i > 0; i--)
      {
        ints.add(i);
        ints.add(i);
      } // for
    // Make sure that they are all there.
    for (int i = 0; i < 10; i++)
      {
        ints.remove(i);
        if (ints.contains(i))
          fail(i + " was not found in the sorted list");
      }//for
  } // testNegativeElements()

  /**
   * Test that our sorted Lists work with multiple
   * identical elements
   */
  @Test
  public void testDistribution()
  {
    // Add a bunch of negative values
    for (int i = 0; i < 10; i++)
      {
        for (int j = 0; j < i; j++)
          {
            ints.add(i);
          }// for
      } // for
    // Make sure that they are all there. Don't count 0.
    for (int i = 1; i < 10; i++)
      {
        if (!ints.contains(i))
          fail(i + " was not found in the sorted list");
        ints.remove(i);
        if (ints.contains(i))
          fail(i + " was found in the sorted list");
      }// for
  }// testDistribution()

  /**
   * Test that our iterator remove method removes a single
   * element from the sorted list
   */
  @Test
  public void testIteratorRemove()
  {
    // Add a bunch of values
    for (int i = 0; i < 10; i++)
      {
        // We add three 4's
        if (i == 4)
          {
            ints.add(i);
            ints.add(i);
            ints.add(i);
          }// if
        else
          ints.add(i);
      }// for
    // Create an iterator, loop until we reach the fours
    Iterator<Integer> iterator = ints.iterator();
    while (iterator.next() != 4)
      {
        iterator.next();
      }// while
    // Remove 4's one by one
    for (int i = 0; i < 3; i++)
      {
        if (!ints.contains(4))
          fail("4 was not found in the sorted list!");
        iterator.remove();
        iterator.next();
      }// for
    // Make sure we've removed the 4's
    if (ints.contains(4))
      fail("4 was found in the sorted list!");

  } // testIteratorRemove()

  /**
   * Test that multiple iterators register when other iterators
   * edit the sorted list
   */
  @Test
  public void testIteratorMods()
  {
    // Add a bunch of negative values
    for (int i = 10; i < 21; i++)
      {
        ints.add(i);
      }// for
    // Make sure that all values are there. Don't count 0.
    Iterator<Integer> iterator1 = ints.iterator();
    Iterator<Integer> iterator2 = ints.iterator();
    // We move both iterators to center 
    for (int i = 0; i < 5; i++)
      {
        iterator1.next();
        iterator2.next();
      }// for
    try
      {
        /*
         * Remove an element with one iterator, try to call next with the other
         * we expect an exception
         */
        iterator1.remove();
        iterator2.next();
        fail("Iterator should throw an exception upon external skip list modification");
      }// try
    catch (Exception e)
      {
        //Do nothing, success
      }// catch

  } // testIteratorMods()

  /**
   * Further test that our sorted lists works well with strings, we test iterator logic, remove,
   * add methods
   */
  @Test
  public void stringGeneralTest()
  {
    // Set up list of strings
    strings.add("Henry");
    strings.add("Alicia");
    strings.add("Walden");
    strings.add("Ryo");
    strings.add("Goku");
    // Make first iterator, perform operations
    Iterator<String> iterator1 = strings.iterator();
    iterator1.next();
    iterator1.next();
    iterator1.remove();
    iterator1.next();
    // Make second iterator, perform operations
    Iterator<String> iterator2 = strings.iterator();
    iterator2.next();
    iterator2.remove();
    // Because iterator2 has modified the list, we hope iterator1 fails
    try
      {
        iterator1.next();
        fail("Iterator should throw an exception upon external skip list modification");
      }// try
    catch (Exception e)
      {
        //Do nothing - success
      }// catch
    strings.add("Dr.D00M");
    // Because we added an additional element, we want iterator2 to crash
    try
      {
        iterator2.next();
        fail("Iterator should throw an exception upon external skip list modification");
      }// try
    catch (Exception e)
      {
        //Do nothing - we want it to crash!!!
      }// catch
  } // stringGeneralTest()
} // class SortedListTest

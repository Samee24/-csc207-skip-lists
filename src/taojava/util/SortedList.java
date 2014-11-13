package taojava.util;

import java.util.Iterator;

/**
 * Sorted lists - dynamic collections that support insertion, removal,
 * search, and iteration from smallest to largest. 
 * 
 * @author Samuel A. Rebelsky
 */
public interface SortedList<T extends Comparable<T>>
    extends Iterable<T>, SimpleSet<T>, SemiIndexed<T>
{
  /**
   * Return an iterator that visits the elements of the list
   * from smallest to largest.
   */
  public Iterator<T> iterator();
  
  /**
   * Add an element.  It is up to the implementer whether or not the
   * list can have more than one copy of the same value.
   */
  public void add(T val);
} // interface SortedList<T>

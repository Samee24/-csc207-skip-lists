package taojava.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Sorted lists implemented with Java's array lists.  As one
 * might expect, the iteration and index operations are fairly
 * fast, the insert and remove elements are relatively slow.
 * 
 * @author Samuel A. Rebelsky
 */
public class SortedArrayList<T extends Comparable<T>>
    implements SortedList<T>
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying ArrayList.
   */
  ArrayList<T> core;

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new sorted list.
   */
  public SortedArrayList()
  {
    this.core = new ArrayList<T>();
  } // SortedArrayList()

  // +-------------------------+-----------------------------------------
  // | Internal Helper Methods |
  // +-------------------------+

  /**
   * Determine if val appears at the given index.
   */
  boolean valAppearsAt(T val, int index)
  {
    // If the index is too large, it clearly doesn't appear
    if (index >= this.core.size())
      return false;
    // Compare to the value at the index
    T tmp = this.core.get(index);
    return ((tmp != null) && (val.compareTo(tmp) == 0));
  } // appearsAt

  /**
   * Find the index of val.  If val is not in the array, returns the
   * index of where val should go (either the index of the first value
   * greater than val, if there is such an index, or length, if there
   * is no such index).
   */
  int findIndex(T val)
  {
    int lb = 0;
    int ub = this.core.size();

    // Invariant:
    //   +-------+------+-------+
    //   | < val |  ?   | > val |
    //   +-------+------+-------+
    //   0       lb     ub      length
    while (lb < ub)
      {
        // Find the midpoint in a relatively safe way (assuming
        // the lb and ub are positive integers).
        int mid = lb + (ub - lb) / 2;
        T midval = this.core.get(mid);
        if (val.compareTo(midval) == 0)
          return mid;
        else if (val.compareTo(midval) > 0)
          lb = mid + 1;
        else
          ub = mid;
      } // while

    // If we've reached this point we know that
    //   +-------+-------+
    //   | < val | > val |
    //   +-------+-------+
    //   0       lb,ub   length
    // We therefore need to add an element at lb.
    return lb;
  } // findIndex(T)

  // +-----------------------+-------------------------------------------
  // | Methods from Iterable |
  // +-----------------------+

  /**
   * Return an iterator that steps through the values of the list from
   * smallest to largest.
   */
  public Iterator<T> iterator()
  {
    // We use a wrapper/adapter class, even though we currently
    // don't do any adaptations, because we might eventually 
    // find it useful to adapt.
    return new Iterator<T>()
      {
        // An underlying iterator.
        Iterator<T> core = SortedArrayList.this.core.iterator();

        public T next()
        {
          return core.next();
        } // next()

        public boolean hasNext()
        {
          return core.hasNext();
        } // hasNext()

        public void remove()
        {
          core.remove();
        } // remove()
      }; // new Iterator<T>
  } // iterator()

  // +------------------------+------------------------------------------
  // | Methods from SimpleSet |
  // +------------------------+

  /**
   * Add a value to the set.
   *
   * @post contains(val)
   * @post For all lav != val, if contains(lav) held before the call
   *   to add, contains(lav) continues to hold.
   */
  public void add(T val)
  {
    // Find the index
    int index = this.findIndex(val);
    // If the value is already at the index, do nothing
    if (this.valAppearsAt(val, index))
      return;
    // Shift everything down and put the value at the index.
    this.core.add(index, val);
  } // add(T val)

  /**
   * Determine if the set contains a particular value.
   */
  public boolean contains(T val)
  {
    int index = this.findIndex(val);
    return this.valAppearsAt(val, index);
  } // contains(T)

  /**
   * Remove an element from the set.
   *
   * @post !contains(val)
   * @post For all lav != val, if contains(lav) held before the call
   *   to remove, contains(lav) continues to hold.
   */
  public void remove(T val)
  {
    int index = this.findIndex(val);
    if (this.valAppearsAt(val, index))
      this.core.remove(index);
  } // remove(T)

  // +--------------------------+----------------------------------------
  // | Methods from SemiIndexed |
  // +--------------------------+

  /**
   * Get the element at index i.
   *
   * @throws IndexOutOfBoundsException
   *   if the index is out of range (index < 0 || index >= length)
   */
  public T get(int i)
  {
    return this.core.get(i);
  } // get(int)

  /**
   * Determine the number of elements in the collection.
   */
  public int length()
  {
    return this.core.size();
  } // length()
} // class SortedArrayList<T>

package taojava.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Sorted lists implemented with Java's linked lists.  As one
 * might expect, the iteration and index operations are fairly
 * fast, the insert and remove elements are relatively slow.
 * 
 * @author Samuel A. Rebelsky
 */
public class SortedLinkedList<T extends Comparable<T>>
    implements SortedList<T>
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying LinkedList.
   */
  LinkedList<T> core;

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new sorted list.
   */
  public SortedLinkedList()
  {
    this.core = new LinkedList<T>();
  } // SortedArrayList()

  // +-------------------------+-----------------------------------------
  // | Internal Helper Methods |
  // +-------------------------+

  /**
   * Create a new iterator and advance that iterator to the first value 
   * greater than or equal to val, or to the end of the list.
   */
  ListIterator<T> advanceTo(T val)
  {
    ListIterator<T> it = this.core.listIterator();
    while (it.hasNext())
      {
        // If we pass over a larger/equal value
        T next = it.next();
        if (val.compareTo(next) <= 0)
          {
            // Back up
            it.previous();
            // And we're set
            return it;
          } // if the next value is less than or equal to val
      } // while (it.hasNext()
    // Ran off the end.  Return the iterator
    return it;
  } // advanceTo(T val)

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
        Iterator<T> core = SortedLinkedList.this.core.iterator();

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
    // An iterator for the list.
    ListIterator<T> it = this.advanceTo(val);
    // If there's no next element, add it there
    if (!it.hasNext())
      {
        it.add(val);
      } // if there's no next element
    // If the next element is not equal, add val
    else if (val.compareTo(it.next()) != 0)
      {
        it.previous();
        it.add(val);
      } // if the next element is not val
  } // add(T val)

  /**
   * Determine if the set contains a particular value.
   */
  public boolean contains(T val)
  {
    ListIterator<T> it = this.advanceTo(val);
    return (it.hasNext() && (val.compareTo(it.next()) == 0));
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
    ListIterator<T> it = this.advanceTo(val);
    if (it.hasNext() && (val.compareTo(it.next()) == 0))
      it.remove();
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

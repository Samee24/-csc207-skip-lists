package taojava.expt;

import java.io.PrintWriter;

import java.util.Iterator;

import taojava.util.SortedList;

/**
 * Sorted lists that print out information about themselves.
 * 
 * @author Samuel A. Rebelsky
 */
public class VerboseSortedList<T extends Comparable<T>>
  implements SortedList<T>
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying sorted list.
   */
  SortedList<T> slist;

  /**
   * The name we use for the sorted list.
   */
  String name;

  /**
   * A PrintWriter for printing out information.
   */
  PrintWriter pen;

  /**
   * A count of the iterators we've created.
   */
  int itnum;

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Wrap slist so that all of the methods print out information about
   * what they are doing.
   */
  public VerboseSortedList(SortedList<T> slist, PrintWriter pen, String name)
  {
    this.slist = slist;
    this.name = name;
    this.pen = pen;
    this.itnum = 0;
  } // VerboseSortedList(SortedList<T>, String, PrintWriter)

  // +-----------------------+-------------------------------------------
  // | Methods from Iterable |
  // +-----------------------+

  /**
   * Return a read-only iterator (one that does not implement the remove
   * method) that iterates the values of the list from smallest to
   * largest.
   */
  public Iterator<T> iterator()
  {
    return new Iterator<T>()
    {
      int id = VerboseSortedList.this.itnum++;

      Iterator<T> core = slist.iterator();

      public T next()
      {
        T val = core.next();
        pen.println(name + ".iterator-" + id + ".next() -> " + val);
        return val;
      } // next()

      public boolean hasNext()
      {
        boolean result = core.hasNext();
        pen.println(name + ".iterator-" + id + ".hasNext() -> " + result);
        return result;
      } // hasNext()

      public void remove()
      {
        throw new UnsupportedOperationException();
      } // remove()
    }; // new Iterator<T>
  } // iterator()

  // +------------------------+------------------------------------------
  // | Methods from SimpleSet |
  // +------------------------+

  public void add(T val)
  {
    pen.println(name + ".add(" + val + ")");
    slist.add(val);
  } // add(T val)

  public boolean contains(T val)
  {
    boolean result = slist.contains(val);
    pen.println(name + ".contains(" + val + ") -> " + result);
    return result;
  } // contains(T)

  public void remove(T val)
  {
    pen.println(name + ".remove(" + val + ")");
    slist.remove(val);
  } // remove(T)

  // +--------------------------+----------------------------------------
  // | Methods from SemiIndexed |
  // +--------------------------+

  public T get(int i)
  {
    T result = slist.get(i);
    pen.println(name + ".get(" + i + ") -> " + result);
    return result;
  } // get(int)

  /**
   * Determine the number of elements in the collection.
   */
  public int length()
  {
    int length = slist.length();
    pen.println(name + ".length() -> " + length);
    return length;
  } // length()

  // +---------------+---------------------------------------------------
  // | Other Methods |
  // +---------------+

  /**
   * Print out the sorted list.
   */
  public void dump()
  {
    int len = slist.length();
    if (len == 0)
      {
        pen.println(name + " is empty");
        return;
      }
    pen.print(name + " = [" + slist.get(0));
    for (int i = 1; i < len; i++)
      {
        pen.print(", ");
        pen.print(slist.get(i));
      } // for
    pen.println("]");
  } // dump()
} // class VerboseSortedList

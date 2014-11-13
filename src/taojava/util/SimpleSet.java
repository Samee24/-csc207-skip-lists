package taojava.util;

/**
 * Simple sets of values.  Sets let you add and remove elements,
 * as well as check membership.
 * 
 * @author Samuel A. Rebelsky
 */
public interface SimpleSet<T>
{
  /**
   * Determine if the set contains a particular value.
   */
  public boolean contains(T val);

  /**
   * Add a value to the set.
   *
   * @post contains(val)
   * @post For all lav != val, if contains(lav) held before the call
   *   to add, contains(lav) continues to hold.
   */
  public void add(T val);

  /**
   * Remove an element from the set.
   *
   * @post !contains(val)
   * @post For all lav != val, if contains(lav) held before the call
   *   to remove, contains(lav) continues to hold.
   */
  public void remove(T val);
} // interface SimpleSet<T>

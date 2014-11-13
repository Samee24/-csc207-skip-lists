package taojava.util;

/**
 * Objects that index elements and allow you to retrieve the ith element.
 * 
 * @author Samuel A. Rebelsky
 */
public interface SemiIndexed<T>
{
  /**
   * Get the element at index i.
   *
   * @throws IndexOutOfBoundsException
   *   if the index is out of range (index < 0 || index >= length)
   */
  public T get(int i);

  /**
   * Determine the number of elements in the collection.
   */
  public int length();
} // interface SemiIndexed 

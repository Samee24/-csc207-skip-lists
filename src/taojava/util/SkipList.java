package taojava.util;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * A randomized implementation of sorted lists.  
 * 
 * @author Samuel A. Rebelsky
 * @author Samee Zahid
 * @author William Royle
 */
public class SkipList<T extends Comparable<T>>
    implements SortedList<T>
{
  // +--------+----------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The dummy node at the front of the list.
   */
  Node front;

  /**
   * The dummy node at the back of the list.
   */
  Node back;

  /**
   * The number of modifications to the list.  Used to determine
   * whether an iterator is valid.
   */
  long mods;

  /**
   * The highest possible level which a node can actually have
   */
  int maxLevel;
  
  /**
   * The number of elements in the SkipList
   */
  int length;

  /**
   * The probabilty that a level generated for a node will be 0. The probability
   * that a level generated will be 1 is probability*(1-probability).
   * The logic continues for the likelihood of higher levels appearing
   */
  double probability = .5;

  // +------------------+------------------------------------------------
  // | Internal Classes |
  // +------------------+

  /**
   * Nodes for skip lists.
   */
  public class Node
  {
    // +--------+--------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * The value stored in the node.
     */
    T val;
    
    /**
     * The array of nodes which this node points to, length of nodeList must
     * be less than maxLevel+1
     */
    Node[] nodeList;

    // +--------------+----------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Create a Node with the given val and Nodes, with the maxLevel of the node
     * being nodeList.length-1.
     */
    public Node(T val, Node[] nodeList)
    {
      this.val = val;
      this.nodeList = nodeList;
    }// Node(T val, Node[] nodeList)

  }// class Node

  // +--------------+----------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Creates an empty SkipList with probability .5
   */
  public SkipList()
  {
    this(.5);
  } // SkipList()

  /**
   * Creates an empty SkipList with the given probability
   */
  @SuppressWarnings({ "unchecked" })
  public SkipList(double probability)
  {
    this.probability = probability;
    // We set the highest level any node is allowed to have
    maxLevel = 17;
    // We make a back dummy node for the front dummy node to point to
    back = new Node(null, null);
    /*
     * logic for creating generic array of Nodes taken from:
     *  http://courses.cs.washington.edu/courses/cse332/10sp/otherNotes/genericArrays.html
     *  This list of nodes all pointing to back is the list of nodes given to front
     */
    Node[] frontNodes = (Node[]) new SkipList.Node[maxLevel + 1];
    for (int filler = 0; filler < frontNodes.length; filler++)
      {
        frontNodes[filler] = back;
      } // for
    /*
     *  We have front having the highest possible level, with each Node in the array
     *  pointing to last
     */
    front = new Node(null, frontNodes);
    mods = 0;
  } // SkipList(double probability)
  
  // +-------------------------+-----------------------------------------
  // | Internal Helper Methods |
  // +-------------------------+
  
  

  /*
   * Generates an integer which is the maxLevel a node can have, corresponding
   * in probability to the desired distribution for randomly generated nodes. Runs
   * in at most log(MaxLevels), which is negligable. The probabilty that a level 
   * generated for a node will be 0 is probability. The probability that a level
   * generated will be 1 is probability*(1-probability). This logic continues for 
   * the likelihood of higher levels appearing, up to maxLevels
   */
  public int levelGenerator()
  {
    // Generate double between 0 and 1
    double levelIndicator = new Random().nextDouble();
    /*
     *  If our double is below max, we say that our levelGenerator should return
     *  the current level
     */
    double max = probability;
    // We loop through each level
    for (int level = 0; level < maxLevel; level++)
      {
        if (levelIndicator < max)
          {
            return level;
          }// if
        // We increase max
        max += (1 - max)*probability;
      }// for
    return maxLevel;
  }// levelGenerator()

  // +-----------------------+-------------------------------------------
  // | Methods from Iterable |
  // +-----------------------+

  /**
   * Return a read-only iterator (one that does not implement the remove
   * method) that iterates the values of the list from smallest to
   * largest.
   */
  // Parts of code borrowed from Samuel Rebelsky's linked list lab
  public Iterator<T> iterator()
  {
    return new Iterator<T>()
      {
        // +--------+------------------------------------------------------
        // | Fields |
        // +--------+

        /**
         * The node that immediately precedes the value to be returned 
         * by next.
         */
        Node cursor = front;

        /**
         * The number of modifications at the time this iterator was
         * created or last updated.
         */
        long mods = SkipList.this.mods;

        /**
         * A flag to keep track of whether we can remove an element
         * in the list - it's dependent on next() being called right
         * after remove.
         */
        boolean canRemove = true;

        // +---------+-----------------------------------------------------
        // | Helpers |
        // +---------+

        /**
         * Determine if the list has been updated since this iterator
         * was created or modified.
         */
        void failFast()
        {
          if (this.mods != SkipList.this.mods)
            throw new ConcurrentModificationException();
        }// failFast()

        // +---------+-----------------------------------------------------
        // | Methods |
        // +---------+

        /**
         * Determine if the has an additional element after the current position
         * of the iterator
         */
        public boolean hasNext()
        {
          failFast();
          return this.cursor.nodeList[0].val != null;
        } // hasNext()

        /**
         * Returns the next element after the current position of the iterator and
         * increments the position of the iterator
         */
        public T next()
          throws NoSuchElementException
        {
          canRemove = true;
          failFast();
          if (!this.hasNext())
            throw new NoSuchElementException();
          // Advance to the next node.
          this.cursor = this.cursor.nodeList[0];
          // The next value is in the current node.
          return this.cursor.val;
        }// next()

        /**
         * Removes the element which was last returned by next from the SkipList
         * @post If remove() is called twice in a row, an IllegalStateException will
         *      be thrown
         */
        public void remove()
          throws IllegalStateException
        {
          // We throw an exception if we cannot remove an element 
          if (!canRemove)
            throw new IllegalStateException("Must call next before remove");
          // We save info on the  node we want to remove
          T removeVal = cursor.val;
          int nodeLevel = cursor.nodeList.length - 1;
          Node currentFront = front;
          for (int level = maxLevel; level >= 0; level--)
            {
              /*
               *  While element to be removed is larger than value at where currentFront points,
               *  we increment currentFront
               */
              while (currentFront.nodeList[level].val != null
                     && currentFront.nodeList[level].val.compareTo(removeVal) < 0)
                {
                  currentFront = currentFront.nodeList[level];
                }// while
              /*
               *  If the element we want to remove is at least on the current level, and we point
               *  to a different node with the same value, we increment currentFront
               */
              while (level <= nodeLevel
                     && currentFront.nodeList[level].val != null
                     && currentFront.nodeList[level] != cursor
                     && currentFront.nodeList[level].val.compareTo(removeVal) == 0)
                {
                  currentFront = currentFront.nodeList[level];
                }
              // We remove references to the node we are removing
              if (level <= nodeLevel && currentFront.nodeList[level] == cursor)
                {
                  currentFront.nodeList[level] = cursor.nodeList[level];
                }

            }
          mods++;
          SkipList.this.mods++;
          length--;
          canRemove = false;
        }// remove()
      };
  }// iterator()

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
  @SuppressWarnings({ "unchecked" })
  public void add(T val)
  {
    // We get a level for the new node
    int newLevel = levelGenerator();
    // The nodes which our new node points to
    Node[] nodeLinks = (Node[]) new SkipList.Node[newLevel + 1];
    // Initialize the new node
    Node newNode = new Node(val, nodeLinks);
    /*
     *  We loop through the array at each level, updating nodeLinks and the 
     *  currentFront pointer at each stage if needed. currentFront is our current
     *  position in the list 
     */
    Node currentFront = front;
    for (int level = maxLevel; level >= 0; level--)
      {
        /*
         *  while element to be inserted is larger than value at where currentFront points,
         *  we increment currentFront
         */
        while (currentFront.nodeList[level].val != null
               && currentFront.nodeList[level].val.compareTo(val) < 0)
          {
            currentFront = currentFront.nodeList[level];
          }// while
        /*
         * If needed, we update both the currentFront nodeList at the appropriate level and 
         * the nodeLinks (in other words, insert our new node for the given level)
         */
        if (level <= newLevel)
          {
            nodeLinks[level] = currentFront.nodeList[level];
            currentFront.nodeList[level] = newNode;
          }// if
      }// for
    mods++;
    length++;
  }// add(T val)

  /**
   * Determine if the set contains a particular value.
   */
  public boolean contains(T val)
  {
    Node currentFront = front;
    /*
     *  We loop through the array at each level, updating the currentFront pointer
     *  at each stage if needed, until currentFront is pointing at the element in the list
     *  we want (if such element exists)
     */
    for (int level = maxLevel; level >= 0; level--)
      {
        /*
         *  while element to be inserted is larger than value at where currentFront points,
         *  we increment currentFront
         */
        while (currentFront.nodeList[level].val != null
               && currentFront.nodeList[level].val.compareTo(val) < 0)
          {
            currentFront = currentFront.nodeList[level];
          }// while
      }// for
    //We return true if the first element from currentFront nodeList points to val
    if (currentFront.nodeList[0].val == null)
      {
        return false;
      }// if
    return currentFront.nodeList[0].val.equals(val);
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
    Node currentFront = front;
    /* 
     * We loop through levels, removing all nodes with val at a given level
     * with each iteration of level
     */
    for (int level = maxLevel; level >= 0; level--)
      {
        /*
         *  while element to be inserted is larger than value at where currentFront points,
         *  we increment currentFront
         */
        while (currentFront.nodeList[level].val != null
               && currentFront.nodeList[level].val.compareTo(val) < 0)
          {
            currentFront = currentFront.nodeList[level];
          }// while
        /*
         *  If currentFront at the specified level points to a node we want to remove, we remove
         *  the node
         */
        if (currentFront.nodeList[level].val != null
            && currentFront.nodeList[level].val.compareTo(val) == 0)
          {
            currentFront.nodeList[level] =
                currentFront.nodeList[level].nodeList[level];
            /*
             *  We increment level in case there are multiple nodes of the same level in a row which
             *  we want to delete
             */
            level++;
            /*
             *  We count the number of elements we remove, because that will be the number of zero level
             *  links we remove
             */
            if (level==0)
              {
                length++;
              }// if
          }// if
      }// for
    mods++;
  }// remove(T)

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
    throws IndexOutOfBoundsException
  {
    // STUB
    return null;
  } // get(int)

  /**
   * Determine the number of elements in the collection.
   */
  public int length()
  {
    return length;
  } // length()

  //Delete Me, prints every node except for last
  public void print()
  {
    Node cursor = front;
    System.out.println("We now print a SkipList:\n");
    while (cursor.nodeList != null)
      {
        System.out.println("Node Value is " + cursor.val);
        System.out.println("Maximum Node Level is "
                           + (cursor.nodeList.length - 1) + " of " + maxLevel);
        for (int count = 0; count < cursor.nodeList.length; count++)
          {
            System.out.println("nodeList[" + count
                               + "] points to node with val="
                               + cursor.nodeList[count].val);
          }
        cursor = cursor.nodeList[0];
        System.out.println();
      }

  }

  public static void main(String[] args)
  {

    SkipList<Integer> theList = new SkipList<Integer>(.5);
    System.out.println("New SkipList:");

    //  theList.add(131);
    theList.add(2);
    theList.add(2);
    theList.add(31);
    theList.add(31);
    theList.print();
  }

} // class SkipList<T>


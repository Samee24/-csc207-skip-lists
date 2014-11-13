package taojava.analysis;

import java.io.PrintWriter;

import taojava.util.SortedLinkedList;

/**
 * Quick and dirty analysis of SortedArrayLists.
 *
 * @author Samuel A. Rebelsky
 */
public class SortedLinkedListAnalyzer
{

  public static void main(String[] args)
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    SortedListAnalyzer.analyze(pen, new SortedLinkedList<Integer>(), 16000, 20);
    pen.close();
  } // main(String[])

} // SortedArrayListAnalyzer

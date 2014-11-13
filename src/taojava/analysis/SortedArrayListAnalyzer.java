package taojava.analysis;

import java.io.PrintWriter;

import taojava.util.SortedArrayList;

/**
 * Quick and dirty analysis of SortedArrayLists.
 *
 * @author Samuel A. Rebelsky
 */
public class SortedArrayListAnalyzer
{

  public static void main(String[] args)
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    SortedListAnalyzer.analyze(pen, new SortedArrayList<Integer>(), 4000, 20);
    pen.close();
  } // main(String[])

} // SortedArrayListAnalyzer




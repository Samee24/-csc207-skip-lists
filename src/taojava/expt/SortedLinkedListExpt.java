package taojava.expt;

import java.io.PrintWriter;

import taojava.util.SortedLinkedList;

/**
 * An experiment with SortedLinkedLists.
 * 
 * @author Samuel A. Rebelsky
 */
public class SortedLinkedListExpt
{
  public static void main(String[] args)
    throws Exception
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    SortedListExpt.stringExperiment(pen, new SortedLinkedList<String>());
    pen.flush();
  } // main(String[])
} // class SortedLinkedListExpt


package taojava.expt;

import java.io.PrintWriter;

import taojava.util.SortedArrayList;

/**
 * An experiment with SortedArrayLists.
 * 
 * @author Samuel A. Rebelsky
 */
public class SortedArrayListExpt
{
  public static void main(String[] args)
    throws Exception
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    SortedListExpt.stringExperiment(pen, new SortedArrayList<String>());
    pen.flush();
  } // main(String[])
} // class SortedArrayListExpt


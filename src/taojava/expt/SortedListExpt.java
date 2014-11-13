package taojava.expt;

import java.io.PrintWriter;

import taojava.util.SortedList;

/**
 * Some simple experiments with sorted lists.  Intended as helpers
 * for experiments with particular implementations of sorted lists.
 *
 * @author Samuel A. Rebelsky
 */
public class SortedListExpt
{
  /**
   * Print a heading.
   */
  static void heading(PrintWriter pen, String title)
  {
    pen.println();
    for (int i = 0; i < 72; i++)
      pen.print("*");
    pen.println();
    pen.println("*** " + title + " ***");
  } // heading

  /**
   * An experiment involving strings.
   */
  public static void
    stringExperiment(PrintWriter pen, SortedList<String> slist)
  {
    VerboseSortedList<String> strings =
        new VerboseSortedList<String>(slist, pen, "strings");

    // Add a bunch of strings
    heading(pen, "Adding first");
    String[] first =
        new String[] { "twas", "brillig", "and", "the", "slithy", "toves",
                      "did", "gyre", "and", "gimble", "in", "the", "wabe" };
    for (String str : first)
      strings.add(str);

    // Look at the resulting list
    heading(pen, "Dumping contents");
    strings.dump();

    // Iterate through the list once.
    heading(pen, "Iterating");
    for (String str : strings)
      ;

    // Remove some of the elements
    heading(pen, "Removing");
    for (int i = 0; i < first.length; i += 2)
      strings.remove(first[i]);

    // Look at the resulting list
    heading(pen, "Dumping contents");
    strings.dump();

    // Iterate through the list once.
    heading(pen, "Iterating again");
    for (String str : strings)
      ;

    // Add a few more strings
    String[] second =
        new String[] { "all", "mimsy", "were", "the", "borogoves" };
    heading(pen, "Adding additional strings");
    for (String str : second)
      strings.add(str);

    // Look at the resulting list
    heading(pen, "Dumping contents");
    strings.dump();

    // And look up elements by index
    heading(pen, "Iterating by index");
    int len = slist.length();
    for (int i = 1; i < len; i+= 3)
      strings.get(i);
  } // stringExperiment
} // SortedListExpt

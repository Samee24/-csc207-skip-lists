package taojava.analysis;

import java.io.PrintWriter;

import java.util.Iterator;
import java.util.Random;

import taojava.util.SortedList;

/**
 * Mechanisms for analyzing the efficiency of various operations in 
 * sorted lists.
 * 
 * @author Samuel A. Rebelsky
 */
public class SortedListAnalyzer
{
  /**
   * Run some simple analysis on a SortedList, using N (or multiples 
   * of N) steps.  Adds N random elements, looks up N elements by index,
   * iterates the list, adds N more elements, removes N elements, removes
   * N more elements.
   * 
   * @return data
   *   An array containing 0: overall time; 1: time for first N adds;
   *   2: time for N calls to index; 3: time for iteration; 4: time
   *   for next N adds; 5: time for first N removes; 6: time for next
   *   N removes.
   */
  public static long[] analyze(SortedList<Integer> sl, int n)
  {
    // Set up a friendly random number generator.
    Random random = new Random();

    // Set up timers for various parts of the analysis.
    SimpleTimer overall = new SimpleTimer(); // Overall time
    SimpleTimer adds1 = new SimpleTimer(); // First n adds
    SimpleTimer indices = new SimpleTimer(); // n calls to get
    SimpleTimer iterate = new SimpleTimer(); // Time to iterate
    SimpleTimer adds2 = new SimpleTimer(); // Next n adds
    SimpleTimer removes1 = new SimpleTimer(); // First n removes
    SimpleTimer removes2 = new SimpleTimer(); // Next n removes

    overall.start();

    // First set of additions (all even)
    adds1.start();
    for (int i = 0; i < n; i++)
      {
        sl.add(2 * random.nextInt(4 * n));
      } // for
    adds1.stop();

    // Grab the length (which may not be n)
    int len = sl.length();

    // Index
    indices.start();
    for (int i = 0; i < n; i++)
      {
        sl.get(random.nextInt(len));
      } // for
    indices.stop();

    // Iterate
    iterate.start();
    for (Integer i : sl)
      ;
    iterate.stop();

    // Second set of additions (all odd)
    adds2.start();
    for (int i = 0; i < n; i++)
      {
        sl.add(1 + 2 * random.nextInt(4 * n));
      } // for
    adds2.stop();

    // First set of removals (all even)
    removes1.start();
    Iterator<Integer> it = sl.iterator();
    while (it.hasNext())
      {
        if (it.next() % 2 == 0)
          it.remove();
      } // while (it.hasNext())
    removes2.stop();

    // Second set of removals
    removes2.start();
    it = sl.iterator();
    while (it.hasNext())
      {
        it.next();
        it.remove();
      } // while (it.hasNext();
    removes2.stop();

    // And we're done
    overall.stop();
    return new long[] { overall.elapsed(), adds1.elapsed(), indices.elapsed(),
                       iterate.elapsed(), adds2.elapsed(), removes1.elapsed(),
                       removes2.elapsed() };
  } // analyze(SortedList<Integer>, int)

  /**
   * Do a few individual analyses, printing out the results of each
   * as well as the average.
   */
  public static void analyze(PrintWriter pen, SortedList<Integer> sl, int n,
                             int reps)
  {
    pen.printf("         %8s%8s%8s%8s%8s%8s%8s\n", "add/1", "index", "iterate",
               "add/2", "rem/1", "rem/2", "total");

    long[] results = new long[7];
    for (int rep = 0; rep < reps; rep++)
      {
        long[] round = analyze(sl, n);
        for (int i = 0; i < results.length; i++)
          results[i] += round[i];
        pen.printf("Round %2d %8d%8d%8d%8d%8d%8d%8d\n", rep, round[1], round[2],
                   round[3], round[4], round[5], round[6], round[0]);
      } // for (i)
    long[] averages = new long[7];
    for (int i = 0; i < results.length; i++)
      averages[i] = results[i] / reps;
    pen.printf("Average  %8d%8d%8d%8d%8d%8d%8d\n", averages[1], averages[2],
               averages[3], averages[4], averages[5], averages[6], averages[0]);
  } // analyze(PrintWriter, SortedList<Integer>, int, int)
} // class SortedListAnalyzer

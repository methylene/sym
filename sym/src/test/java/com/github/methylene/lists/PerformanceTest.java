package com.github.methylene.lists;

import static java.lang.System.nanoTime;
import com.github.methylene.sym.Util;
import org.junit.Test;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class PerformanceTest {

  private static String cutoff(double f, int width) {
    String s = Double.toString(f);
    return  (s.length() > width) ?
      s.substring(0, width) :
    s;
  }

  @Test
  public void testPerf() {
    long index = 0;
    long lastIndex = 0;
    long indexJdk = 0;
    long lastIndexJdk = 0;
    int repeat = 50;
    int size = 10000; // list size
    int maxNumber = 20000;
    int[] a = Util.randomNumbers(maxNumber, size);
    Integer candidate = (int) (Math.random() * maxNumber);
    IntList asList = (IntList) LookupList.of(a);
    List<Integer> jdk = Arrays.asList(Util.box(a));
    long check;
    for (int _ = 0; _ < repeat; _ += 1) {

      check = nanoTime();
      asList.indexOf(candidate);
      index += (nanoTime() - check);

      check = nanoTime();
      jdk.indexOf(candidate);
      indexJdk += (nanoTime() - check);

      check = nanoTime();
      asList.lastIndexOf(candidate);
      lastIndex += (nanoTime() - check);

      check = nanoTime();
      jdk.lastIndexOf(candidate);
      lastIndexJdk += (nanoTime() - check);

    }
    // format and print
    int width = 12;
    PrintStream out = System.out;
    out.println("== list size: " + size);
    out.println("== repeat: " + repeat);
    out.println("== return value of .indexOf: " + Util.indexOf(a, candidate, 0));
    out.format("indexOf:            %1$" + width + "s%n", cutoff((double) index / repeat, width));
    out.format("indexOfjdK:         %1$" + width + "s%n", cutoff((double) indexJdk / repeat, width));
    out.format("lastIndexOf:        %1$" + width + "s%n", cutoff((double) lastIndex / repeat, width));
    out.format("lastIndexOfJdk      %1$" + width + "s%n", cutoff((double) lastIndexJdk / repeat, width));
    out.format("index_relative:     %1$" + width + "s%n", cutoff((double) index / indexJdk, width));
    out.format("lastIndex_relative: %1$" + width + "s%n", cutoff((double) lastIndex / lastIndexJdk, width));
  }

}

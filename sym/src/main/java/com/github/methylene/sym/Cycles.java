package com.github.methylene.sym;

import static com.github.methylene.sym.Util.duplicateFailure;
import static com.github.methylene.sym.Util.indexFailure;
import static com.github.methylene.sym.Util.indexOf;
import static com.github.methylene.sym.Util.lengthFailure;
import static com.github.methylene.sym.Util.negativeFailure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of methods that return cycles or operate on cycles.
 */
public final class Cycles {

  private Cycles() {}

  /**
   * Get the indexes that are moved by the cycle.
   * @param cycle a cycle in cycle notation
   * @return the indexes that are moved by the cycle
   * @throws java.lang.IllegalArgumentException if the input does not define a cycle
   */
  public static boolean[] movedIndexes(int[] cycle) {
    if (cycle.length == 0)
      return new boolean[0];
    boolean[] moved = new boolean[Util.max(cycle) + 1];
    for (int el : cycle) {
      if (el < 0)
        negativeFailure();
      if (moved[el])
        duplicateFailure();
      moved[el] = true;
    }
    return moved;
  }

  /**
   * Create a ranking from a cycle in cycle notation.
   * @param cycle a cycle in cycle notation
   * @return the cycle as a ranking
   * @throws java.lang.IllegalArgumentException if the input does not define a cycle
   */
  static public int[] cycle(int... cycle) {
    boolean[] moved = movedIndexes(cycle);
    int[] ranking = new int[moved.length];
    for (int i = 0; i < moved.length; i += 1)
      ranking[i] = moved[i] ? cycle[(indexOf(cycle, i, 0) + 1) % cycle.length] : i;
    return ranking;
  }

  public static int orbitLength(int[] ranking, int i) {
    if (i < 0 || i >= ranking.length)
      lengthFailure();
    int length = 1;
    int j = i;
    while ((j = ranking[j]) != i)
      length++;
    return length;
  }

  public static int[] orbit(int[] ranking, int pos, int orbitLength) {
    if (pos < 0 || pos >= ranking.length)
      indexFailure();
    int[] result = new int[orbitLength];
    result[0] = pos;
    int j = pos;
    for (int k = 1; k < result.length; k += 1) {
      j = ranking[j];
      result[k] = j;
    }
    return result;
  }

  public static int[] orbit(int[] ranking, int i) {
    return orbit(ranking, i, orbitLength(ranking, i));
  }

  public static boolean isCyclicRanking(int[] ranking) {
    int[] candidate = null;
    for (int i = 0; i < ranking.length; i += 1) {
      int orbitLength = orbitLength(ranking, i);
      if (orbitLength > 1) {
        if (candidate == null) {
          candidate = Util.sortedCopy(orbit(ranking, i, orbitLength));
        } else {
          if (Arrays.binarySearch(candidate, i) < 0) {
            return false;
          }
        }
      }
    }
    return true;
  }


  public static List<int[]> toOrbits(int[] ranking) {
    LinkedList<int[]> orbits = new LinkedList<int[]>();
    boolean[] done = new boolean[ranking.length];
    for (int i = 0; i < ranking.length; i += 1) {
      if (!done[i]) {
        int orbitLength = orbitLength(ranking, i);
        if (orbitLength > 1) {
          int[] candidate = orbit(ranking, i, orbitLength);
          for (int k : candidate)
            done[k] = true;
          boolean goodCandidate = true;
          for (int[] orbit : orbits) {
            if (orbit.length == candidate.length) {
              if (indexOf(orbit, candidate[0], 0) != -1) {
                goodCandidate = false;
                break;
              }
            }
          }
          if (goodCandidate) {orbits.push(candidate);}
        }
      }
    }
    return orbits;
  }

  public static List<int[]> toTranspositions(int[] ranking) {
    List<int[]> transpositions = new ArrayList<int[]>(ranking.length);
    for (int[] orbit : toOrbits(ranking))
      for (int i = 0; i < orbit.length - 1; i += 1)
        transpositions.add(new int[]{ orbit[i], orbit[i + 1] });
    return transpositions;
  }


}

package com.github.methylene.sym;

import static com.github.methylene.sym.Util.duplicateFailure;
import static com.github.methylene.sym.Util.indexOf;
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
   * Check if the input defines a cycle.
   * @param a an array
   * @return true if the input defines a cycle, because it contains no negative
   * numbers or duplicates
   */
  public static boolean isCycle(int[] a) {
    boolean[] used = new boolean[Util.max(a) + 1];
    for (int i : a) {
      if (i < 0)
        return false;
      if (used[i])
        return false;
      used[i] = true;
    }
    return true;
  }

  /**
   * Create a ranking from a cycle in cycle notation.
   * @param cycle a cycle in cycle notation
   * @return the cycle as a ranking
   * @throws java.lang.IllegalArgumentException if the input does not define a cycle
   * @see #isCycle
   */
  public static int[] asRanking(int... cycle) {
    boolean[] moved = movedIndexes(cycle);
    int[] ranking = new int[moved.length];
    for (int i = 0; i < moved.length; i += 1)
      ranking[i] = moved[i] ? cycle[(indexOf(cycle, i, 0) + 1) % cycle.length] : i;
    return ranking;
  }

  public static int order(int[] ranking, final int i) {
    Rankings.checkRanking(ranking);
    int length = 1;
    int j = i;
    while ((j = ranking[j]) != i)
      length++;
    return length;
  }

  /**
   * Calculate the orbit of an index
   * @param ranking a ranking
   * @param i an non-negative integer
   * @return the orbit of {@code i}
   * @throws java.lang.IllegalArgumentException if {@code i} is negative
   */
  public static int[] orbit(int[] ranking, int i) {
    if (i < 0)
      negativeFailure();
    if (i >= ranking.length || ranking[i] == i)
      return new int[]{i};
    int[] result = new int[order(ranking, i)];
    result[0] = i;
    for (int k = 1; k < result.length; k += 1)
      result[k] = (i = ranking[i]);
    return result;

  }

  public static boolean isCyclicRanking(int[] ranking) {
    int[] candidate = null;
    for (int i = 0; i < ranking.length; i += 1) {
      if (ranking[i] != i) {
        if (candidate == null) {
          candidate = Util.sortedCopy(orbit(ranking, i));
        } else {
          if (Arrays.binarySearch(candidate, i) < 0) {
            return false;
          }
        }
      }
    }
    return true;
  }


  public static int[][] toOrbits(int[] ranking) {
    int[][] orbits = new int[ranking.length / 2][];
    boolean[] done = new boolean[ranking.length];
    int cnt = 0;
    outer:
    for (int i = 0; i < ranking.length; i += 1) {
      if (!done[i] && ranking[i] != i) {
        int[] candidate = orbit(ranking, i);
        for (int k : candidate)
          done[k] = true;
        for (int j = 0; j < cnt; j++)
          if (orbits[j].length == candidate.length
              && indexOf(orbits[j], candidate[0]) >= 0)
            continue outer;
        orbits[cnt++] = candidate;
      }
    }
    return orbits.length == cnt ? orbits : Arrays.copyOf(orbits, cnt);
  }

  /**
   * Write the input ranking as a list of transpositions.
   * This method does not check if the input is indeed a valid ranking and may have unexpected results otherwise.
   * @param ranking a ranking
   * @param factory a transposition factory
   * @return a list of transpositionst that is equivalent to the input {@code ranking}
   */
  public static List<Transposition> toTranspositions(int[] ranking, Transposition.TranspositionFactory factory) {
    ArrayList<Transposition> transpositions = new ArrayList<Transposition>(ranking.length);
    for (int[] orbit : toOrbits(ranking))
      for (int i = 0; i < orbit.length - 1; i++)
        transpositions.add(factory.swap(orbit[i], orbit[i + 1]));
    return transpositions;
  }

  /**
   * Write the input ranking as a list of transpositions.
   * This method does not check if the input is indeed a valid ranking and may have unexpected results otherwise.
   * @param ranking a ranking
   * @return a list of transpositions that is equivalent to the input {@code ranking}
   */
  public static List<Transposition> toTranspositions(int[] ranking) {
    return toTranspositions(ranking, Transposition.NON_CACHING_FACTORY);
  }

}

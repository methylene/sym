package com.github.methylene.sym;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Cycles {

  static public int[] cycle(int... cycle) {
    int maxIndex = -1;
    for (int index : cycle) {
      if (index < 0)
        throw new IllegalArgumentException("negative index: " + index);
      maxIndex = Math.max(maxIndex, index);
    }
    int length = maxIndex + 1;
    boolean[] cycleIndexes = new boolean[length];
    for (int i : cycle) {
      if (cycleIndexes[i])
        throw new IllegalArgumentException("duplicate index: " + i);
      cycleIndexes[i] = true;
    }
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1)
      result[i] = !cycleIndexes[i] ? i : cycle[(Util.indexOf(cycle, i) + 1) % cycle.length];
    return result;
  }

  public static int orbitLength(int[] ranking, int i) {
    if (i < 0 || i >= ranking.length)
      throw new IllegalArgumentException("bad index: " + i);
    int length = 1;
    int j = i;
    while ((j = ranking[j]) != i)
      length += 1;
    return length;
  }

  public static int[] orbit(int[] ranking, int pos, int orbitLength) {
    if (pos < 0 || pos >= ranking.length)
      throw new IllegalArgumentException("wrong pos: " + pos);
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

  public static boolean isCycle(int[] ranking) {
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
              if (Util.indexOf(orbit, candidate[0]) != -1) {
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

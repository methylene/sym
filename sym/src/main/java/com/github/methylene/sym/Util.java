package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;

class Util {

  static int[][] withIndex(int[] a) {
    int[][] result = new int[a.length][];
    for (int i = 0; i < a.length; i += 1)
      result[i] = new int[]{i, a[i]};
    return result;
  }

  static int indexOf(int[] ints, int k) {
    for (int i = 0; i < ints.length; i += 1)
      if (ints[i] == k)
        return i;
    throw new IllegalArgumentException("could not find " + k + " in input");
  }

  static int[] add(int[] a, int k) {
    int[] result = new int[a.length];
    for (int i = 0; i < a.length; i += 1)
      result[i] = a[i] + k;
    return result;
  }

  static int[] pad(int[] unpadded, int targetLength) {
    if (targetLength <= unpadded.length)
      return unpadded;
    int[] result = new int[targetLength];
    System.arraycopy(unpadded, 0, result, 0, unpadded.length);
    for (int i = unpadded.length; i < targetLength; i += 1) {
      result[i] = i;
    }
    return result;
  }

  /**
   * Check that the input can be used as a posmap: Each number from {@code 0} through
   * {@code posmap.length - 1} is contained in it exactly once.
   * @param posmap
   */
  static void validate(int[] posmap) {
    boolean[] used = new boolean[posmap.length];
    for (int i : posmap) {
      if (i < 0 || i >= posmap.length)
        throw new IllegalArgumentException("invalid input: " + Arrays.toString(posmap));
      if (used[i])
        throw new IllegalArgumentException("invalid input: " + Arrays.toString(posmap) + ", duplicate: " + i);
      used[i] = true;
    }
  }

  /**
   * @param size      How many random integers we want
   * @param maxFactor Controls the size of random numbers that are produced
   * @return Random array of {@code size} distinct integers between {@code 0} and {@code size * maxFactor}
   */
  static int[] distinctInts(int size, int maxFactor) {
    boolean[] test = new boolean[size * maxFactor];
    int[] result = new int[size];
    for (int i = 0; i < size; i += 1) {
      Integer candidate = (int) (size * maxFactor * Math.random());
      int direction = Math.random() >= 0.5 ? 1 : -1;
      while (test[candidate]) {
        candidate += direction;
        if (candidate == test.length) {
          candidate -= test.length;
        } else if (candidate < 0) {
          candidate += test.length;
        }
      }
      test[candidate] = true;
      result[i] = candidate;
    }
    return result;
  }

  static String[] symbols(int n) {
    String[] r = new String[n];
    String s = "a";
    for (int i = 0; i < n; i += 1) {
      r[i] = s;
      s = nextString(s);
    }
    return r;
  }

  static String nextString(String s) {
    char last = s.charAt(s.length() - 1);
    if (last == 'z') {
      int nflip = 1;
      while (s.length() > nflip && s.charAt(s.length() - 1 - nflip) == 'z')
        nflip += 1;
      if (nflip == s.length()) {
        StringBuilder news = new StringBuilder();
        for (int i = 0; i < nflip; i += 1)
          news.append('a');
        return news.append('a').toString();
      } else {
        StringBuilder news = new StringBuilder(s.substring(0, s.length() - nflip - 1));
        news.append((char) (s.charAt(s.length() - 1 - nflip) + 1));
        for (int i = 0; i < nflip; i += 1)
          news.append('a');
        return news.toString();
      }
    } else {
      return s.substring(0, s.length() - 1) + ((char) (last + 1));
    }
  }

  /**
   * Check if a and b have a common element.
   * @param maxValue maximum value of all numbers in a and b
   * @param a        an array of non negative integers
   * @param b        an array of non negative integers
   * @return true if a and b have no common element
   */
  static boolean disjoint(int maxValue, int[] a, int[] b) {
    boolean[] test = new boolean[maxValue];
    for (int i : a)
      test[i] = true;
    for (int i : b)
      if (test[i])
        return false;
    return true;
  }

  static char[] sortedCopy(char[] chars) {
    char[] sorted = Arrays.copyOf(chars, chars.length);
    Arrays.sort(sorted);
    return sorted;
  }

  static double[] sortedCopy(double[] chars) {
    double[] sorted = Arrays.copyOf(chars, chars.length);
    Arrays.sort(sorted);
    return sorted;
  }

  static float[] sortedCopy(float[] chars) {
    float[] sorted = Arrays.copyOf(chars, chars.length);
    Arrays.sort(sorted);
    return sorted;
  }

  static long[] sortedCopy(long[] chars) {
    long[] sorted = Arrays.copyOf(chars, chars.length);
    Arrays.sort(sorted);
    return sorted;
  }

  static byte[] sortedCopy(byte[] chars) {
    byte[] sorted = Arrays.copyOf(chars, chars.length);
    Arrays.sort(sorted);
    return sorted;
  }

  static Comparable[] sortedCopy(Comparable[] input) {
    Comparable[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted);
    return sorted;
  }

  static Object[] sortedCopy(Object[] input, Comparator comp) {
    Object[] sorted = Arrays.copyOf(input, input.length);
    Arrays.sort(sorted, comp);
    return sorted;
  }

  static int[] sortedCopy(int[] ints) {
    int[] sorted = Arrays.copyOf(ints, ints.length);
    Arrays.sort(sorted);
    return sorted;
  }

}
